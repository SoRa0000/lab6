package common.interaction;

import common.datas.*;

import java.io.Serializable;

public class StudyGroupRaw implements Serializable {
    private static final long serialVersionUID = 3L;
    private final String name;
    private final Coordinates coordinates;
    private final Long studentsCount;
    private final long transferredStudents;
    private final FormOfEducation formOfEducation;
    private final Semester semesterEnum;
    private final Person groupAdmin;

    public StudyGroupRaw(String name, Coordinates coordinates, Long studentsCount, long transferredStudents, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this.name = name;
        this.coordinates = coordinates;
        this.studentsCount = studentsCount;
        this.transferredStudents = transferredStudents;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }

    /**
     * @return Name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the group
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Students count of the group
     */
    public Long getStudentsCount() {
        return studentsCount;
    }

    /**
     * @return Transferred students count of the group
     */
    public long getTransferredStudents() {
        return transferredStudents;
    }

    /**
     * @return Form of education of the group
     */
    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    /**
     * @return Semester of the group
     */
    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    /**
     * @return Admin of the group
     */
    public Person getGroupAdmin() {
        return groupAdmin;
    }




    @Override
    public int hashCode() {

        int result = 31 + (name != null ? name.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        result = 31 * result + (studentsCount != null ? studentsCount.hashCode() : 0);
        result = 31 * result + (int) (transferredStudents ^ (transferredStudents >>> 32));
        result = 31 * result + (formOfEducation != null ? formOfEducation.hashCode() : 0);
        result = 31 * result + (semesterEnum != null ? semesterEnum.hashCode() : 0);
        result = 31 * result + (groupAdmin != null ? groupAdmin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return
                getName() + "," +
                getCoordinates().getX() + "," +
                getCoordinates().getY() + "," +
                getStudentsCount() + "," +
                getTransferredStudents() + "," +
                getFormOfEducation() + "," +
                getSemesterEnum() + "," +
                getGroupAdmin().getName() + "," +
                getGroupAdmin().getPassportID() + "," +
                getGroupAdmin().getCountry() + "," +
                getGroupAdmin().getLocation().getX() + "," +
                getGroupAdmin().getLocation().getY() + "," +
                getGroupAdmin().getLocation().getZ() + "," +
                getGroupAdmin().getLocation().getName();

    }

}
