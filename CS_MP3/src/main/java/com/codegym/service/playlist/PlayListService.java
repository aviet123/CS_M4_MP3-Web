package com.codegym.service.playlist;

import com.codegym.model.music.PlayList;
import com.codegym.model.user.AppUser;
import com.codegym.service.IService;

public interface PlayListService extends IService<PlayList> {
    Iterable<AppUser> findAllByAppUser(AppUser appUser);
}
