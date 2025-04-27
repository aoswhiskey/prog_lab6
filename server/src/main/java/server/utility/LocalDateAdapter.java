package server.utility;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер для сериализации и десериализации объектов типа LocalDate.
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    /**
     * Сериализует объект в JSON.
     * @param out   JSON-писатель, в который записывается дата.
     * @param value Объект, который нужно сериализовать.
     * @throws IOException Если возникает ошибка при записи в JSON.
     */
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
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
    public LocalDate read(JsonReader in) throws IOException {
        String date = in.nextString();
        return LocalDate.parse(date, formatter);
    }
}
