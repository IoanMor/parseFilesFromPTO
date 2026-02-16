package me.ivanmorozov.extarctAndWrite;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class PdfExtractor implements FileExtractInfo, FileWriteCSV {

    @Override
    public void extract(File pdfFile, Path toPath) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile);
             ObjectExtractor extractor = new ObjectExtractor(document);) {
            PageIterator pages = extractor.extract();

            while (pages.hasNext()) {
                Page page = pages.next();

                // Попробовать таблицы
                SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
                List<Table> tables = sea.extract(page);

                for (Table table : tables) {
                    List<List<RectangularTextContainer>> rows = table.getRows();

                    for (List<RectangularTextContainer> row : rows) {
                        if (row.size() > 0 && row.get(0).getText().trim().startsWith("Итого:")) {

                            // Предполагаем фиксированное положение колонок
                            String gPod = row.get(2).getText().trim();
                            String tNar = row.get(row.size() - 1).getText().trim();
                            write(toPath,pdfFile,gPod,tNar);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void write(Path toPath, File pdfFile, String gPod, String tNar) {
        FileWriteCSV.super.write(toPath, pdfFile, gPod, tNar);
    }
}