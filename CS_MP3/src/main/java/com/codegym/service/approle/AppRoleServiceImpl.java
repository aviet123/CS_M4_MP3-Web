package com.codegym.service.approle;

import com.codegym.model.user.AppRole;
import com.codegym.repository.AppRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppRoleServiceImpl implements AppRoleService {

    @Autowired
    AppRoleRepository appRoleRepository;

    @Override
    public Iterable<AppRole> findAll() {
        return appRoleRepository.findAll();
    }

    @Override
    public Optional<AppRole> findById(Long id) {
        return appRoleRepository.findById(id);
    }

    @Override
    public AppRole save(AppRole appRole) {
        return null;
    }

    @Override
    public void remove(Long id) {
    }
}
