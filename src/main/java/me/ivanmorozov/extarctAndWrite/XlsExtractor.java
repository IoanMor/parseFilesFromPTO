package me.ivanmorozov.extarctAndWrite;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

@Component("xls")
public class XlsExtractor implements FileExtractInfo, FileWriteCSV{
    @Override
    public void extract(File file, Path toPath) throws IOException {
        String gPod = "";
        String tNar = "";
        try (InputStream is = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            Iterator<Row> rowIterator = sheet.rowIterator();
            int countItogo = 0;
            while (rowIterator.hasNext()) {
                Row row =  rowIterator.next();
                String marker = getCellString(row.getCell(0), evaluator);
                String gPodValue = getCellString(row.getCell(2), evaluator); // укахатель
                String tNarValue = getCellString(row.getCell(11), evaluator); // указатель
                if (marker!=null && marker.trim().startsWith("Итого")){
                    countItogo++;
                }
                if (countItogo==6){
                    gPod = gPodValue;
                    tNar = tNarValue;
                    break;
                }
            }
            System.out.println(gPod);
            System.out.println(tNar);
            write(toPath,file.getName(),gPod.replace(".",","),tNar.replace(".",","));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String getCellString(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.FORMULA) {
            CellValue value = evaluator.evaluate(cell);
            if (value.getCellType() == CellType.STRING) {
                return value.getStringValue().trim();
            }
            return value.formatAsString().trim();
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        return null;
    }
    @Override
    public void write(Path toPath, String address, String gPod, String tNar) {
        FileWriteCSV.super.write(toPath, address, gPod, tNar);
    }
}
