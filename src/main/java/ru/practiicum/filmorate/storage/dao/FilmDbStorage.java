package ru.practiicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.practiicum.filmorate.model.Film;
import ru.practiicum.filmorate.model.Genre;
import ru.practiicum.filmorate.model.Mpa;
import ru.practiicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO film (name, description, release_date, duration, rating_id)" +
                "VALUES (?, ?, ?, ?, ?);";
        String queryForFilmGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        },keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForFilmGenre, film.getId(), genre.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "";
        return findFilmById(film.getId());
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
    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getNString("genre_name") );
    }
    private Integer mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }
    private List<Genre> getGenresOfFilm(Long filmId) {
        String queryForFilmGenres = "SELECT fb.film_id, fg,genre_id, g.genre_name FROM film_genre fg" +
                "JOIN genre g ON g.genre_id = fg.genre_id WHERE film_id = ?;";
        return jdbcTemplate.query(queryForFilmGenres, this::mapRowToGenre, filmId);
    }

    private List<Integer> getLikesOfFilm(Long filmId) {
        String queryForFilmLikes = "SELECT user_id FROM film_like WHERE film_id = ?";
        return jdbcTemplate.query(queryForFilmLikes, this::mapRowToLike, filmId);
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
