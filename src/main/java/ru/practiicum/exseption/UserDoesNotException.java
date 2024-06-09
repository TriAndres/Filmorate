package ru.practiicum.exseption;

public class UserDoesNotException extends RuntimeException {
    public UserDoesNotException(String message) {
        super(message);
    }
}
