package client.utilities;

import common.datas.Country;
import common.datas.FormOfEducation;
import common.datas.Semester;
import common.exceptions.EmptyStringException;
import common.exceptions.IncorrectInputScriptException;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Check all enums requirements
 */
public class QueryEnumsChecker {
    private boolean scriptMode = false;
    private Scanner scanner;

    public QueryEnumsChecker(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * change mode
     *
     * @param scriptMode boolean script Mode
     */
    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    /**
     * Set scanner, also can change scanner type
     *
     * @param scanner New scanner
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return Semester
     */
    public Semester semesterChecker(String promptMessage,boolean beEmpty) {
        Semester semester = null;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println("Список семестера: " + Arrays.asList(Semester.values()));
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                String readString = scanner.nextLine().trim().toUpperCase();
                if(beEmpty && readString.isEmpty()){
                    return null;
                }
                semester = Semester.valueOf(readString);
                repeat = false;
            } catch (IllegalArgumentException err) {
                System.out.println("Неверный вывод: Semester!");
                if (scriptMode) throw new IncorrectInputScriptException();
            } catch (EmptyStringException err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return semester;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return FormOfEducation
     */
    public FormOfEducation formOfEducationChecker(String promptMessage) {
        FormOfEducation formOfEducation = null;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println("Список семестера: " + Arrays.asList(FormOfEducation.values()));
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                String readString = scanner.nextLine().trim().toUpperCase();
                formOfEducation = FormOfEducation.valueOf(readString);
                repeat = false;
            } catch (IllegalArgumentException err) {
                System.out.println("Неверный вывод: FormOfEducation!");
                if (scriptMode) throw new IncorrectInputScriptException();
            } catch (EmptyStringException err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return formOfEducation;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return Country
     */
    public Country countryChecker(String promptMessage) {
        Country country = null;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println("Список семестера: " + Arrays.asList(Country.values()));
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                String readString = scanner.nextLine().trim().toUpperCase();
                country = Country.valueOf(readString);
                repeat = false;
            } catch (IllegalArgumentException err) {
                System.out.println("Неверный вывод: Country!");
                if (scriptMode) throw new IncorrectInputScriptException();
            } catch (EmptyStringException  err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return country;
    }
}
