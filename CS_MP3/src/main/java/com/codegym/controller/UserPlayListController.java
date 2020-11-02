package com.codegym.controller;

import com.codegym.model.music.Category;
import com.codegym.model.music.PlayList;
import com.codegym.model.music.Song;
import com.codegym.service.appuser.AppUserService;
import com.codegym.service.category.CategoryService;
import com.codegym.service.playlist.PlayListService;
import com.codegym.service.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserPlayListController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SongService songService;
    @Autowired
    private PlayListService playListService;
    @Autowired
    private AppUserService appUserService;
    @ModelAttribute("songs")
    public Iterable<Song> getSongs(){
        return songService.findAll();
    }
    @ModelAttribute("playlists")
    public Iterable<PlayList> getPlaylist(){
        return playListService.findAll();
    }
    @GetMapping("/{idUser}")
    public String formUserCategories(@RequestParam("s") Optional<String> a,Model model, @PathVariable("idUser") Long id){
        Iterable<Song> songs = null;
        if(a.isPresent()){
            songs= songService.findAllByNameContaining(a.get());
        }
        model.addAttribute("listSong",songs);
        model.addAttribute("list", categoryService.findAll());
        model.addAttribute("user",appUserService.findById(id).get());
        model.addAttribute("playlist",playListService.findAll());
        return "viewUser/list";
    }
    @GetMapping("/{idUser}/categories/detail/{idCate}")
    public String viewCategories(@RequestParam("s") Optional<String> a,@PathVariable("idUser") Long idUser,Model model,@PathVariable("idCate") Long idCate){
        Iterable<Song> listSongs = null;
        if(a.isPresent()){
            listSongs= songService.findAllByNameContaining(a.get());
        }
        model.addAttribute("listSongSearch",listSongs);
        model.addAttribute("user",appUserService.findById(idUser).get());
        Optional<Category> categoryOptional = categoryService.findById(idCate);
        Iterable<Song> songs = songService.findAllByCategory(categoryOptional.get());
        model.addAttribute("listCategories",categoryOptional.get());
        model.addAttribute("playlist",playListService.findAll());
        model.addAttribute("listSong", songs);
        return "viewUser/detail";
    }
    @GetMapping("/{idUser}/createPlaylist")
    public String viewCreatePlaylist(@RequestParam("s") Optional<String> a,@PathVariable("idUser") Long idUser,Model model){
        Iterable<Song> songs = null;
        if(a.isPresent()){
            songs= songService.findAllByNameContaining(a.get());
        }
        model.addAttribute("listSong",songs);
        model.addAttribute("user",appUserService.findById(idUser).get());
        model.addAttribute("playlist",new PlayList());
        model.addAttribute("playlistBefore",playListService.findAll());
        return "viewUser/playlist";
    }
    @PostMapping("/{idUser}/createPlaylist")
    public String createPlaylist(@PathVariable("idUser") Long idUser, Model model, @ModelAttribute PlayList playList){
        model.addAttribute("user",appUserService.findById(idUser).get());
        playList.setAppUser(appUserService.findById(idUser).get());
        playListService.save(playList);
        return "redirect:/user/{idUser}";
    }
    @GetMapping("/{idUser}/addSongToPlaylist/{idSong}")
    public String showFormAddSongInPlaylist(@RequestParam("s") Optional<String> a,@PathVariable("idUser") Long idUser, Model model, @PathVariable("idSong") Long idSong){
        Iterable<Song> songs = null;
        if(a.isPresent()){
            songs= songService.findAllByNameContaining(a.get());
        }
        model.addAttribute("listSong",songs);
        model.addAttribute("user",appUserService.findById(idUser).get());
        model.addAttribute("song",songService.findById(idSong).get());
        model.addAttribute("playlist",new PlayList());
        model.addAttribute("playlistBefore",playListService.findAll());
        return "viewUser/addSongToPlaylist";
    }
    @PostMapping("/{idUser}/addSongToPlaylist/{idSong}")
    public String createSongToPlaylist(@PathVariable("idUser") Long idUser, Model model,@ModelAttribute("playlist") PlayList playList,@PathVariable("idSong") Long idSong){
        model.addAttribute("user",appUserService.findById(idUser).get());
        PlayList playList1 = playListService.findById(playList.getId()).get();
        playList1.setAppUser(appUserService.findById(idUser).get());
        playList1.getSongs().add(songService.findById(idSong).get());
        playListService.save(playList1);
        return "redirect:/user/{idUser}";
    }
    @GetMapping("/{idUser}/playlist/{idPl}")
    public String showFormPlaylist(@RequestParam("s") Optional<String> a,@PathVariable("idUser") Long idUser, Model model, @PathVariable("idPl") Long idPl){
        Iterable<Song> songs = null;
        if(a.isPresent()){
            songs= songService.findAllByNameContaining(a.get());
        }
        model.addAttribute("listSong",songs);
        model.addAttribute("user",appUserService.findById(idUser).get());
        model.addAttribute("playlist",playListService.findById(idPl).get());
        model.addAttribute("playlistBefore",playListService.findAll());
        return "viewUser/detailPlaylist";
    }
}
