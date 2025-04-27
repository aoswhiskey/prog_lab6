package client.ask;

import common.models.Location;
import client.utility.Console;

import java.util.NoSuchElementException;
/**
 * Отвечает за прием вводимых пользователем данных о локации.
 */
public class AskLocation {
    /**
     * Принимает вводимые пользователем данных о локации.
     * @param console Консоль для ввода данных.
     * @return Объект класса Location.
     */
    public static Location askLocation(Console console) throws AskBreak {
        try {
            Double x;
            Long y;
            Integer z;
            String name;
            if (console.ifFileMode()) {
                x = Double.parseDouble(console.readln().trim());
                y = Long.parseLong(console.readln().trim());
                z = Integer.parseInt(console.readln().trim());
                name = console.readln().trim();
            }
            else {
                while (true) {
                console.print("location.coordinates.x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        x = Double.parseDouble(line);
                        break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное число.");
                    }
                } else {
                    console.printError("Координата x не может быть пустой!");
                }
            }
            while (true) {
                console.print("location.coordinates.y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        y = Long.parseLong(line);
                        break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное число.");
                    }
                } else {
                    console.printError("Координата y не может быть пустой!");
                }
            }
            while (true) {
                console.print("location.coordinates.z: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        z = Integer.parseInt(line);
                        break;
                    } catch (NumberFormatException e) {
                        console.printError("Введите корректное целое число.");
                    }
                } else {
                    console.printError("Координата z не может быть пустой!");
                }
            }
            console.print("location.name: ");
            name = console.readln().trim();
            if (name.equals("exit")) throw new AskBreak();
        }
            return new Location(x, y, z, name);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}
