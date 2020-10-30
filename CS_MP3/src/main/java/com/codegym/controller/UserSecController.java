package com.codegym.controller;


import com.codegym.model.AppRole;
import com.codegym.model.AppUser;
import com.codegym.service.approle.AppRoleService;
import com.codegym.service.appuser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserSecController {

    @Autowired
    AppUserService appUserService;

    @Autowired
    AppRoleService appRoleService;

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

    @ModelAttribute("roles")
    public Iterable<AppRole> roles(){
        return appRoleService.findAll();
    }


    @GetMapping("/edit")
    public String editUsuer(){
        return "edituser";
    }

    @GetMapping("/userprofile")
    public String getUserProfile(Model model){
        AppUser appUser = appUserService.findByUsername(getPrincipal());
        model.addAttribute("user", appUser);
        return "userprofile";
    }
    @GetMapping("/user")
    public String getHome(){
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String register(AppUser appUser){
        appUserService.save(appUser);
        return "login";
    }

    @GetMapping("")
    public String Home(){
        return "home";
    }

    @GetMapping("/admin")
    public String adminHome(Model model){
        return "admin";
    }

}
