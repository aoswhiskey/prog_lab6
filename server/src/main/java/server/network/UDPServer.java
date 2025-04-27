package server.network;

import common.network.requests.Request;
import common.network.responses.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationUtils;
import server.App;
import common.network.responses.NoSuchCommandResponse;
import server.utility.CommandHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Абстрактный класс для обработки UDP-запросов.
 * Определяет общий шаблон (Template Method) работы сервера.
 */
abstract class UDPServer {
    // Адрес (IP + порт), на котором сервер слушает входящие запросы
    private final InetSocketAddress addr;
    // Обработчик команд: преобразует Request в Response
    private final CommandHandler commandHandler;
    // Опциональный хук, выполняемый после обработки каждого запроса
    private Runnable afterHook;
    // Логгер для вывода информации о событиях сервера
    private final Logger logger = App.logger;
    // Флаг, определяющий, продолжает ли сервер работу
    private boolean running = true;

    /**
     * Конструктор сохраняет адрес и обработчик команд
     */
    public UDPServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
    }

    /**
     * Возвращает адрес сервера для создания сокета или логирования
     */
    public InetSocketAddress getAddr() {
        return addr;
    }

    /**
     * Абстрактный метод для получения данных от клиента.
     * Возвращает пару: массив байтов и адрес отправителя.
     */
    public abstract Pair<Byte[], SocketAddress> receiveData() throws IOException;

    /**
     * Абстрактный метод для отправки данных клиенту.
     * Принимает данные и адрес назначения.
     */
    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    /**
     * Абстрактный метод логического подключения к клиенту (для UDP).
     */
    public abstract void connectToClient(SocketAddress addr) throws SocketException;

    /**
     * Абстрактный метод логического отключения от клиента.
     */
    public abstract void disconnectFromClient();

    /**
     * Абстрактный метод полного закрытия сервера и освобождения ресурсов.
     */
    public abstract void close();

    /**
     * Основной метод запуска сервера. Циклически выполняет:
     * 1. Приём данных от клиента (receiveData)
     * 2. Логическое подключение (connectToClient)
     * 3. Десериализацию байтов в объект Request
     * 4. Обработку запроса через командный хендлер
     * 5. Сериализацию и отправку Response клиенту (sendData)
     * 6. Логическое отключение (disconnectFromClient)
     * Повторяется, пока флаг running = true.
     */
    public void run() {
        // Логируем старт сервера вместе с его адресом
        logger.info("Сервер запущен по адресу " + addr);

        // Основной цикл обработки
        while (running) {
            Pair<Byte[], SocketAddress> dataPair;
            try {
                // Шаг 1: Приём данных от клиента
                dataPair = receiveData();
            } catch (Exception e) {
                // Если не удалось получить данные — логируем и пробуем дальше
                logger.error("Ошибка получения данных : " + e.toString(), e);
                disconnectFromClient();
                continue;
            }

            // Извлекаем полезные данные и адрес клиента
            var dataFromClient = dataPair.getKey();
            var clientAddr = dataPair.getValue();

            try {
                // Шаг 2: Логическое подключение к клиенту
                connectToClient(clientAddr);
                logger.info("Соединено с " + clientAddr);
            } catch (Exception e) {
                // Логируем ошибку подключения, но не прерываем цикл
                logger.error("Ошибка соединения с клиентом : " + e.toString(), e);
            }

            Request request;
            try {
                // Шаг 3: Преобразуем полученные байты в объект Request
                byte[] primitive = ArrayUtils.toPrimitive(dataFromClient);
                request = SerializationUtils.deserialize(primitive);
                logger.info("Обработка " + request + " из " + clientAddr);
            } catch (SerializationException e) {
                // Если десериализация не удалась, логируем и продолжаем
                logger.error("Невозможно десериализовать объект запроса.", e);
                disconnectFromClient();
                continue;
            }

            Response response = null;
            try {
                // Шаг 4: Обрабатываем запрос через CommandHandler
                response = commandHandler.handle(request);
                // Если задан afterHook, выполняем его после обработки
                if (afterHook != null) {
                    afterHook.run();
                }
            } catch (Exception e) {
                // Логируем любые ошибки внутри обработчика команд
                logger.error("Ошибка выполнения команды : " + e.toString(), e);
            }
            // Если обработка не вернула ответа, создаём стандартный ответ "NoSuchCommand"
            if (response == null) {
                response = new NoSuchCommandResponse(request.getName());
            }

            // Шаг 5: Сериализуем Response в байты для отправки
            byte[] data = SerializationUtils.serialize(response);
            logger.info("Ответ: " + response);

            try {
                // Отправляем ответ клиенту
                sendData(data, clientAddr);
                logger.info("Отправлен ответ клиенту " + clientAddr);
            } catch (Exception e) {
                // Логируем ошибки ввода-вывода при отправке
                logger.error("Ошибка ввода-вывода : " + e.toString(), e);
            }

            // Шаг 6: Логическое отключение от клиента после отправки ответа
            disconnectFromClient();
            logger.info("Отключение от клиента " + clientAddr);
        }

        // После выхода из цикла (stop вызван) закрываем сервер полностью
        close();
    }

    /**
     * Устанавливает хук, выполняемый после каждой обработки запроса.
     * @param afterHook Лямбда или метод, который нужно вызвать.
     */
    public void setAfterHook(Runnable afterHook) {
        this.afterHook = afterHook;
    }

    /**
     * Останавливает работу сервера: флаг running становится false.
     */
    public void stop() {
        running = false;
    }
}