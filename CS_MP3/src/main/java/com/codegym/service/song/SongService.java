package com.codegym.service.song;

import com.codegym.model.music.Category;
import com.codegym.model.music.Song;
import com.codegym.service.IService;

public interface SongService extends IService<Song> {
    Iterable<Song> findAllByCategory(Category category);
    Iterable<Song> findAllByNameContaining(String name);
}
