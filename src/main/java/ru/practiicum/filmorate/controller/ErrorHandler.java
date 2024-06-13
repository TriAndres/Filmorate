package ru.practiicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practiicum.filmorate.exseption.*;

import java.io.FileNotFoundException;

@Slf4j
@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка валидации");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(FileNotFoundException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Фильм не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserDoesNotExistException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Пользователь не найден");
    }
}