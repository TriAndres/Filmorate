package ru.practiicum.filmorate.storage.genre;

import ru.practiicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Optional;

public interface GenreStorage {
    Map<Integer, Genre> getAllGenres();
    Optional<Genre> findGenreById(Integer id);
}
