package com.codegym.service.playlist;

import com.codegym.model.music.PlayList;
import com.codegym.model.user.AppUser;
import com.codegym.repository.PlayListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayListServiceImpl implements PlayListService {

    @Autowired
    private PlayListRepository playListRepository;

    @Override
    public Iterable<AppUser> findAllByAppUser(AppUser appUser) {
        return playListRepository.findAllByAppUser(appUser);
    }

    @Override
    public Iterable<PlayList> findAll() {
        return playListRepository.findAll();
    }

    @Override
    public Optional<PlayList> findById(Long id) {
        return playListRepository.findById(id);
    }

    @Override
    public PlayList save(PlayList playList) {
        return playListRepository.save(playList);
    }

    @Override
    public void remove(Long id) {
        playListRepository.deleteById(id);
    }
}
