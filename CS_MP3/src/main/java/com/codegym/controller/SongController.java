package com.codegym.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.codegym.model.music.Category;
import com.codegym.model.music.Song;
import com.codegym.model.user.AppUser;
import com.codegym.service.appuser.AppUserService;
import com.codegym.service.category.CategoryService;
import com.codegym.service.song.SongService;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/songs")
public class SongController {


    final String CLOUDINARY_URL = "cloudinary://292468957674773:lcLLj26C4VX82SHtbJrjZkcwhas@dos9lacv4";

    @Autowired
    private SongService songService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CategoryService categoryService;


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

    @ModelAttribute("categories")
    public Iterable<Category> getAllCategory(){
        return categoryService.findAll();
    }

    @ModelAttribute("user")
    public AppUser getUser(){
        return appUserService.findByUsername(getPrincipal());
    }

    @GetMapping
    public String showList(Model model){
        Iterable<Song> songs = songService.findAll();
        model.addAttribute("songs",songs);
        return "song/song-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("song",new Song());
        return "song/create";
    }

    @PostMapping("/create")
    public String saveSong(Song song){
        MultipartFile multipartFile = song.getMp3();
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        try{
            File mp3File = File.createTempFile("test", multipartFile.getOriginalFilename()).toPath().toFile();
            multipartFile.transferTo(mp3File);

            Map responseMp3 = cloudinary.uploader().upload(mp3File,ObjectUtils.asMap("resource_type", "auto"));
            JSONObject jsonObject = new JSONObject(responseMp3);
            String urlMp3 = jsonObject.getString("url");
            song.setLinkMp3(urlMp3);
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
        songService.save(song);
        return "redirect:/songs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model){
        Song song = songService.findById(id).get();
        model.addAttribute("song",song);
        return "song/edit";
    }

    @PostMapping("/edit")
    public String editSong(Song song){
        MultipartFile multipartFile = song.getMp3();
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        try{
            File mp3File = File.createTempFile("test",multipartFile.getOriginalFilename()).toPath().toFile();
            multipartFile.transferTo(mp3File);

            Map responseMp3 = cloudinary.uploader().upload(mp3File,ObjectUtils.asMap("resource_type", "auto"));
            JSONObject jsonObject = new JSONObject(responseMp3);
            String urlMp3 = jsonObject.getString("url");
            song.setLinkMp3(urlMp3);
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }
        songService.save(song);
        return "redirect:/songs";
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable("id") Long id){
        songService.remove(id);
        return "redirect:/songs";
    }
}
