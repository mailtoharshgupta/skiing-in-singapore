/**
 * @author Harsh Gupta on {1/27/18}
 */
public class Grid {

    private int x;
    private int y;

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Grid() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grid grid = (Grid) o;

        if (x != grid.x) return false;
        return y == grid.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
