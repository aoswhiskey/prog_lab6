package server.managers;

import com.google.gson.*;
import common.models.LabWork;
import server.App;
import server.utility.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class DumpManager {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private final String fileName;

    public DumpManager(String fileName) {
        this.fileName = fileName;
    }

    public void writeCollection(PriorityQueue<LabWork> collection) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            String json = gson.toJson(collection);
            fileWriter.write(json);
            App.logger.info("Коллекция успешно сохранена в файл!");
        } catch (IOException exception) {
            App.logger.error("Загрузочный файл не может быть открыт!");
        }
    }

    public PriorityQueue<LabWork> readCollection() {
        PriorityQueue<LabWork> validCollection = new PriorityQueue<>();

        if (fileName != null && !fileName.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

                for (JsonElement jsonElement : jsonArray) {
                    try {
                        LabWork labWork = gson.fromJson(jsonElement, LabWork.class);

                        if (LabWorkValidator.isValid(labWork)) {
                            validCollection.add(labWork);
                        } else {
                            App.logger.error("Невалидный элемент коллекции пропущен: " + labWork);
                        }
                    } catch (RuntimeException e) {
                        App.logger.error("Ошибка парсинга элемента: " + jsonElement);
                    }
                }

                App.logger.info("Коллекция успешно загружена с игнорированием невалидных объектов!");

                App.logger.info(validCollection);

            } catch (FileNotFoundException exception) {
                App.logger.error("Загрузочный файл не найден!");
            } catch (IOException exception) {
                App.logger.error("Непредвиденная ошибка!");
            }
        } else {
            App.logger.error("Аргумент командной строки с загрузочным файлом не найден!");
        }

        return validCollection;
    }
}
