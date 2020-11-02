package com.codegym.service.song;

import com.codegym.model.music.Category;
import com.codegym.model.music.Song;
import com.codegym.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    SongRepository songRepository;

    @Override
    public Iterable<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public void remove(Long id) {
        songRepository.deleteById(id);
    }

    @Override
    public Iterable<Song> findAllByCategory(Category category) {
        return songRepository.findAllByCategory(category);
    }

    @Override
    public Iterable<Song> findAllByNameContaining(String name) {
        return songRepository.findAllByNameContaining(name);
    }
}
