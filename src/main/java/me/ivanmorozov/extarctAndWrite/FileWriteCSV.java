package me.ivanmorozov.extarctAndWrite;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
                StandardOpenOption.APPEND)) {
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
                csvWriter.writeNext(new String[]{"Имя файла", "Gпод", "Тнар", "Расход мгн. т/ч"});
            }
            String cleanGPod = gPod.trim().replace(',', '.').replaceAll("\\s+", "");
            String cleanTNar = tNar.trim().replace(',', '.').replaceAll("\\s+", "");
            BigDecimal res = new BigDecimal(cleanGPod).divide(new BigDecimal(cleanTNar), 2, RoundingMode.HALF_UP);
            String resStr = res.toString().replace(".",",");

            csvWriter.writeNext(new String[]{address, gPod, tNar, resStr});
            csvWriter.flush();

            System.out.println("Добавлено: " + address
                    + " | " + gPod + " | " + tNar);

        } catch (Exception e) {
            System.err.println("Ошибка при записи в CSV: " + e.getMessage());
        }
    }
}

