package com.webapp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

    public static String readFile(String filePath) {
        try {
            return _readFile(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while reading from file " + filePath, e);
        }
    }

    public static String readFile(Path path) {
        try {
            return Files.readString(path, UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while reading from file " + path, e);
        }
    }

    public static String readFile(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading from InputStream " + e.getMessage(), e);
        }
    }

    public static void writeToFile(File file, String text) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print(text);
        } catch (IOException e) {
            log.warn("Error occurred while writing into file " + file.getName(), e);
        }
    }

    public static boolean createFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while creating file " + file.getName(), e);
        }
    }

    public static void createReplaceFileWithDirTree(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while creating file " + path.toFile().getName(), e);
        }
    }

    public static void createReplaceFileWithDirTree(List<File> files) {
        files.forEach(file -> createReplaceFileWithDirTree(file.toPath()));
    }

    public static List<String> readCsvFile(String filePath) {
        List<String> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, Charset.defaultCharset()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("//")) {
                    content.add(String.join(StrUtil.COMMA, line));
                }
            }
            if (content.isEmpty()) {
                throw new IllegalStateException("File is empty!");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while reading file " + filePath, e);
        }
        return content;
    }

    public static String readFileFormatted(String pathToResource) {
        var file = new File(Objects.requireNonNull(FileUtil.class.getClassLoader().getResource(pathToResource)).getFile());
        Path path = Paths.get(file.getAbsolutePath());
        try (Stream<String> lines = Files.lines(path)) {
            return lines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while reading from file " + path, e);
        }
    }

    private static String _readFile(String filePath) throws IOException {
        String fileOutput;
        StringBuilder stringBuilder = new StringBuilder();

        try (Reader reader = new FileReader(new File(filePath));
             BufferedReader buffered = new BufferedReader(reader)) {

            String line;
            while ((line = buffered.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(StrUtil.EMPTY);
            }

            fileOutput = stringBuilder.toString();
        }
        return fileOutput;
    }
}