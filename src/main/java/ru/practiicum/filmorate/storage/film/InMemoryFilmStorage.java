package ru.practiicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practiicum.filmorate.model.Film;
import ru.practiicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final Map<Long, Film> films = new HashMap<>();

    public InMemoryFilmStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film findFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        return null;
    }

    @Override
    public void addLike(Long id, Long userId) {
        Film film = findFilmById(id);
        if (film != null && userStorage.findUserById(userId) != null) {
            findFilmById(id).getLikes().add(userId);
            log.info("Пользователь с id {} оставил с id {} лайк", userId, id);
        } else {
            log.info("Пользователь с id {} не поставил фильм с id {} лайк", userId, id);
        }
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        Film film = findFilmById(id);
        if (film != null && userStorage.findUserById(userId) != null) {
            film.getLikes().remove(userId);
            log.info("Лайк пользователя с id {} фильму с id {} удалён", id, userId);
        } else {
            log.info("Ранее лайк пользователя с id {} не ставил", userId);
        }
    }
}