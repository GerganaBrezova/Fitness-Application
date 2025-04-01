package app.exceptions;

public class WorkoutNotFound extends RuntimeException {
    public WorkoutNotFound(String message) {
        super(message);
    }
}
