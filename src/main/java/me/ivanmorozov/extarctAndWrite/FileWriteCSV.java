package me.ivanmorozov.extarctAndWrite;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public interface FileWriteCSV {
    default void write(Path toPath, String address, String gPod, String tNar) {

        try (Writer writer = Files.newBufferedWriter(
                toPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND))
        {
            CSVWriter csvWriter = new CSVWriter(
                    writer,
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );
            boolean needHeader = !Files.exists(toPath) || Files.size(toPath) == 0;
            if (needHeader) {
                writer.write('\uFEFF');
                csvWriter.writeNext(new String[]{"Имя файла", "Gпод", "Тнар"});
            }
            csvWriter.writeNext(new String[]{address, gPod, tNar});
            csvWriter.flush();

            System.out.println("Добавлено: " + address
                    + " | " + gPod + " | " + tNar);

        } catch (Exception e) {
            System.err.println("Ошибка при записи в CSV: " + e.getMessage());
        }
    }
}

