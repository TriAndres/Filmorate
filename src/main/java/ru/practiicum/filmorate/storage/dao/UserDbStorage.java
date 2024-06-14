package ru.practiicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practiicum.filmorate.model.User;
import ru.practiicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        return List.of();
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public void addFriend(Long userId, Long friendId) {

    }

    @Override
    public void removeFromFriend(Long userId, Long friendId) {

    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        return List.of();
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return List.of();
    }
}
