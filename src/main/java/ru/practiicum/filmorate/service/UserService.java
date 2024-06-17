package ru.practiicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practiicum.filmorate.exseption.UserDoesNotExistException;
import ru.practiicum.filmorate.exseption.ValidationException;
import ru.practiicum.filmorate.model.User;
import ru.practiicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FilmService filmService;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FilmService filmService) {
        this.userStorage = userStorage;
        this.filmService = filmService;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User create(User user) {
        for (User registreaUser : userStorage.getUsers()) {
            if (registreaUser.getEmail().equals(user.getEmail())) {
                log.warn("Пользовватель с электронной почтой {} ужезарегистрирован", user.getEmail());
                throw new ValidationException();
            }
        }
        log.info("Добавлен новый пользователь");
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.findUserById(user.getId()) == null) {
            log.warn("Невозможно обновить пользователя");
            throw new UserDoesNotExistException();
        }
        log.info("Пользователь с id {} обнавлён", user.getId());
        return userStorage.update(user);
    }

    public User findUserById(Long id) {
        User user = userStorage.findUserById(id);
        if (user == null) {
            log.warn("Пользователь с id {} не найден", id);
            throw new UserDoesNotExistException();
        }
        return user;
    }

    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
        log.info("Пользователи с id {} и {} теперь друзья", userId, friendId);
    }

    public void removeFromFriends(Long userId, Long friendId) {
        userStorage.removeFromFriend(userId, friendId);
        log.info("Пользователи с id {} и {} еперь не являютсядрузьями", userId, friendId);
    }

    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        return userStorage.getMutualFriends(userId, otherUserId);
    }

    public List<User> getAllFriends(Long userId) {
        return userStorage.getAllFriends(userId);
    }
}