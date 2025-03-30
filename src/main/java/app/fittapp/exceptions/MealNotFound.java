package app.fittapp.exceptions;

public class MealNotFound extends RuntimeException {
    public MealNotFound(String message) {
        super(message);
    }
}
