package common.utility;

import common.models.*;

public class LabWorkValidator {
    public static boolean isValid(LabWork labWork) {
        if (labWork == null) return false;

        if (labWork.getId() <= 0) return false;

        if (labWork.getName() == null || labWork.getName().trim().isEmpty()) return false;

        Coordinates coordinates = labWork.getCoordinates();
        if (coordinates == null ||
                coordinates.getX() == null || coordinates.getX() > 595 ||
                coordinates.getY() <= -974) return false;

        if (labWork.getCreationDate() == null) return false;

        if (labWork.getMinimalPoint() <= 0) return false;

        if (labWork.getDifficulty() == null) return false;

        Person author = labWork.getAuthor();
        if (author == null || author.getName() == null || author.getName().trim().isEmpty() ||
                author.getBirthday() == null || !isValidPerson(author)) return false;

        return true;
    }

    private static boolean isValidPerson(Person author) {
        if (author.getHeight() != null && author.getHeight() <= 0) return false;

        Location location = author.getLocation();
        if (location == null || location.getX() == null || location.getY() == null || location.getZ() == null) return false;

        return true;
    }
}
