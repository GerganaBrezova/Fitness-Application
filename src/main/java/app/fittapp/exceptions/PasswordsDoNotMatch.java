package app.fittapp.exceptions;

public class PasswordsDoNotMatch extends RuntimeException {

    public PasswordsDoNotMatch(String message) {
        super(message);
    }
}
