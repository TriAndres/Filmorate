package ru.practiicum.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practiicum.validation.ReleaseDateValidation;

import java.time.LocalDate;
@Data
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
}
