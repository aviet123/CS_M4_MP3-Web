package com.codegym.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.codegym.model.music.Category;
import com.codegym.model.user.AppUser;
import com.codegym.service.appuser.AppUserService;
import com.codegym.service.category.CategoryService;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    final String CLOUDINARY_URL = "cloudinary://292468957674773:lcLLj26C4VX82SHtbJrjZkcwhas@dos9lacv4";


    @Autowired
    private CategoryService iCategoryService;


    @Autowired
    private AppUserService appUserService;


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


    @ModelAttribute("user")
    public AppUser user(){
        return appUserService.findByUsername(getPrincipal());
    }

    @GetMapping
    public ModelAndView showCategory() {
        Iterable<Category> categories = iCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("category/album-list");
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }
    @GetMapping("/create")
    public ModelAndView showCreateCategory() {
        ModelAndView modelAndView = new ModelAndView("category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveCategory(@ModelAttribute("category") Category category) {
        MultipartFile multipartFile = category.getAvatar();
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        try{
            File imgFile = File.createTempFile("test", multipartFile.getOriginalFilename()).toPath().toFile();
            multipartFile.transferTo(imgFile);

            Map responseImg = cloudinary.uploader().upload(imgFile, ObjectUtils.emptyMap());
            JSONObject jsonObject = new JSONObject(responseImg);
            String urlImg = jsonObject.getString("url");        //link img
            category.setImg(urlImg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        iCategoryService.save(category);
        return new ModelAndView("category/create", "category", category);
    }
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id)  {
            Category category = iCategoryService.findById(id).get();
            ModelAndView modelAndView = new ModelAndView("category/edit");
            modelAndView.addObject("category",category);
            return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView updateCategory(@ModelAttribute("category") Category category) {
        MultipartFile multipartFile = category.getAvatar();
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        try{
            File imgFile = File.createTempFile("test", multipartFile.getOriginalFilename()).toPath().toFile();
            multipartFile.transferTo(imgFile);

            Map responseImg = cloudinary.uploader().upload(imgFile, ObjectUtils.emptyMap());
            JSONObject jsonObject = new JSONObject(responseImg);
            String urlImg = jsonObject.getString("url");        //link img
            category.setImg(urlImg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        iCategoryService.save(category);
        return new ModelAndView("redirect:/categories", "category", new Category());

    }
    //    @GetMapping("/delete/{id}")
//    public ModelAndView showDeleteForm(@PathVariable Long id) {
//       Optional<Category> category = iCategoryService.findOne(id);
//        if(category != null) {
//            ModelAndView modelAndView = new ModelAndView("/category/delete");
//            modelAndView.addObject("category", category);
//            return modelAndView;
//
//        }else {
////            ModelAndView modelAndView = new ModelAndView("/error.404");
//            ModelAndView modelAndView = new ModelAndView("/category/loi");
//            return modelAndView;
//        }
//    }
    @GetMapping("/delete/{id}")
    public String showFormDelete(@PathVariable Long id){
        Optional<Category> product = iCategoryService.findById(id);
        if (product.isPresent()){
            iCategoryService.remove(id);
        }else
            return "error";
        return "redirect:/categories";
    }
//    @PostMapping("/delete")
//    public String deleteCategory(@ModelAttribute("category") Category category) {
//        iCategoryService.remove(category.getId());
//        return "redirect:categories";
//    }
//    public ModelAndView showInputNotAcceptable() {
//        return new ModelAndView("/category/exption");
//    }

}
