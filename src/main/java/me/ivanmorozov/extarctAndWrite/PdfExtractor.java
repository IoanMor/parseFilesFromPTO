package me.ivanmorozov.extarctAndWrite;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.ivanmorozov.util.UtilMethods.getAddress;

@Component("pdf")
public class PdfExtractor implements FileExtractInfo, FileWriteCSV {

    @Override
    public void extract(File file, Path toPath) throws IOException {
        boolean found = false;

        try (PDDocument document = PDDocument.load(file); ObjectExtractor extractor = new ObjectExtractor(document)) {
            PageIterator pages = extractor.extract();

            while (pages.hasNext() && !found) {
                Page page = pages.next();

                BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
                List<Table> tables = bea.extract(page);

                for (Table table : tables) {
                    List<List<RectangularTextContainer>> rows = table.getRows();

                    for (List<RectangularTextContainer> row : rows) {
                        StringBuilder lineBuilder = new StringBuilder();
                        for (RectangularTextContainer cell : row) {
                            lineBuilder.append(cell.getText()).append(" ");
                        }
                        String line = lineBuilder.toString().trim();
                        Pattern itogoPattern = Pattern.compile("Итого[:\\s│║]*.*");
                        Matcher itogoMatcher = itogoPattern.matcher(line);
                        if (line.startsWith("Итого") || line.startsWith("Итого:") || itogoMatcher.find()) {

                            Matcher matcher = Pattern.compile("\\d+\\.\\d+").matcher(line);
                            List<String> numbers = new ArrayList<>();
                            while (matcher.find()) {
                                numbers.add(matcher.group());
                            }

                            if (numbers.size() >= 2) {
                                String gPod = numbers.get(1).replace(".",","); // указатель
                                String tNar = numbers.get(numbers.size() - 1).replace(".",",");// указатель

                                write(toPath, getAddress(file.getName()), gPod, tNar);
                                found = true;
                                System.out.println("Найдено в Tabula: " + file.getName() + " -> Gпод: " + gPod + ", Тнар: " + tNar);
                                break;
                            }
                        }
                    }
                    if (found) break;
                }
            }

            if (!found) {
                System.out.println("Tabula не нашла Итого, используем PDFTextStripper для " + file.getName());

                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");
                String line = null;

                for (String l : lines) {
                    if (l.startsWith("Итого") || l.startsWith("Итого:")) {
                        line = l;
                        break;
                    }
                }

                if (line != null) {
                    Matcher matcher = Pattern.compile("\\d+\\.\\d+").matcher(line);
                    List<String> numbers = new ArrayList<>();
                    while (matcher.find()) {
                        numbers.add(matcher.group());
                    }

                    if (numbers.size() >= 2) {
                        String gPod = numbers.get(numbers.size() - 2);// указатель
                        String tNar = numbers.get(numbers.size() - 1);// указатель

                        write(toPath,getAddress(file.getName()) , gPod, tNar);
                        System.out.println("Найдено в PDFTextStripper: " + file.getName() + " -> Gпод: " + gPod + ", Тнар: " + tNar);
                    } else {
                        System.err.println("Не удалось найти нужные числа в файле: " + file.getName());
                    }
                } else {
                    System.err.println("Строка 'Итого' не найдена в файле: " + file.getName());
                }
            }
        }
    }


    @Override
    public void write(Path toPath, String address, String gPod, String tNar) {
        FileWriteCSV.super.write(toPath, address, gPod, tNar);
    }
}