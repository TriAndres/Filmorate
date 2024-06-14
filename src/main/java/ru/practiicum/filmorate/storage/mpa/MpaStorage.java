package ru.practiicum.filmorate.storage.mpa;

import ru.practiicum.filmorate.model.Mpa;

import java.util.Map;
import java.util.Optional;

public interface MpaStorage {
    Map<Integer, Mpa> getAllMpa();
    Optional<Mpa> findMpaById(Integer id);
}
