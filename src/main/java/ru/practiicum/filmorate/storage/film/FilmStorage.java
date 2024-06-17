package ru.practiicum.filmorate.storage.film;

import ru.practiicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film create(Film film);

    Film update(Film film);

    Film findFilmById(Long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    void deleteFilm(Long filmId);
}