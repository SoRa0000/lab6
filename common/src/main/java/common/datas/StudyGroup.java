package common.datas;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Object, which is stored in the collection
 */
public class StudyGroup implements Comparable<StudyGroup> {
    private final Long id;
    private final String name;
    private final Coordinates coordinates;
    private final java.time.LocalDate creationDate;
    private final Long studentsCount;
    private final long transferredStudents;
    private final FormOfEducation formOfEducation;
    private final Semester semesterEnum;
    private final Person groupAdmin;

    public StudyGroup(Long id, String name, Coordinates coordinates, LocalDate creationDate, Long studentsCount, long transferredStudents, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.transferredStudents = transferredStudents;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }

    /**
     * @return ID of the group
     */
    public Long getId() {
        return id;
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
     * @return Creation date of the group
     */
    public LocalDate getCreationDate() {
        return creationDate;
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
    public int compareTo(StudyGroup studyGroup) {
        return name.compareTo(studyGroup.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyGroup that = (StudyGroup) o;

        if (transferredStudents != that.transferredStudents) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(coordinates, that.coordinates)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(studentsCount, that.studentsCount)) return false;
        if (formOfEducation != that.formOfEducation) return false;
        if (semesterEnum != that.semesterEnum) return false;
        return Objects.equals(groupAdmin, that.groupAdmin);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (studentsCount != null ? studentsCount.hashCode() : 0);
        result = 31 * result + (int) (transferredStudents ^ (transferredStudents >>> 32));
        result = 31 * result + (formOfEducation != null ? formOfEducation.hashCode() : 0);
        result = 31 * result + (semesterEnum != null ? semesterEnum.hashCode() : 0);
        result = 31 * result + (groupAdmin != null ? groupAdmin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getId() + "," +
                getName() + "," +
                getCoordinates().getX() + "," +
                getCoordinates().getY() + "," +
                getCreationDate() + "," +
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