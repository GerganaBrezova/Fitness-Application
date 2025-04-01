package app.exceptions;

public class ExcessiveNumberOfMeals extends RuntimeException {
    public ExcessiveNumberOfMeals(String message) {
        super(message);
    }
}
