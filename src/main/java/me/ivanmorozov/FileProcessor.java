package me.ivanmorozov;


import com.opencsv.CSVWriter;

import me.ivanmorozov.extarctAndWrite.FileExtractInfo;
import me.ivanmorozov.extarctAndWrite.HtmlExtract;
import me.ivanmorozov.extarctAndWrite.PdfExtractor;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {

    private static final Map<String, FileExtractInfo> strategies = new HashMap<>();

    static
    {
        strategies.put("html", new HtmlExtract());
        strategies.put("pdf", new PdfExtractor());
    }


    private static final String pdfFolderPath = "C:\\Users\\LORD\\Downloads";
    private static final String outputCsvPath = "C:\\Users\\LORD\\Desktop\\result\\result.csv";


    public static void main(String[] args) {


        Path path1 = Path.of(outputCsvPath);
        try (Stream<Path> pathStream = Files.walk(Path.of(pdfFolderPath));) {



            Set<Path> pdfFiles = pathStream
                    .filter(path -> {
                        String name = path.toString().toLowerCase();
                        return name.endsWith(".pdf") || name.endsWith(".html");
                    })
                    .filter(path -> path.getFileName().toString().contains("ЦО"))
                    .collect(Collectors.toSet());

            for (Path filePath : pdfFiles) {
                System.out.println("Обработка файла: " + filePath);
                String extension = getExtension(filePath);
                FileExtractInfo strategy = strategies.get(extension.toLowerCase());
                try {
                    strategy.extract(filePath.toFile(), path1);

                } catch (Exception e) {
                    System.err.println("Не удалось разобрать PDF: " + filePath.getFileName());
                    e.printStackTrace();
                }
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getExtension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot == -1 ? "" : name.substring(dot + 1).toLowerCase();
    }

}
