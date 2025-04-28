package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OnpReadCalculate {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition writeCondition = lock.newCondition();
    private final List<String> equations = new ArrayList<>();
    private boolean isReadyToWrite = false;

    public void read() {
        lock.lock();
        try (Scanner fileRead = new Scanner(new File("rownania.txt"));) {
            while(fileRead.hasNext()) {
                equations.add(fileRead.nextLine());
            }
            isReadyToWrite = true;
            writeCondition.signalAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public String calculate() {
        lock.lock();
        try {
            while (!isReadyToWrite) {
                writeCondition.await();
            }
            ONP onp = new ONP();
            String onpEquation = onp.przeksztalcNaOnp(equations.removeFirst());
            return onp.obliczOnp(onpEquation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
