package ru.practiicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practiicum.filmorate.model.Film;
import ru.practiicum.filmorate.model.Genre;
import ru.practiicum.filmorate.model.Mpa;
import ru.practiicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Collection<Film> getFilms() {
        Map<Long, Film> films = new HashMap<>();
        String sqlQuery = "SELECT f.* FROM film AS f JOIN rating AS r ON f.rating_id = r.rating_id";
        List<Film> filmsFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        for (Film film : filmsFromDb) {
            films.put(film.getId(), film);
        }
        return films.values();
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film findFilmById(Long id) {
        return null;
    }

    @Override
    public void addLike(Long id, Long userId) {

    }

    @Override
    public void deleteLike(Long id, Long userId) {

    }

    private List<Genre> getGenresOfFilm(Long id) {
        return null;
    }

    private List<Integer> getLikesOfFilm(Long id) {
        return null;
    }
    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Film build start>>>");
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")))
                .build();
        log.info("Film = {}", film);
        List<Genre> genresOfFilm = getGenresOfFilm(film.getId());
        List<Integer>likes = getLikesOfFilm(film.getId());
        for (Genre genre : genresOfFilm) {
            film.getGenres().add(genre);
        }
        for (Integer like : likes) {
            film.getLikes().add(Long.valueOf(like));
        }
        return film;
    }
}
