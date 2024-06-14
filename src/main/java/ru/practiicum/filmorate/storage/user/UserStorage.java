package ru.practiicum.filmorate.storage.user;

import ru.practiicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getUsers();

    User create(User user);

    User update(User user);

    User findUserById(Long id);

    void deleteUser(Long userId);

    void addFriend(Long userId, Long friendId);

    void removeFromFriend(Long userId, Long friendId);

    List<User> getMutualFriends(Long userId, Long otherUserId);

    List<User> getAllFriends(Long userId);

}