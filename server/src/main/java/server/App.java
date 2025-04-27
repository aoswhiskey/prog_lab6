package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.utility.CommandHandler;
import server.managers.CollectionManager;
import server.managers.DumpManager;
import server.managers.CommandManager;
import server.network.UDPDatagramServer;
import server.commands.*;

import common.utility.Commands;
import server.utility.CommandHandler;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Серверная часть приложения.
 * @author maxbarsukov
 */
public class App {
    public static final int PORT = 23456;

    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("Введите имя загружаемого файла как аргумент командной строки");
//            System.exit(1);
//        }

        var dumpManager = new DumpManager("file.json");
        var collection = new CollectionManager(dumpManager);

        Runtime.getRuntime().addShutdownHook(new Thread(collection::saveCollection));

        var commandManager = new CommandManager() {{
            register(Commands.ADD, new Add(collection));
            register(Commands.CLEAR, new Clear(collection));
            register(Commands.FILTER_LESS_THAN_AUTHOR, new FilterLessThanAuthor(collection));
            register(Commands.HEAD, new Head(collection));
            register(Commands.HELP, new Help(this));
            register(Commands.INFO, new Info(collection));
            register(Commands.MAX_BY_MINIMAL_POINT, new MaxByMinimalPoint(collection));
            register(Commands.PRINT_FIELD_DESCENDING_DIFFICULTY, new PrintFieldDescendingDifficulty(collection));
            register(Commands.REMOVE_AT, new RemoveAt(collection));
            register(Commands.REMOVE_BY_ID, new RemoveById(collection));
            register(Commands.REMOVE_FIRST, new RemoveFirst(collection));
            register(Commands.SHOW, new Show(collection));
            register(Commands.UPDATE, new Update(collection));
        }};

        try {
            var server = new UDPDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commandManager));
            server.setAfterHook(collection::saveCollection);
            server.run();
        } catch (SocketException e) {
            logger.fatal("Случилась ошибка сокета", e);
        } catch (UnknownHostException e) {
            logger.fatal("Неизвестный хост", e);
        }
    }
}
