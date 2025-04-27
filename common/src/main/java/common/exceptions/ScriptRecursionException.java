package common.exceptions;
/**
 * Выбрасывается, если происходит рекурсия вызывания скрипта
 */
public class ScriptRecursionException extends Exception {
    public ScriptRecursionException() {
        super("Скрипты не могут вызываться рекурсивно!");
    }
}
