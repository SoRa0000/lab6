package common.datas;

import java.io.Serializable;
import java.util.Objects;

/**
 * Location of the group admin
 */
public class Location implements Serializable {
    private static final long serialVersionUID = 6L;
    private final Float x;
    private final Long y;
    private final int z;
    private final String name;

    public Location(Float x, Long y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    /**
     * @return X of location
     */
    public Float getX() {
        return x;
    }

    /**
     * @return Y of location
     */
    public Long getY() {
        return y;
    }

    /**
     * @return Z of location
     */
    public int getZ() {
        return z;
    }
    /**
     * @return name of location
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (z != location.z) return false;
        if (!Objects.equals(x, location.x)) return false;
        if (!Objects.equals(y, location.y)) return false;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + z;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }
}
