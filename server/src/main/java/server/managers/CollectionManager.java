package server.managers;

import com.google.common.collect.Iterables;
import common.models.LabWork;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Оперирует коллекцией.
 */
public class CollectionManager {
    private int currentId = 1;
    private final PriorityQueue<LabWork> collection = new PriorityQueue<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;

        init();
    }
    /**
     * @return Первый элемент коллекции.
     */
    public LabWork getFirst() {
        if (collection.isEmpty()) return null;
        return collection.peek();
    }
    /**
     * @return Последний элемент коллекции.
     */
    public LabWork getLast() {
        return collection.stream()
            .reduce((first, second) -> second)
            .orElse(null);
    }
    /**
     * @param index index элемента.
     * @return Элемент коллекции по позиции в коллекции (индексу).
     */
    public LabWork getElementByIndex(int index) {
        return collection.stream()
                .skip(index)
                .findFirst()
                .orElseThrow(() ->
                        new IndexOutOfBoundsException("Invalid index: " + index));
    }
    /**
     * @param id ID элемента.
     * @return Элемент коллекции по ID.
     */
    public LabWork byId(int id) {
        return collection.stream()
                .filter(e -> e.getId() == id)
                .findAny()
                .orElse(null);
    }
    /**
     * @return Время последней инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }
    /**
     * @return Время последнего сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }
    /**
     * @return Коллекцию.
     */
    public PriorityQueue<LabWork> getCollection() {
        return collection;
    }
    /**
     * @return Имя типа коллекции.
     */
    public String collectionType() {
        return collection.getClass().getName();
    }
    /**
     * @return Размер коллекции.
     */
    public int collectionSize() {
        return collection.size();
    }
    /**
     * @return Свободный ID.
     */
    public int getFreeId() {
        int start = currentId;
        int free = IntStream.iterate(start, i -> i + 1)
                .filter(i -> byId(i) == null)
                .findFirst()
                .getAsInt();
        currentId = free; // обновляем текущее значение
        return free;
    }
    /**
     * Добавляет элемент в коллекцию.
     * @param labWork Элемент для добавления.
     */
    public void add(LabWork labWork) {
        collection.add(labWork);
        sort();
    }
    /**
     * Обновляет элемент в коллекции.
     * @param labWork Элемент для обновления.
     */
    public void update(LabWork labWork) {
        collection.remove(byId(labWork.getId()));
        collection.add(labWork);
        sort();
    }
    /**
     * Удаляет элемент из коллекции.
     * @param id ID элемента для удаления.
     */
    public void remove(int id) {
        var labWork = byId(id);
        if (labWork == null) return;
        collection.remove(labWork);
        sort();
    }
    /**
     * Сортирует коллекцию.
     */
    public void sort() {
        List<LabWork> sorted = collection.stream()
                .sorted(Comparator.comparing(LabWork::getName))
                .collect(Collectors.toList());
        collection.clear();
        collection.addAll(sorted);
    }

    /**
     * @return Отсортированная коллекция.
     */
    public List<LabWork> getSorted() {
        return new ArrayList<>(collection)
                .stream()
                .sorted(Comparator.comparing(LabWork::getName))
                .collect(Collectors.toList());
    }
    /**
     * Инициализирует коллекцию, загружая ее из файла.
     */
    public void init() {
        collection.addAll(dumpManager.readCollection());
        lastInitTime = LocalDateTime.now();
        sort();
    }
    /**
     * Сохраняет коллекцию в файл.
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }
    /**
     * Очищает коллекцию.
     */
    public void clearCollection() {
        collection.clear();
    }

    /**
     * @param id ID элемента.
     * @return Проверяет, существует ли элемент с таким ID.
     */
    public boolean checkExist(int id) {
        return byId(id) != null;
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";
        return collection.stream()
                .map(LabWork::toString)
                .collect(Collectors.joining("\n\n"));
    }
}
