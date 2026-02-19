package me.ivanmorozov.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ivanmorozov.extarctAndWrite.FileExtractInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.ivanmorozov.util.UtilMethods.getExtension;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessor {
    private final Map<String, FileExtractInfo> strategies;
    public void process(Path folderPathFromRead) throws IOException {
        Path resultPath = folderPathFromRead.resolve("result.csv");
        Files.deleteIfExists(resultPath);
        Files.createFile(resultPath);
        log.info("Создан файл результата: {}", resultPath);
        try (Stream<Path> pathStream = Files.walk(folderPathFromRead)) {
            Set<Path> pdfFiles = pathStream
                    .filter(path -> {
                        String name = path.toString().toLowerCase();
                        return name.endsWith(".pdf") || name.endsWith(".html") ||
                                name.endsWith(".xls") || name.endsWith(".xlsx");
                    })
                    .filter(path -> path.getFileName().toString().contains("ЦО"))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            for (Path filePath : pdfFiles) {
                log.info("Обработка файла: {}", filePath);

                String extension = getExtension(filePath);
                FileExtractInfo strategy = strategies.get(extension.toLowerCase());
                if (strategy == null) {
                    log.warn("Нет стратегии для файла: {}", filePath);
                    continue;
                }
                try {
                    strategy.extract(filePath.toFile(), resultPath);

                } catch (Exception e) {
                    log.error("Не удалось разобрать файл: {}", filePath.getFileName(), e);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
