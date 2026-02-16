package me.ivanmorozov.extarctAndWrite;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileWriteCSV {
        default void write(Path toPath, File pdfFile,String gPod, String tNar) {
            try(Writer writer = Files.newBufferedWriter(
                    Path.of(toPath.toUri()), StandardCharsets.UTF_8);)
            {
                writer.write('\uFEFF');
                CSVWriter csvWriter = new CSVWriter(
                        writer,
                        ';',
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END
                );
                csvWriter.writeNext(
                        new String[]{pdfFile.getName(), gPod, tNar}
                );
                System.out.println("Добавлено: " + pdfFile.getName()
                        + " | " + gPod + " | " + tNar);
                csvWriter.close();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

