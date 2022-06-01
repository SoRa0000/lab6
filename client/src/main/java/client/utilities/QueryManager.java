package client.utilities;

import common.datas.*;
import common.exceptions.IncorrectInputScriptException;
import common.interaction.StudyGroupRaw;

import java.time.LocalDate;
import java.util.Scanner;

public class QueryManager {

    public boolean scriptMode = false;
    private Scanner queryReader = new Scanner(System.in);
    private QueryChecker queryChecker = new QueryChecker(queryReader);
    private QueryEnumsChecker queryEnumsChecker = new QueryEnumsChecker(queryReader);

    public QueryManager(Scanner queryReader) {
        this.queryReader = queryReader;
    }

    /**
     * @return scanner
     */
    public Scanner getScanner() {
        return queryReader;
    }

    /**
     * Set scanner to queryChecker and queryEnumsChecker
     * @param scanner scanner
     */
    public void setScanner(Scanner scanner) {
        this.queryReader = scanner;
        queryChecker.setScanner(this.queryReader);
        queryEnumsChecker.setScanner(this.queryReader);
    }

    /**
     * Set scriptMode to queryChecker  and queryChecker
     * @param scriptMode scriptMode
     */
    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
        queryChecker.setScriptMode(scriptMode);
        queryEnumsChecker.setScriptMode(scriptMode);
    }

    /**
     * Create new study group
     * @return StudyGroup
     */
    public StudyGroupRaw createQueryOfStudyGroup() {
        String name;
        Coordinates coordinates;
        Long studentsCount;
        long transferredStudents;
        FormOfEducation formOfEducation;
        Semester semesterEnum;
        Person groupAdmin;
        try {
            name = queryChecker.stringChecker("Введите имя группы: ", false);
            coordinates = createQueryOfCoordinates();
            studentsCount = queryChecker.longChecker("Введите колличество студентов: ", 0,true);
            transferredStudents = queryChecker.longChecker("Введите количество переведенных студентов: ", 0,false);
            formOfEducation = queryEnumsChecker.formOfEducationChecker("Введите форму обучения: ");
            semesterEnum = queryEnumsChecker.semesterChecker("Введите семестер: ",true);
            groupAdmin = createQueryOfPerson();
            return new StudyGroupRaw(name,
                    coordinates,
                    studentsCount,
                    transferredStudents,
                    formOfEducation,
                    semesterEnum,
                    groupAdmin);
        } catch (IncorrectInputScriptException err) {
            throw new IncorrectInputScriptException();
        }
    }

    /**
     * Create new Coordinates
     * @return Coordinates
     */
    private Coordinates createQueryOfCoordinates() {
        int x;
        long y;
        try {
            x = queryChecker.intChecker("Введите координат X: ");
            y = queryChecker.longChecker("Введите координат Y: ");
            return new Coordinates(x, y);
        } catch (IncorrectInputScriptException err) {
            throw new IncorrectInputScriptException();
        }
    }

    /**
     * Create new Person(admin)
     * @return Person
     */
    private Person createQueryOfPerson() {
        String name;
        String passportID;
        Country country;
        Location location;
        try {
            name = queryChecker.stringChecker("Введите имя админа группы: ", false);
            passportID = queryChecker.passportIdChecker("Введите номер паспорта админа группы: ",true);
            country = queryEnumsChecker.countryChecker("Введите страну админа группы: ");
            location = createQueryOfLocation();
            return new Person(name, passportID, country, location);
        } catch (IncorrectInputScriptException err) {
            throw new IncorrectInputScriptException();
        }
    }

    /**
     * Create new Location
     * @return Location
     */
    private Location createQueryOfLocation() {
        Float x;
        Long y;
        int z;
        String name;
        try {
            x = queryChecker.floatChecker("Введите местоположение X: ");
            y = queryChecker.longChecker("Введите местоположение Y: ");
            z = queryChecker.intChecker("Введите местоположение Z: ");
            name = queryChecker.stringChecker("Введите имя местоположения: ", true);
            return new Location(x, y, z, name);
        } catch (IncorrectInputScriptException err) {
            throw new IncorrectInputScriptException();
        }
    }

}
