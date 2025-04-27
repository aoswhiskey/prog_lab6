package server.commands;

import common.network.requests.Request;
import common.network.responses.Response;

/**
 * Интерфейс для всех выполняемых команд.
 */
public interface Executable {
    /**
     * Выполняет что-либо.
     * @param arguments Аргумент для выполнения
     * @return результат выполнения
     */
    Response execute(Request request);
}