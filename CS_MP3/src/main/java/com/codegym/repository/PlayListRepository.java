package com.codegym.repository;

import com.codegym.model.music.PlayList;
import com.codegym.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    Iterable<AppUser> findAllByAppUser(AppUser appUser);
}
