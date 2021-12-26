package com.company;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public record FileReader(Path filePath) implements Callable<User> {

    @Override
    public User call() {
        return parse(filePath.toFile());
    }
    private static User parse(File file) {
        java.io.FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new java.io.FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            Map<String, String> userFields = new HashMap<>();

            while ((line = bufferedReader.readLine()) != null) {
                int equalIndex = line.indexOf(FileUtils.EQUAL);
                if (equalIndex < 0) {
                    throw new IllegalStateException();
                }
                userFields.put(line.substring(0, equalIndex), line.substring(equalIndex + 1));
            }
            return new User(
                    userFields.get(FileUtils.NAME),
                    userFields.get(FileUtils.SURNAME),
                    Integer.parseInt(userFields.get(FileUtils.AGE))
            );
        } catch (IOException ex) {
            throw new IllegalStateException("", ex);
        } finally {
            close(fileReader);
            close(bufferedReader);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
