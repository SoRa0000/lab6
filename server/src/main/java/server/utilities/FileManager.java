package server.utilities;
import java.io.*;
import java.time.LocalDate;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import common.exceptions.CannotReadFileException;
import common.exceptions.CannotWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AppServer;
import server.customCollection.CustomTreeMap;
import common.datas.*;

/**
 * File manager, saving and loading collection from file.
 */
public class FileManager {
    private final String path;
    private long lastId = 1L;
    public final Logger LOG
            = LoggerFactory.getLogger(FileManager.class);
    /**
     * Constructor
     * @param path Path of CSV file
     * @throws FileNotFoundException If file not found
     */
    public FileManager(String path) throws FileNotFoundException, CannotReadFileException {
        this.path = path;
        try{
            File file = new File(path);
            if(!file.exists()) throw new FileNotFoundException();
            if(!file.canRead()) throw new CannotReadFileException();
        }catch (FileNotFoundException err){
            LOG.error("Файл не найден");
            System.exit(0);
        }catch (CannotReadFileException err){
            LOG.error("Отказано в доступе для чтения из файла!");
            System.exit(0);
        }

    }

    /**
     * Read collection from CSV file
     * @return collection TreeMap
     */
    public CustomTreeMap<String, StudyGroup> readCollection() throws IOException {
        CustomTreeMap<String, StudyGroup> studyGroupCollection = new CustomTreeMap<>();
        String[] data;
        int row = 0;
        try {
            InputStream inputStream = new FileInputStream(path);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            while ((data = reader.readNext()) != null) {
                row++;
                try{
                    if(data.length == 16) throw new IndexOutOfBoundsException();

                    long id = Long.parseLong(data[0]);
                    String key = data[1];
                    String name = data[2];
                    int coordinate_x = Integer.parseInt(data[3]);
                    long coordinate_y = Long.parseLong(data[4]);
                    LocalDate localDate = LocalDate.parse(data[5]);
                    Long students_count = Long.parseLong(data[6]);
                    long transferred_students = Long.parseLong(data[7]);
                    FormOfEducation formOfEducation = FormOfEducation.valueOf(data[8]);
                    Semester semester = Semester.valueOf(data[9]);
                    String admin_name = data[10];
                    String passport_id = data[11];
                    Country country = Country.valueOf(data[12]);
                    Float location_x = Float.parseFloat(data[13]);
                    Long location_y = Long.parseLong(data[14]);
                    int location_z = Integer.parseInt(data[15]);
                    String location_name = data[16];

                    if (id > lastId) {
                        lastId = id;
                    }

                    Coordinates coordinates = new Coordinates(coordinate_x, coordinate_y);
                    Location location = new Location(location_x, location_y, location_z, location_name);
                    Person groupAdmin = new Person(admin_name, passport_id, country, location);
                    StudyGroup studyGroup = new StudyGroup(
                            id,
                            name,
                            coordinates,
                            localDate,
                            students_count,
                            transferred_students,
                            formOfEducation,
                            semester,
                            groupAdmin
                    );
                    studyGroupCollection.put(key, studyGroup);

                }catch(IndexOutOfBoundsException | IllegalArgumentException err){
                    LOG.error("Ошибка в " + row + " строке!");
                }
            }
            LOG.info("Данные добавлены в коллекцию!");
            reader.close();
        } catch (IOException err){
            LOG.error("Ошибка во время чтения из файла");

        }

        return studyGroupCollection;
    }

    /**
     * Write collection to CSV file
     * @param studyGroupCollection collection TreeMap
     */
    public void writeCollection(CustomTreeMap<String,StudyGroup> studyGroupCollection) {
        try {
            File file = new File(path);
            if(!file.canWrite()) throw new CannotWriteException();
            OutputStream outputStream = new FileOutputStream(path);
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream), ',', '\0');
            studyGroupCollection.forEach((key, value) -> {
                String[] data = new String[17];
                data[0] = value.getId().toString();
                data[1] = key;
                String[] values = value.toString().split(",");
                System.arraycopy(values, 1, data, 2, values.length-1);
                writer.writeNext(data);

            });
            writer.close();
            LOG.info("Успешно сохранено в файл!");
        }catch (IOException | CannotWriteException err){
            LOG.error("Отказано в доступе для записи в файл!");
        }
    }
    public Long getLastId(){
        return lastId;
    }
}
