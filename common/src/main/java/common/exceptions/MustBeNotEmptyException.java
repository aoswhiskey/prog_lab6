package common.exceptions;

public class MustBeNotEmptyException extends Exception {
    String name;
    public MustBeNotEmptyException(String name) {
        super("Строка с " + name + " не может быть пустой!");
        this.name = name;
    }
}
