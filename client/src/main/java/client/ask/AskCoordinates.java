package client.ask;

import common.models.Coordinates;
import client.utility.Console;

import java.util.NoSuchElementException;
/**
 * Отвечает за прием вводимых пользователем координат.
 */
public class AskCoordinates {
    /**
     * Принимает вводимые пользователем координаты.
     * @param console Консоль для ввода данных.
     * @return Объект класса Coordinates.
     */
    public static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            Long x;
            long y;
            if (console.ifFileMode()) {
                x = Long.parseLong(console.readln().trim());
                y = Long.parseLong(console.readln().trim());
            }
            else {
                while (true) {
                    console.print("coordinates.x (<= 595): ");
                    var line = console.readln().trim();
                    if (line.equals("exit")) throw new AskBreak();
                    if (!line.equals("")) {
                        try {
                            x = Long.parseLong(line);
                            if (x <= 595) break;
                            else console.printError("x должно быть меньше или равно 595.");
                        } catch (NumberFormatException e) {
                            console.printError("Введите корректное число.");
                        }
                    } else {
                        console.printError("Координата x не может быть пустой!");
                    }
                }
                while (true) {
                    console.print("coordinates.y (> -974): ");
                    var line = console.readln().trim();
                    if (line.equals("exit")) throw new AskBreak();
                    if (!line.equals("")) {
                        try {
                            y = Long.parseLong(line);
                            if (y > -974) break;
                            else console.printError("y должно быть больше -974.");
                        } catch (NumberFormatException e) {
                            console.printError("Введите корректное число.");
                        }
                    } else {
                        console.printError("Координата y не может быть пустой!");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}
