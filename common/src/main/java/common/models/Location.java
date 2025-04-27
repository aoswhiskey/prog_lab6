package common.models;

import java.io.Serializable;

/**
 * Класс локации.
 */
public class Location implements Serializable {
    private final Double x; //Поле не может быть null
    private final Long y; //Поле не может быть null
    private final Integer z; //Поле не может быть null
    private final String name; //Поле может быть null

    public Location(Double x, Long y, Integer z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    @Override
    public String toString() {
        String info = "";
        info += "\n          x: " + x;
        info += "\n          y: " + y;
        info += "\n          z: " + z;
        info += "\n          Наименование локации: " + name;
        return info;
    }

    public Double getX() {
        return x;
    }
    public Long getY() {
        return y;
    }
    public Integer getZ() {
        return z;
    }
}
