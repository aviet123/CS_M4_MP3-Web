package com.codegym.controller;

import com.codegym.model.user.AppUser;
import com.codegym.service.appuser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AdminController {

    @Autowired
    AppUserService appUserService;

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @ModelAttribute("users")
    public Iterable<AppUser> getAll(){
        return appUserService.findAll();
    }

    @ModelAttribute("user")
    public AppUser user(){
        return appUserService.findByUsername(getPrincipal());
    }

    @GetMapping("/admin")
    public String adminHome(Model model){
        return "admin/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(Long id){
        appUserService.remove(id);
        return "redirect:/admin";
    }
}
