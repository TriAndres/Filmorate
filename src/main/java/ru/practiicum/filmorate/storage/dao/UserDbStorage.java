package ru.practiicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.practiicum.filmorate.exseption.UserDoesNotExistException;
import ru.practiicum.filmorate.model.User;
import ru.practiicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        String sqlQuery = "select * from users";
        List<User> userFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        for (User user : userFromDb) {
            users.put(user.getId(), user);
        }
        return users.values();
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO users (email, login, birthday, name) VALUES (?, ?, ?, ?)";
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(sqlQuery, new String[] {"user_id"});
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getLogin());
                    ps.setDate(3, Date.valueOf(user.getBirthday()));
                    ps.setString(4, user.getName());
                    return ps;
                }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return findUserById(user.getId());
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, birthday = ?, name = ? WHERE user_id = ?";
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(),
                user.getId());
        return findUserById(user.getId());
    }

    @Override
    public User findUserById(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            User user = User.builder()
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .id(userRows.getLong("user_id"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();
            log.info("Найден пользователь с id {}",id);
            return user;
        }
        log.warn("ользователь с id {} не найден", id);
        throw new UserDoesNotExistException();
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .id(rs.getLong("user_id"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public void deleteUser(Long userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?;";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friends (user_first_id, user_second_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFromFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        String sqlQuery = "SELECT FROM users AS u WHERE u.user_id IN  (SELECT f.user_second_id " +
                "FROM friendship AS f WHERE f.user_first_id = ?" +
                "INTERSECT SELECT f.user_second_id FROM friendship AS f WHERE f.user_first_id = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        String sqlQuery = "SELECT * FROM users AS u WHERE u.user_id IN" +
                "(SELECT f.user_second_id FROM friendship AS f WHERE f.user_first_id = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }
}
