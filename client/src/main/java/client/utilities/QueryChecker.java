package client.utilities;

import common.exceptions.EmptyStringException;
import common.exceptions.IncorrectInputScriptException;
import common.exceptions.IncorrectPassportID;
import common.exceptions.NotInDeclaredLimitsException;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Check all queries requirements
 */

public class QueryChecker {
    private int MAX_ID_LENGTH = 27;
    private boolean scriptMode = false;
    private Scanner scanner;

    public QueryChecker(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * change mode
     * @param scriptMode boolean script Mode
     */
    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    /**
     * Set scanner, also can change scanner type
     * @param scanner New scanner
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     *
     * @param promptMessage Message, which will be displayed
     * @param beEmpty Express of empty
     * @return If String meets all requirements return String
     */
    public String stringChecker(String promptMessage, boolean beEmpty) {
        String value = "";
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                value = scanner.nextLine().trim();
                if (!beEmpty && value.isEmpty()) throw new EmptyStringException("Ввод не может быть пустым");
                repeat = false;
            } catch (EmptyStringException err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return value;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return String
     */
    public String passportIdChecker(String promptMessage, boolean beEmpty) {
        String value = null;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                value = scanner.nextLine().trim();
                if(beEmpty && value.isEmpty()){
                    return null;
                }

                if (value.length() > MAX_ID_LENGTH)
                    throw new IncorrectPassportID("Длина строки не должна быть больше 27!");
//                else {
//                    String finalValue = value;
//                    CollectionManager.studyGroupCollection.forEach((k, v) -> {
//                        if (finalValue.equals(v.getGroupAdmin().getPassportID()))
//                            throw new IncorrectPassportID("Значение номер паспорта должно быть уникальным!");
//                    });
//                }
                repeat = false;
            } catch (IncorrectPassportID | EmptyStringException err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }

        }
        return value;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return Integer
     */
    public int intChecker(String promptMessage) {
        int value = 0;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                value = Integer.parseInt(scanner.nextLine().trim());
                repeat = false;
            } catch (NumberFormatException err) {
                System.out.println("Должны вводить число!");
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
        return value;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return Long
     */
    public long longChecker(String promptMessage) {
        long value = 0;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                value = Long.parseLong(scanner.nextLine().trim());
                repeat = false;
            } catch (NumberFormatException err) {
                System.out.println("Должны вводить число!");
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
        return value;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @param min Express of minimum
     * @return If String meets all requirements return Long
     */
    public Long longChecker(String promptMessage, int min,boolean beEmpty) {
        Long value = 0L;
        String valueString;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                valueString = scanner.nextLine().trim();
                if(!beEmpty && valueString.isEmpty()){
                    throw new EmptyStringException("Ввод не может быть пустым");
                }
                if(beEmpty && valueString.isEmpty()){
                    return null;
                }
                value = Long.parseLong(valueString);
                if (value <= min) throw new NotInDeclaredLimitsException("Значение поля должно быть больше 0");
                repeat = false;
            } catch (NumberFormatException err) {
                System.out.println("Должны вводить число!");
                if (scriptMode) throw new IncorrectInputScriptException();
            } catch (NotInDeclaredLimitsException | EmptyStringException  err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return value;
    }

    /**
     * @param promptMessage Message, which will be displayed
     * @return If String meets all requirements return Float
     */
    public Float floatChecker(String promptMessage) {
        Float value = null;
        boolean repeat = true;
        while (repeat) {
            try {
                if (!scriptMode) {
                    System.out.println(promptMessage);
                    System.out.print("> ");
                }
                value = Float.parseFloat(scanner.nextLine().trim());
                repeat = false;
            } catch (NumberFormatException err) {
                System.out.println("Должны вводить число!");
                if (scriptMode) throw new IncorrectInputScriptException();
            } catch (NotInDeclaredLimitsException | EmptyStringException err) {
                System.out.println(err.getMessage());
                if (scriptMode) throw new IncorrectInputScriptException();
            }
            catch (NoSuchElementException err) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return value;
    }
}