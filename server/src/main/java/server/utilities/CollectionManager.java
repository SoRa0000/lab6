package server.utilities;

import common.interaction.StudyGroupRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AppServer;
import server.commands.ICommand;
import common.datas.FormOfEducation;
import common.datas.StudyGroup;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import common.exceptions.IncorrectInputScriptException;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import server.customCollection.*;
/**
 * Receiver class
 */

public class CollectionManager {
    private final CommandManager commandManager;
    private final FileManager fileManager;
    protected static CustomTreeMap<String, StudyGroup> studyGroupCollection = new CustomTreeMap<>();

    private static Long lastId = 1L;
    //private final QueryManager queryManager;
    private final List<String> scriptStack = new ArrayList<>();
    private LocalDate initData;
    public final Logger LOG
            = LoggerFactory.getLogger(CollectionManager.class);
    /**
     * Constructor
     * @param commandManager Command manager
     * @param fileManager Fil manager
     * @throws IOException On input error
     */
    public CollectionManager(CommandManager commandManager,FileManager fileManager)throws IOException {
        this.fileManager = fileManager;
        this.commandManager = commandManager;
        loadCollectionFromCsv();
    }

    /**
     * Load collection from CSV file.
     */
    public void loadCollectionFromCsv() throws IOException {
        studyGroupCollection = fileManager.readCollection();
        lastId = fileManager.getLastId();
        initData = LocalDate.now();
    }

    /**
     * @return last ID of Collection
     */
    static public Long getLastId(){
        return lastId;
    }

    /**
     *
     * @param arg key of TreeMap
     */
    public void insert(String arg, StudyGroupRaw studyGroupRaw) {
        try {
            if(!studyGroupCollection.containsKey(arg)){
                studyGroupCollection.put(arg,
                        new StudyGroup(getLastId()+1L,
                                studyGroupRaw.getName(),
                                studyGroupRaw.getCoordinates(),
                                LocalDate.now(),
                                studyGroupRaw.getStudentsCount(),
                                studyGroupRaw.getTransferredStudents(),
                                studyGroupRaw.getFormOfEducation(),
                                studyGroupRaw.getSemesterEnum(),
                                studyGroupRaw.getGroupAdmin())
                        );
                ResponseOutputer.append("Успешно добавлено Study Group!");
                LOG.info("Успешно добавлено Study Group!");
            }
            else {
                ResponseOutputer.append("Ключ не может перекрываться");
                LOG.error("Ключ не может перекрываться");
            }
        }
        catch(IncorrectInputScriptException err){
            throw new IncorrectInputScriptException();
        }
    }

    /**
     * Print collection
     */
    public void show() {
        printCollection(studyGroupCollection);
    }
//
    /**
     * Print all server.commands with description
     */
    public void help() {
        for (Map.Entry<String, ICommand> entry : commandManager.getCommands().entrySet()) {
            String key = entry.getKey();
            ICommand value = entry.getValue();
            ResponseOutputer.append(value.getDescription());
        }
    }

    /**
     * Clear collection
     */
    public void clear(){
        studyGroupCollection.clear();
        ResponseOutputer.append("Успешно очистили коллекцию!");
        LOG.info("Успешно очистили коллекцию!");
    }

    /**
     * Print information of collection
     */
    public void info(){
        ResponseOutputer.append("Тип коллекции: " + studyGroupCollection.getClass().getName());
        ResponseOutputer.append("Дата инициализации: " + initData);
        ResponseOutputer.append("Количество элементов: " + studyGroupCollection.size());
    }

    /**
     * Update collection with key
     * @param arg id of StudyGroup
     */
    public void update(String arg,StudyGroupRaw studyGroupRaw){
        try{
            final Long id = Long.parseLong(arg);
            boolean updated = false;

            for(Map.Entry<String, StudyGroup> studyGroup : studyGroupCollection.entrySet()){
                if(studyGroup.getValue().getId().equals(id)){
                    studyGroupCollection.replace(studyGroup.getKey(),
                            new StudyGroup(
                                    id,
                                    studyGroupRaw.getName(),
                                    studyGroupRaw.getCoordinates(),
                                    LocalDate.now(),
                                    studyGroupRaw.getStudentsCount(),
                                    studyGroupRaw.getTransferredStudents(),
                                    studyGroupRaw.getFormOfEducation(),
                                    studyGroupRaw.getSemesterEnum(),
                                    studyGroupRaw.getGroupAdmin()
                                )
                            );
                    updated = true;
                }
            }
            if(updated) {
                ResponseOutputer.append("Успешно обновлено!");
                LOG.info("Успешно обновлено!");
            }
            else {
                LOG.error("Не удалось обновить. Нет такого id!");
            }

        }
        catch(NumberFormatException err){
            ResponseOutputer.append("id должно быть цифром!");
            LOG.error("id должно быть цифром!");
        }

    }

