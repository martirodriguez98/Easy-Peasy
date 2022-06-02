package ar.edu.itba.paw.interfaces.exceptions;

public class DuplicateUserException extends Exception {
    public DuplicateUserException() {
        super("This user already exists.");
    }
}
