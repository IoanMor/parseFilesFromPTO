package me.ivanmorozov.extarctAndWrite;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static me.ivanmorozov.util.UtilMethods.getAddress;

@Component("html")
public class HtmlExtract implements FileExtractInfo, FileWriteCSV {


    @Override
    public void extract(File file, Path toPath) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        Element scriptElement = document.selectFirst("script#AVProtocol");

        if (scriptElement == null) {
            System.err.println("Скрипт с AVProtocol не найден");
            return;
        }
        String xmlContent = scriptElement.html().trim();
        Document xmlDoc = Jsoup.parse(xmlContent, "", Parser.xmlParser());

        Element recordsSum = xmlDoc.selectFirst("RECORDS_SUM");

        if (recordsSum != null) {
            String g1Value = recordsSum.selectFirst("G1") != null ?
                    recordsSum.selectFirst("G1").text().replace(".",",") : "Не найдено";
            String tnarValue = recordsSum.selectFirst("Tnar") != null ?
                    recordsSum.selectFirst("Tnar").text().replace(".",",") : "Не найдено";

            write(toPath,getAddress(file.getName()),g1Value,tnarValue);
            System.out.println("Добавлено " + getAddress(file.getName() + " | "+g1Value+" | "+tnarValue ));

        } else {
            System.err.println("RECORDS_SUM не найден в XML");
        }


    }



    @Override
    public void write(Path toPath, String address, String gPod, String tNar) {
        FileWriteCSV.super.write(toPath, address, gPod, tNar);
    }
}
