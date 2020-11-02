package com.codegym.repository;

import com.codegym.model.music.Category;
import com.codegym.model.music.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
    Iterable<Song> findAllByCategory(Category category);
    Iterable<Song> findAllByNameContaining(String name);
}
