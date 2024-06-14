package ru.practiicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practiicum.filmorate.exseption.FilmDoesNotExistException;
import ru.practiicum.filmorate.model.Film;
import ru.practiicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        log.info("Добавлен новый фильм");
        filmStorage.create(film);
        return film;
    }

    public Film update(Film film) {
        if (filmStorage.findFilmById(film.getId()) == null) {
            log.warn("Невозможно обновить фильм");
            throw new FilmDoesNotExistException();
        }
        log.info("Филм с id {} обнавлён", film.getId());
        return filmStorage.update(film);
    }

    public Film findFilmById(Long id) {
        Film film = filmStorage.findFilmById(id);
        if (film == null) {
            throw new FilmDoesNotExistException();
        }
        return film;
    }

    public void addLike(Long id, Long userId) {
        filmStorage.addLike(id, userId);
        //log.info("Пользователь с id {} оставил с id {} лайк", userId,id);
    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id, userId);
        //log.info("Лайк пользователя с id {} фильму с id {} удалён", id, userId);
    }

    public List<Film> getMostPopularFilms(Long count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
