package com.example.allesflauschig.utils;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.MOOD_FILE_HEADERS;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CsvUtils {

    static Logger LOG = Logger.getLogger(CsvUtils.class.getName());


    public static void addEntry(String directory, String fileName, String[] values) {
        File file = createFileWithHeaders(directory, fileName, MOOD_FILE_HEADERS);
        appendToFile(file, values);
    }

    public static List<String[]> readFromFile(String directory, String fileName) {
        List<String[]> result = new ArrayList<>();
        File dir = new File(directory);
        try {
            CSVReader reader = new CSVReader(new FileReader(new File(dir, fileName)));
            reader.iterator().forEachRemaining(line -> {
                LOG.info(Arrays.toString(line));
                result.add(line);
            });
            reader.close();
            return result;
        } catch (Exception e) {
            LOG.severe(String.format("An error occured when trying to read from file '%s' in directory '%s': %s", fileName, directory, e.getMessage()));
            LOG.severe(Arrays.toString(e.getStackTrace()));
            return Collections.emptyList();
        }
    }

    private static void appendToFile(File file, String[] values) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
            List<String[]> data = Collections.singletonList(values);
            writer.writeAll(data);

            //writer.close(); --> not needed. This will be done be the try-with-resources

            LOG.info("Content written to file " + file.getAbsolutePath());
        } catch (IOException e){
            LOG.severe(String.format("Error occured when trying to add file content: %s", e.getMessage()));
            e.printStackTrace();
        }
    }

    private static File createFileWithHeaders(String directory, String fileName, String[] headers) {
        try {
            File dir = new File(directory);
            if (dir.mkdirs()) {
                LOG.info(String.format("Created directory '%s'", directory));
            }

            File file = new File(dir, fileName);
            if (file.createNewFile()) {
                LOG.info(String.format("Created file '%s' in directory '%s'.", fileName, directory));
                appendToFile(file, headers);
            }

            return file;
        } catch (IOException e) {
            LOG.severe("Error occurred when trying to create file!");
            LOG.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
