package ru.practiicum.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
@Data
public class User {
    private Long id;
    @NotNull
    @Email(message = "Адрес электронной почты введён некорректно")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+$")
    private String login;
    private String name;

    @PastOrPresent(message = "День рождения не может быть в будущем")
    @NotNull
    private LocalDate birthday = LocalDate.now();
}
