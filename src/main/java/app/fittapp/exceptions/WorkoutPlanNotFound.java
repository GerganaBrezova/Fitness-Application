package app.fittapp.exceptions;

public class WorkoutPlanNotFound extends RuntimeException {
    public WorkoutPlanNotFound(String message) {
        super(message);
    }
}
