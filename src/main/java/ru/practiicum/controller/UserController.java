package ru.practiicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practiicum.exseption.UserDoesNotException;
import ru.practiicum.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Добавлен новый пользователь");
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.get(user.getId()) == null) {
            log.info("Невозможно обновить пользователя");
            throw new UserDoesNotException("Невозможно обновить пользователя");
        }

        log.info("Пользователь с id {} обнавлён", user.getId());
        users.put(user.getId(), user);
        return user;
    }
}
