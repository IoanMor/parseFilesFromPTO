package me.ivanmorozov.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class UtilMethods {
    public static String getAddress(String fileName) {
        String[] strings = fileName.split("_");
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals("д.") || strings[i].equals("д")) {
                return strings[i - 2] + strings[i - 1] + strings[i] + strings[i + 1];
            } else continue;
        }
        return "Не удалось найти дом";
    }

    public static String getExtension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot == -1 ? "" : name.substring(dot + 1).toLowerCase();
    }

    public static void validate(String path) {
        try {
            if (path == null || path.trim().isEmpty()) {
                throw new IllegalArgumentException("Путь не может быть пустым");
            }
            Path inputPath = Paths.get(path).normalize();
            if (!Files.exists(inputPath)) {
                throw new IllegalArgumentException("Входная папка не существует: " + inputPath);
            }
            if (!Files.isDirectory(inputPath)) {
                throw new IllegalArgumentException("Указанный путь не является папкой: " + inputPath);
            }
            if (!Files.isReadable(inputPath)) {
                throw new IllegalArgumentException("Нет прав на чтение папки: " + inputPath);
            }

            log.info("Валидация успешна: {}", inputPath);

        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Некорректный путь: " + e.getMessage());
        }
    }

    }




