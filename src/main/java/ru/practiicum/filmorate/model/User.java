package ru.practiicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    @NotNull
    @Email(message = "Адрес электронной почты введён некорректно")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+$")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "День рождения не может быть в будущем")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
