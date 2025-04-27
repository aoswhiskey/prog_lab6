package client.utility;

import java.util.Scanner;
import java.lang.IllegalStateException;
import java.util.NoSuchElementException;
/**
 * Для ввода команд и вывода результата.
 */
public class StandardConsole implements Console {
    public static final String P = "$ ";
    public static Scanner fileScanner = null;
    public static Scanner defScanner = new Scanner(System.in);
    /**
     * Выводит obj.toString() в консоль
     * @param obj Объект для печати
     */
    public void print(Object obj) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.print(obj);
    }
    /**
     * Выводит obj.toString() + \n в консоль
     * @param obj Объект для печати
     */
    public void println(Object obj) {
        System.out.println(obj);
    }
    /**
     * Выводит ошибка: obj.toString() в консоль
     * @param obj Ошибка для печати
     */
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }
    /**
     * Выводит таблицу из 2 колонок.
     * @param elementLeft Левый элемент колонки.
     * @param elementRight Правый элемент колонки.
     */
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-40s%-1s%n", elementLeft, elementRight);
    }

    /**
     * Выводит prompt.
     */
    public void prompt() {
        print(P);
    }
    /**
     * @return prompt.
     */
    public String getPrompt() {
        return P;
    }
    /**
     * Принимает данные из консоли.
     * @return Считанные данные.
     */
    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).nextLine();
    }
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    public void selectConsoleScanner() {
        fileScanner = null;
    }


    public boolean ifFileMode() {
        return fileScanner != null;
    }
}