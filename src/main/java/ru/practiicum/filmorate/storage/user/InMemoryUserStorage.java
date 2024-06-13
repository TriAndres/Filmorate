package ru.practiicum.filmorate.storage.user;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import ru.practiicum.filmorate.model.User;

import java.util.*;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    @Override
    public Collection<User> getUsers() {
        return users.values();
    }
    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }
    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        return null;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = findUserById(id);
        User friend = findUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            user.getFriends().add(id);
        }
    }

    @Override
    public void removeFromFriend(Long id, Long friendId) {
        User user = findUserById(id);
        User friend = findUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            user.getFriends().remove(id);
        }
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> mutualFriends = new ArrayList<>();
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        if (user != null && friend != null) {
            Set<Long> mutualFriendsIds = Sets.intersection(user.getFriends(), friend.getFriends());
            for (Long id : mutualFriendsIds) {
                mutualFriends.add(findUserById(id));
            }
        }
        return mutualFriends;
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        User user = findUserById(userId);
        if (user != null) {
            for (Long id : user.getFriends()) {
                friends.add(findUserById(id));
            }
        }
        return friends;
    }
}