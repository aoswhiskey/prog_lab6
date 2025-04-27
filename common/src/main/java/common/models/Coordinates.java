package common.models;

import java.io.Serializable;

/**
 * Класс координат.
 */
public class Coordinates implements Serializable {
    private final Long x; //Максимальное значение поля: 595, Поле не может быть null
    private final long y; //Значение поля должно быть больше -974, Поле не может быть null

    public Coordinates(Long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    public Long getX() {
        return x;
    }

    public long getY() {
        return y;
    }
}