package app.fittapp.exceptions;

public class InsufficientAmountOfPoints extends RuntimeException {
    public InsufficientAmountOfPoints(String message) {
        super(message);
    }
}
