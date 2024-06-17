package ru.practiicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practiicum.filmorate.model.User;
import ru.practiicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/id")
    public User findUserById(@NotNull @PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@NotNull @PathVariable Long id, @NotNull @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@NotNull @PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @DeleteMapping("/{id}/friends/{frendId}")
    public void removeFromFriends(@NotNull @PathVariable Long id, @NotNull @PathVariable Long friendId) {
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@NotNull @PathVariable Long id, @NotNull @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@NotNull @PathVariable Long id) {
        return userService.getAllFriends(id);
    }
}
