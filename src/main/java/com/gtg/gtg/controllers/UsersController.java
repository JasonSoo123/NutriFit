package com.gtg.gtg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gtg.gtg.models.Users;
import com.gtg.gtg.models.UsersRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UsersController {

    @Autowired
   private UsersRepository UsersRepo;

    @GetMapping("/")
    public String getHomePage(){
        return "main/login";
    }
    
}
