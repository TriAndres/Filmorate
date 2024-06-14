package ru.practiicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practiicum.filmorate.validation.ReleaseDateValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotEmpty(message = "Название не должно быть пустым")
    private String name;
    @NotNull
    @Length(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    @NotNull
    @ReleaseDateValidation
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "Продолжительность фильма не может бытьотрицательной")
    private Integer duration;
    @NotNull
    private final Mpa mpa;
    private final Set<Long> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
}