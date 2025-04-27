package client.utility;

import java.util.Scanner;

/**
 * Консоль для ввода команд и вывода результата.
 */
public interface Console {
    void printError(Object obj);
    void print(Object obj);
    void println(Object obj);
    void printTable(Object obj1, Object obj2);
    void prompt();
    String getPrompt();
    String readln();
    boolean isCanReadln();
    void selectFileScanner(Scanner obj);
    void selectConsoleScanner();
    boolean ifFileMode();
}
