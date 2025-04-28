package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static private final Path path = Path.of("rownania.txt");
    private final static int AmountOfEquations = 14;
    private static int line = 0;
    private final static ReentrantLock fileLock = new ReentrantLock();
    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            OnpReadCalculate onpRC = new OnpReadCalculate();
            executor.submit(Executors.callable(onpRC::read));
            for (int i = 0; i < AmountOfEquations; i++) {
                executor.submit(Executors.callable(new FutureTask<>(onpRC::calculate) {
                    @Override
                    protected void done() {
                        if (isCancelled()) {
                            System.out.println("Canceled!");
                            return;
                        }
                        try {
                            String result = get();
                            fileLock.lock();
                            List<String> lines = Files.readAllLines(path);

                            lines.set(line, lines.get(line) + result);
                            Files.write(path, lines);
                            line++;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        } finally {
                            fileLock.unlock();
                        }
                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}