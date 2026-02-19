package me.ivanmorozov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ivanmorozov.service.FileProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

import static me.ivanmorozov.util.UtilMethods.validate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParserRunner implements ApplicationRunner {
    private final FileProcessor fileProcessor;
    private final ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println("=== Парсер документов ===");
        Path input = null;
        boolean isValid = false;

        while (!isValid) {
            try {
                System.out.print("Введите путь к папке с файлами: ");
                String pathStr = reader.readLine().trim();
                validate(pathStr);
                input = Path.of(pathStr);
                isValid = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте снова.\n");
            }
        }

        try {
            fileProcessor.process(input);
            System.out.println("=== ЗАВЕРШЕНО ===");
            log.info("Обработка завершена");
        } finally {
            SpringApplication.exit(applicationContext,()->0);
        }
    }
}