    /**
     * Remove element of collection with key
     * @param arg key of TreeMap
     */
    public void remove(String arg){
        if(studyGroupCollection.remove(arg) == null) {
            ResponseOutputer.append("Не удалось удалить. Нет такого ключа.");
            LOG.error("Не удалось удалить. Нет такого ключа.");
        }

        else {
            ResponseOutputer.append("Успешно удалено!");
            LOG.info("Успешно удалено!");
        }

    }

    /**
     * Save collection to file
     */
    public void save(){
        fileManager.writeCollection(studyGroupCollection);
    }

    /**
     * Exit program without saving
     */
    public void exit(){
        ResponseOutputer.append("До свидания!");
    }

    /**
     * Execute command from file
     * @param arg script name
     */
    public void executeScript(String arg){
        ResponseOutputer.append("");
        LOG.info("Чтение команды из скрипта...");
    }

    /**
     * Print last 11 command history
     */
    public void history(){
        List<String> commandHistory = commandManager.getCommandHistory();
        for(int i=0;i<commandHistory.size();i++){
            ResponseOutputer.append(i+1+" " + commandHistory.get(i));
        }
    }

    /**
     * Filter by name
     * @param arg Group name
     */
    public void filter_contains_name(String arg){
        try{
            Map<String,StudyGroup> filteredByName = studyGroupCollection.entrySet()
                    .stream()
                    .filter(studyGroup -> studyGroup.getValue().getName().toLowerCase().contains(arg.toLowerCase()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if(!filteredByName.isEmpty()) printCollection(filteredByName);
            else {
                ResponseOutputer.append("Нет таких элементов!");
                LOG.error("Нет таких элементов!");
            }
        }catch(IllegalArgumentException err){
            ResponseOutputer.append("Нет таких элементов!");
            LOG.error("Нет таких элементов!");
        }
    }

    /**
     * Filter by transferred students
     * @param arg number of transferred students
     */
    public void filter_less_than_transferred_students(String arg){
        try{
            Map <String,StudyGroup> filteredByTransferredStudents = studyGroupCollection.entrySet()
                    .stream()
                    .filter(studyGroup -> Long.parseLong(arg) > studyGroup.getValue().getTransferredStudents())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if(!filteredByTransferredStudents.isEmpty()) printCollection(filteredByTransferredStudents);

            else {
                ResponseOutputer.append("Нет таких элементов!");
                LOG.error("Нет таких элементов!");
            }
        }catch(IllegalArgumentException err){
            ResponseOutputer.append("Должно быть цифром");
            LOG.error("Должно быть цифром");
        }
    }

    /**
     * filter by form of education
     * @param arg form of education
     */
    public void filter_greater_than_form_of_education(String arg){
        FormOfEducation formOfEducation = null;
        try{
            formOfEducation = FormOfEducation.valueOf(arg.toUpperCase());
            FormOfEducation finalFormOfEducation = formOfEducation;
            Map<String,StudyGroup> filteredByFormOfEducation = studyGroupCollection.entrySet()
                    .stream()
                    .filter(studyGroup -> finalFormOfEducation.compareTo(studyGroup.getValue().getFormOfEducation()) < 0 )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if(!filteredByFormOfEducation.isEmpty()) printCollection(filteredByFormOfEducation);

            else {
                ResponseOutputer.append("Нет таких элементов!");
                LOG.error("Нет таких элементов!");
            }

        }catch(IllegalArgumentException err){
            ResponseOutputer.append("Нет таких элементов!");
            LOG.error("Нет таких элементов!");
        }
    }

    /**
     * Remove if element is lower
     */
    public void removeLower(StudyGroupRaw studyGroupRaw){
        StudyGroup removeStudyGroup =
                    new StudyGroup(
                            getLastId()+1,
                            studyGroupRaw.getName(),
                            studyGroupRaw.getCoordinates(),
                            LocalDate.now(),
                            studyGroupRaw.getStudentsCount(),
                            studyGroupRaw.getTransferredStudents(),
                            studyGroupRaw.getFormOfEducation(),
                            studyGroupRaw.getSemesterEnum(),
                            studyGroupRaw.getGroupAdmin()
                );
        if(studyGroupCollection.entrySet().removeIf(studyGroup -> studyGroup.getValue().compareTo(removeStudyGroup) < 0))
        {
            ResponseOutputer.append("Успешно удалили из коллекции");
            LOG.info("Успешно удалили из коллекции");
        }
        else {
            ResponseOutputer.append("Ничего не удалили из коллекции");
            LOG.error("Ничего не удалили из коллекции");
        }
    }

    /**
     * replace if element is greater
     * @param arg key of TreeMap
     */
    public void replace_if_greater(String arg,StudyGroupRaw studyGroupRaw){
        if(studyGroupCollection.get(arg) != null){
            StudyGroup replaceStudyGroup =
                    new StudyGroup(
                        getLastId()+1,
                        studyGroupRaw.getName(),
                        studyGroupRaw.getCoordinates(),
                        LocalDate.now(),
                        studyGroupRaw.getStudentsCount(),
                        studyGroupRaw.getTransferredStudents(),
                        studyGroupRaw.getFormOfEducation(),
                        studyGroupRaw.getSemesterEnum(),
                        studyGroupRaw.getGroupAdmin()
            );
            if(studyGroupCollection.get(arg).getStudentsCount().compareTo(replaceStudyGroup.getStudentsCount()) < 0){
                studyGroupCollection.replace(arg,replaceStudyGroup);
                ResponseOutputer.append("Успешно заменили элементы");
                LOG.info("Успешно заменили элементы");
            }
            ResponseOutputer.append("Не заменили элементы");
            LOG.info("Не заменили элементы");
        }
        else {
            ResponseOutputer.append("Не удалось удалить. Нет такого ключа.");
            LOG.error("Не удалось удалить. Нет такого ключа.");
        }

    }

    /**
     * Print collection as tableView;
     * @param collection collection, which will print
     */

    public void printCollection(Map<String, StudyGroup> collection){
        Map<String, StudyGroup> sortedMap = sortValues(collection);
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("id","Key","name","Coord-X", "Coord-Y", "creatDate","St", "T-St","formEdu","SEM","A-Name","passId","CNTR","LOC-X","LOC-Y","LOC-Z","LOC-Name");
        at.addRule();
        sortedMap.forEach((key,entry)-> {
            at.addRow(
                    String.valueOf(entry.getId()),
                    String.valueOf(key),
                    String.valueOf(entry.getName()),
                    String.valueOf(entry.getCoordinates().getX()),
                    String.valueOf(entry.getCoordinates().getY()),
                    String.valueOf(entry.getCreationDate()),
                    String.valueOf(entry.getStudentsCount()),
                    String.valueOf(entry.getTransferredStudents()),
                    String.valueOf(entry.getFormOfEducation()),
                    String.valueOf(entry.getSemesterEnum()),
                    String.valueOf(entry.getGroupAdmin().getName()),
                    String.valueOf(entry.getGroupAdmin().getPassportID()),
                    String.valueOf(entry.getGroupAdmin().getCountry()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getX()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getY()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getZ()),
                    String.valueOf(entry.getGroupAdmin().getLocation().getName())
            );
        });
        at.addRule();
        at.getContext().setWidth(200);
        CWC_LongestLine cwc = new CWC_LongestLine();
        at.getRenderer().setCWC(cwc);
        String render = at.render();
        ResponseOutputer.append(render);
    }
    public static <K, V extends Comparable<V>> Map<K, V> sortValues(final Map<K, V> m)
    {
        Comparator<K> com = new Comparator<K>()
        {
            public int compare(K k1, K k2)
            {
                int compare = m.get(k1).compareTo(m.get(k2));
                if(compare == 0)
                {
                    return 1;
                }
                else
                {
                    return compare;
                }
            }
        };Map<K, V> sortedByValues = new TreeMap<K, V>(com);
        sortedByValues.putAll(m);
        return sortedByValues;
    }

}