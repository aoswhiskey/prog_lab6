package server.network;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import server.App;
import server.utility.CommandHandler;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

// Класс UDPDatagramServer наследует абстрактный UDPServer и реализует его методы через UDP-сокеты
public class UDPDatagramServer extends UDPServer {
    // Максимальный размер одного UDP-пакета (включая байт-флаг)
    private final int PACKET_SIZE = 1024;
    // Размер полезных данных в пакете (оставляем 1 байт для флага конца передачи)
    private final int DATA_SIZE = PACKET_SIZE - 1;

    // Сам UDP-сокет, через который будем отправлять и принимать датаграммы
    private final DatagramSocket datagramSocket;

    // Логгер для вывода информации о работе на консоль или в файл
    private final Logger logger = App.logger;

    /**
     * Конструктор создает DatagramSocket на указанном адресе и порту,
     * а также передает обработчик команд в базовый класс.
     */
    public UDPDatagramServer(InetAddress address, int port, CommandHandler commandHandler) throws SocketException {
        // Вызываем конструктор родителя, передавая адрес сервера и обработчик команд
        super(new InetSocketAddress(address, port), commandHandler);
        // Создаем UDP-сокет и привязываем его к адресу сервера
        this.datagramSocket = new DatagramSocket(getAddr());
        // Разрешаем быстрое перепривязывание адреса (для перезапуска сервера)
        this.datagramSocket.setReuseAddress(true);
    }

    /**
     * Метод получения данных от клиента.
     * Читает пакеты до тех пор, пока не получит пакет с флагом конца передачи.
     */
    @Override
    public Pair<Byte[], SocketAddress> receiveData() throws IOException {
        // Флаг, указывающий на завершение приемки всех пакетов
        var received = false;
        // Буфер для склеивания всех полученных байтов
        var result = new byte[0];
        // Адрес клиента, от которого пришли данные
        SocketAddress addr = null;

        // Цикл чтения пакетов до установки received = true
        while(!received) {
            // Временный буфер для одного пакета
            var data = new byte[PACKET_SIZE];

            // Пакет для приема данных
            var dp = new DatagramPacket(data, PACKET_SIZE);
            // Блокирующий вызов: ждем прихода очередной датаграммы
            datagramSocket.receive(dp);

            // Запоминаем адрес клиента
            addr = dp.getSocketAddress();
            logger.info("Получено \"" + Arrays.toString(data) + "\" от " + dp.getAddress());
            logger.info("Последний байт: " + data[data.length - 1]);

            // Проверяем флаг конца передачи в последнем байте пакета
            if (data[data.length - 1] == 1) {
                // Если пришел флаг = 1, значит это последний пакет
                received = true;
                logger.info("Получение данных от " + dp.getAddress() + " окончено");
            }
            // Склеиваем полученные данные (кроме последнего флага)
            result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
        }
        // Возвращаем полученные байты (преобразованные в Byte[]) и адрес клиента
        return new ImmutablePair<>(ArrayUtils.toObject(result), addr);
    }

    /**
     * Метод отправки данных клиенту.
     * Делит массив байтов на фрагменты по DATA_SIZE, помечает последний фрагмент флагом = 1,
     * и отправляет каждый фрагмент через DatagramSocket.
     */
    @Override
    public void sendData(byte[] data, SocketAddress addr) throws IOException {
        // Вычисляем количество фрагментов, необходимых для отправки всех данных
        byte[][] ret = new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][DATA_SIZE];

        int start = 0;
        // Разбиваем данные на массив фрагментов фиксированного размера
        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(data, start, start + DATA_SIZE);
            start += DATA_SIZE;
        }

        logger.info("Отправляется " + ret.length + " чанков...");

        // Отправляем каждый фрагмент по очереди
        for(int i = 0; i < ret.length; i++) {
            var chunk = ret[i];
            if (i == ret.length - 1) {
                // Если это последний фрагмент, добавляем байт-флаг = 1
                var lastChunk = Bytes.concat(chunk, new byte[]{1});
                var dp = new DatagramPacket(lastChunk, PACKET_SIZE, addr);
                datagramSocket.send(dp);
                logger.info("Последний чанк размером " + chunk.length + " отправлен на сервер.");
            } else {
                // Для остальных фрагментов байт-флаг = 0 (в ByteBuffer по умолчанию 0)
                var dp = new DatagramPacket(ByteBuffer.allocate(PACKET_SIZE).put(chunk).array(), PACKET_SIZE, addr);
                datagramSocket.send(dp);
                logger.info("Чанк размером " + chunk.length + " отправлен на сервер.");
            }
        }

        logger.info("Отправка данных завершена");
    }

    /**
     * Логическое подключение к клиенту (назначает адрес для отправки/приема по умолчанию).
     */
    @Override
    public void connectToClient(SocketAddress addr) throws SocketException {
        datagramSocket.connect(addr);
    }

    /**
     * Логическое отключение от клиента (сбрасывает привязку адреса).
     */
    @Override
    public void disconnectFromClient() {
        datagramSocket.disconnect();
    }

    /**
     * Полное закрытие сокета и освобождение ресурсов.
     */
    @Override
    public void close() {
        datagramSocket.close();
    }
}
