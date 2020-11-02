package com.codegym.service.appuser;

import com.codegym.model.user.AppUser;
import com.codegym.service.IService;

public interface AppUserService extends IService<AppUser> {

    AppUser findByUsername(String username);
    AppUser save(AppUser appUser);
}
