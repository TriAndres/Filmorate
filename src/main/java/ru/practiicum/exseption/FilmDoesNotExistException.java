package ru.practiicum.exseption;

public class FilmDoesNotExistException extends RuntimeException{
    public FilmDoesNotExistException(String message) {
        super(message);
    }
}
