package app.fittapp.exceptions;

public class UsernameAlreadyExists extends RuntimeException {

    public UsernameAlreadyExists(String message) {
        super(message);
    }
}
