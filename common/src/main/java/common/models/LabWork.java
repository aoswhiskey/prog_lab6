package common.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
/**
 * Класс лабораторной работы.
 */
public class LabWork implements Comparable<LabWork>, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final float minimalPoint; //Значение поля должно быть больше 0, Поле не может быть null
    private final Difficulty difficulty; //Поле не может быть null
    private final Person author; //Поле не может быть null

    public LabWork(int id, String name, Coordinates coordinates, LocalDate creationDate,
                   float minimalPoint, Difficulty difficulty, Person author) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }

    public LabWork(int id, String name, Coordinates coordinates, float minimalPoint,
                   Difficulty difficulty, Person author) {
        this(id, name, coordinates, LocalDate.now(), minimalPoint, difficulty, author);
    }
    /**
     * @return ID лабораторной работы.
     */
    public int getId() {
        return id;
    }
    /**
     * @return Имя лабораторной работы.
     */
    public String getName() {
        return name;
    }
    /**
     * @return Координаты лабораторной работы.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
    /**
     * @return Дата создания лабораторной работы.
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }
    /**
     * @return Минимальная оценка лабораторной работы.
     */
    public float getMinimalPoint() {
        return minimalPoint;
    }
    /**
     * @return Сложность лабораторной работы.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
    /**
     * @return Автор лабораторной работы.
     */
    public Person getAuthor() {
        return author;
    }
    public void setId(int id) { this.id = id; }
    @Override
    public int compareTo(LabWork labWork) {
        return this.id - labWork.getId();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabWork that = (LabWork) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, minimalPoint, difficulty, author);
    }
    @Override
    public String toString() {
        String info = "";
        info += "Лабораторная работа № " + id;
        info += " (добавлена " + creationDate.toString() + ")";
        info += "\n Название: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Минимальная оценка: " + minimalPoint;
        info += "\n Сложность: " + ((difficulty == null) ? null : "'" + difficulty + "'");
        info += "\n Информация об авторе: " + author;
        return info;
    }
}