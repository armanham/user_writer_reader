package com.company;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

public class FileWriter implements Callable<Path> {

    private static final Random RANDOM = new Random();

    @Override
    public Path call() throws Exception {
        Path path = Paths.get(FileUtils.BASE_PATH, getFileName());
        User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString(), RANDOM.nextInt(100));
        return Files.write(path, lines(user), StandardCharsets.UTF_8);
    }
//    with Runnable interface
//    @Override
//    public void run() {
//        List<Callable<? extends Object>> tasks = new ArrayList<>();
//        List<Future<? extends Object>> results = executor.invokeAll(tasks);
//
//        while (latch.getCount() != 0) {
//            Path path = Paths.get(FileUtils.BASE_PATH, UUID.randomUUID().toString() + fileNameSuffix + ".txt");
//            User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString(), RANDOM.nextInt(100));
//            try {
//                Files.write(path, lines(user), StandardCharsets.UTF_8);
//                latch.countDown();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    private Collection<String> lines(User user) {
        return List.of(
                String.format("%s%s%s", FileUtils.NAME, FileUtils.EQUAL, user.getName()),
                String.format("%s%s%s", FileUtils.SURNAME, FileUtils.EQUAL, user.getSurName()),
                String.format("%s%s%s", FileUtils.AGE, FileUtils.EQUAL, user.getAge())
        );
    }

    private static String getFileName() {
        return UUID.randomUUID().toString() + ".txt";
    }
}
