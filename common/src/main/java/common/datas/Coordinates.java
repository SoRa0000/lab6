package common.datas;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 5L;
    private final int x;
    private final long y;

    public Coordinates(int x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X of the coordinates
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y of the coordinates
     */
    public long getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + (int) y;
        return result;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
