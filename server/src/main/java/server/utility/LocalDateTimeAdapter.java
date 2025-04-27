package server.utility;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Адаптер для сериализации и десериализации объектов типа LocalDateTime.
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    /**
     * Сериализует объект в JSON.
     * @param out   JSON-писатель, в который записывается дата.
     * @param value Объект, который нужно сериализовать.
     * @throws IOException Если возникает ошибка при записи в JSON.
     */
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value != null) {
            out.value(value.format(formatter));
        } else {
            out.nullValue();
        }
    }
    /**
     * Десериализует строковое значение из JSON в объект.
     * @param in JSON-чтение, из которого читается строковое представление даты.
     * @return Объект, десериализованный из строки.
     * @throws IOException Если возникает ошибка при чтении из JSON.
     */
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String dateTimeString = in.nextString();
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
