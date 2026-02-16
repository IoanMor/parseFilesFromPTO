package me.ivanmorozov.extarctAndWrite;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class HtmlExtract implements FileExtractInfo, FileWriteCSV {


    @Override
    public void extract(File pdfFile, Path toPath) throws IOException {
        Document document = Jsoup.parse(pdfFile);
        Elements newsHeadlines = document.select("#mp-itn b a");

        for (Element headline : newsHeadlines) {

        }
    }

    @Override
    public void write(Path toPath, File pdfFile, String gPod, String tNar) {
        FileWriteCSV.super.write(toPath, pdfFile, gPod, tNar);
    }
}
