package com.company;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        List<Callable<Path>> writerTasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            writerTasks.add(new FileWriter());
        }
//        List<Callable<Path>> collect = IntStream.range(0, 100)
//                .mapToObj(index -> new FileWriter())
//                .collect(Collectors.toUnmodifiableList());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2, 6, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200));
        List<Future<Path>> futureList = null;
        try {
            futureList = threadPoolExecutor.invokeAll(writerTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<FileReader> readerTasks = new ArrayList<>();
        List<Path> paths = new ArrayList<>();
        try {
            for (Future<Path> future : futureList) {
                Path path = future.get(10, TimeUnit.SECONDS);
                paths.add(path);
                readerTasks.add(new FileReader(Path.of(FileUtils.BASE_PATH)));
            }
            List<Future<User>> futures = threadPoolExecutor.invokeAll(readerTasks);
            for (Future<User> future : futures) {
                User user = future.get();
                System.out.println(user);
            }
        } catch (ExecutionException | TimeoutException | InterruptedException exception) {
            exception.printStackTrace();
        }

        threadPoolExecutor.shutdown();
    }
}
