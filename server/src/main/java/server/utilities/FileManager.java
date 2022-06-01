package server.utilities;
import java.io.*;
import java.time.LocalDate;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import common.exceptions.CannotReadFileException;
import common.exceptions.CannotWriteException;
import server.AppServer;
import server.customCollection.CustomTreeMap;
import common.datas.*;

/**
 * File manager, saving and loading collection from file.
 */
public class FileManager {
    private final String path;
    private long lastId = 1L;

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
            AppServer.logger.info("Файл не найден");
            System.exit(0);
        }catch (CannotReadFileException err){
            AppServer.logger.info("Отказано в доступе для чтения из файла!");
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
                    if (Long.parseLong(data[1]) > lastId) {
                        lastId = Long.parseLong(data[1]);
                    }
                    Coordinates coordinates = new Coordinates(Integer.parseInt(data[3]), Long.parseLong(data[4]));
                    Location location = new Location(Float.parseFloat(data[13]), Long.parseLong(data[14]), Integer.parseInt(data[15]), data[16]);
                    Person groupAdmin = new Person(data[10], data[11], Country.valueOf(data[12]), location);
                    StudyGroup studyGroup = new StudyGroup(
                            Long.parseLong(data[1]),
                            data[2],
                            coordinates,
                            LocalDate.parse(data[5]),
                            Long.parseLong(data[6]),
                            Long.parseLong(data[7]),
                            FormOfEducation.valueOf(data[8]),
                            Semester.valueOf(data[9]),
                            groupAdmin
                    );
                    studyGroupCollection.put(data[0], studyGroup);
                }catch(IndexOutOfBoundsException | IllegalArgumentException err){
                    AppServer.logger.info("Ошибка в " + row + " строке!");
                }
            }
            AppServer.logger.info("Данные добавлены в коллекцию!");
            reader.close();
        } catch (IOException err){
            AppServer.logger.info("Ошибка во время чтения из файла");

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
                data[0] = key;
                String[] values = value.toString().split(",");
                System.arraycopy(values, 0, data, 1, values.length);
                writer.writeNext(data);

            });
            writer.close();
            AppServer.logger.info("Успешно сохранено в файл!");
        }catch (IOException | CannotWriteException err){
            AppServer.logger.info("Отказано в доступе для записи в файл!");
        }
    }
    public Long getLastId(){
        return lastId;
    }
}
