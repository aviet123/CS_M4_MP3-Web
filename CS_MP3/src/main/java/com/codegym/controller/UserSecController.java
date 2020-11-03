package com.codegym.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.codegym.model.user.AppRole;
import com.codegym.model.user.AppUser;
import com.codegym.service.approle.AppRoleService;
import com.codegym.service.appuser.AppUserService;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
public class UserSecController {

    final String CLOUDINARY_URL = "cloudinary://292468957674773:lcLLj26C4VX82SHtbJrjZkcwhas@dos9lacv4";

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

    @ModelAttribute("user")
    public AppUser getUser(){
        return appUserService.findByUsername(getPrincipal());
    }


    @GetMapping("/edit")
    public String editUser(Model model){
        AppUser user = appUserService.findByUsername(getPrincipal());
        model.addAttribute("user", user);
        return "user/edituser";
    }

    @PostMapping("/edit")
    public String editUser(AppUser user){
        MultipartFile multipartFile = user.getAvatar();
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        try{
            File imgFile = File.createTempFile("test", multipartFile.getOriginalFilename()).toPath().toFile();
            multipartFile.transferTo(imgFile);

            Map responseImg = cloudinary.uploader().upload(imgFile, ObjectUtils.emptyMap());
            JSONObject jsonObject = new JSONObject(responseImg);
            String urlImg = jsonObject.getString("url");        //link img
            user.setImg(urlImg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        appUserService.save(user);
        return "redirect:/userprofile";
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable("id") Long id){
        appUserService.remove(id);
        return "redirect:/admin";
    }

    @GetMapping("/userprofile")
    public String getUserProfile(Model model){
        AppUser appUser = appUserService.findByUsername(getPrincipal());
        model.addAttribute("user", appUser);
        return "user/userprofile";
    }


    @GetMapping("/user")
    public String getHome(){
        return "home";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "user/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("userRegister", new AppUser());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegister") AppUser appUser, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "user/register";
        }else {
            appUserService.save(appUser);
            return "user/login";
        }
    }

    @GetMapping("")
    public String Home(){
        return "user/login";
    }

}
