import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Harsh Gupta on {1/27/18}
 */
public class SkiingInSingapore {

    private static final String TEXT_DELIMITER = " ";
    private int longestPath = 0;
    private int largestDrop = 0;

    public int getLongestPath() {
        return longestPath;
    }

    public void setLongestPath(int longestPath) {
        this.longestPath = longestPath;
    }

    public int getLargestDrop() {
        return largestDrop;
    }

    public void setLargestDrop(int largestDrop) {
        this.largestDrop = largestDrop;
    }

    public void solveLongestGrid(String pathToFile){
        int mountainMatrix[][];
        try {
            mountainMatrix = parseDataFromFile(pathToFile);
        } catch (IOException  | URISyntaxException e) {
            System.out.println("Exception while Parsing file");
            return;
        }
        // Util Matrix to traverse in four directions : East, West, North , South
        int util [][] = {
                { 0, -1, 1, 0 },
                { 1, 0, 0, -1 }
        };

        // Traversing through every cell and  keeping a check on longest path and largest drop
        for(int i=0;i<mountainMatrix.length;i++){
            for(int j=0; j< mountainMatrix[0].length;j++){
                List<Grid> currentLongestPath = getLongestSkiingPathFromGrid(i,j,mountainMatrix,util);
                if(currentLongestPath.size() > longestPath){
                    longestPath = currentLongestPath.size();
                    largestDrop = mountainMatrix[currentLongestPath.get(0).getX()][currentLongestPath.get(0).getY()] -
                                    mountainMatrix[currentLongestPath.get(currentLongestPath.size() -1).getX()][currentLongestPath.get(currentLongestPath.size() -1).getY()];

                } else if(currentLongestPath.size() == longestPath){
                    int currentDrop = mountainMatrix[currentLongestPath.get(0).getX()][currentLongestPath.get(0).getY()] -
                            mountainMatrix[currentLongestPath.get(currentLongestPath.size() -1).getX()][currentLongestPath.get(currentLongestPath.size() -1).getY()];
                    largestDrop = currentDrop > largestDrop ? currentDrop : largestDrop;

                }
            }
        }

    }

    /**
     *
     * @param row
     * @param column
     * @param mountainMatrix
     * @param totalRows
     * @param totalColumns
     * @param currentValue
     * @return Boolean value
     * LOGIC : Returns true if the grid (mountain slope ) is allowed from the last grid.
     */
    private boolean isGridAllowed(int row,int column, int mountainMatrix[][], int totalRows, int totalColumns, int currentValue){
        return (row >=0 && row < totalRows
                && column >=0 && column < totalColumns
                && mountainMatrix[row][column] < currentValue);
    }

    /**
     *
     * @param x
     * @param y
     * @param mountainMatrix
     * @param util
     * @return List of Grid having the longest path from x,y( row, column )
     *
     * This uses the DFS technique to find the longest and the largest path
     */
    private List<Grid> getLongestSkiingPathFromGrid(int x, int y, int mountainMatrix[][], int util[][]){

        List<Grid> currentSkiingPath = new ArrayList<>();

        for(int i=0;i<4;i++){
            if(isGridAllowed(x+util[0][i], y+util[1][i],mountainMatrix,mountainMatrix.length, mountainMatrix[x].length,mountainMatrix[x][y])){
                ArrayList<Grid> tempSkiingPath =(ArrayList<Grid>) getLongestSkiingPathFromGrid(x+util[0][i], y+util[1][i], mountainMatrix,util);
                if(tempSkiingPath.size() > currentSkiingPath.size()){
                    // This check passes when we find a longer path than the previous one.
                    currentSkiingPath = tempSkiingPath;
                } else if(tempSkiingPath.size() == currentSkiingPath.size()){
                        // This step is to use the minimum value of the two grids in comparisons
                        currentSkiingPath =
                                mountainMatrix[tempSkiingPath.get(tempSkiingPath.size()-1).getX()][tempSkiingPath.get(tempSkiingPath.size()-1).getY()] <
                                        mountainMatrix[currentSkiingPath.get(currentSkiingPath.size()-1).getX()][currentSkiingPath.get(currentSkiingPath.size()-1).getY()] ?
                                        tempSkiingPath : currentSkiingPath;

                }
            }
        }


        List<Grid> longestSkiingPath = new ArrayList<>();
        longestSkiingPath.add(new Grid(x,y));
        longestSkiingPath.addAll(currentSkiingPath);
        return longestSkiingPath;

    }

    private int[][] parseDataFromFile(String pathToFile) throws IOException,URISyntaxException{

        int mountainMatrix[][];

        URI uri = ClassLoader.getSystemResource(pathToFile).toURI();
        List<String> list = null;
        Stream<String> lines = Files.lines(Paths.get(uri));
        list = lines.collect(Collectors.toList());

        String gridSize[] = list.get(0).split(TEXT_DELIMITER);
        int rows = Integer.valueOf(gridSize[0]);
        int columns = Integer.valueOf(gridSize[1]);

        mountainMatrix = new int[rows][columns];

        Iterator it = list.iterator();
        if(it.hasNext()){
            it.next(); // skipping the grid size line
        }

        int currentRow =0;
        while(it.hasNext()){

            String rowLine = (String) it.next();
            String columnValues[] = rowLine.split(TEXT_DELIMITER);

            int currentColumn = 0;

            for(String column : columnValues){
                    mountainMatrix[currentRow][currentColumn++] = Integer.valueOf(column);
            }
            currentRow++;
        }
        return mountainMatrix;
    }

    private void printPath(List<Grid> list, int mountain[][]){
        for(Grid grid : list){
            System.out.println("X: Y: ("+grid.getX()+","+grid.getY()+"), Value : "+mountain[grid.getX()][grid.getY()]);
        }
    }


    public static void main(String[] args) {

        SkiingInSingapore solution = new SkiingInSingapore();
        solution.solveLongestGrid("map.txt");
        System.out.println("Longest path: " + solution.getLongestPath());
        System.out.println("Largest Drop: " + solution.getLargestDrop());

    }

}
