package common.datas;


import java.io.Serializable;
import java.util.Objects;

/**
 * Admin of the group
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 4L;
    private final String name;
    private final String passportID;
    private final Country country;
    private final Location location;

    public Person(String name, String passportID, Country country, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.country = country;
        this.location = location;
    }

    /**
     * @return Name of admin
     */
    public String getName() {
        return name;
    }

    /**
     * @return Passport ID of admin
     */
    public String getPassportID() {
        return passportID;
    }

    /**
     * @return Country of admin
     */
    public Country getCountry() {
        return country;
    }

    /**
     * @return Location of admin
     */
    public Location getLocation() {
        return location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!Objects.equals(name, person.name)) return false;
        if (!Objects.equals(passportID, person.passportID)) return false;
        if (country != person.country) return false;
        return Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (passportID != null ? passportID.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", passportID='" + passportID + '\'' +
                ", nationality=" + country +
                ", location=" + location +
                '}';
    }
}
