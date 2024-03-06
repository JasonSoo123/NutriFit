package com.gtg.gtg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


import com.gtg.gtg.models.Users;
import com.gtg.gtg.models.UsersRepository;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.sql.Date;

@Controller
public class UsersController {

    @Autowired
   private UsersRepository UsersRepo;

    @GetMapping("/")
    public String getHomePage(){
        return "main/login";
    }
    
    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newUser, HttpServletResponse response) {
        // Extract user attributes from the request parameters
        String username = newUser.get("username");
        String name = newUser.get("name");
        String email = newUser.get("email");
        String password = newUser.get("password");
        Date birthday = newUser.get("birthday") != null ? Date.valueOf(newUser.get("birthday")) : null;
    
        // Create and save the new user entity
        Users user = new Users(username, name, email, password, birthday);
        UsersRepo.save(user);
    
        // Set the response status code to 201 Created
        response.setStatus(HttpServletResponse.SC_CREATED);
    
        // Redirect to a view, e.g., to list all users or to a user profile page
        return "main/login"; // Adjust the redirect as necessary for your application
    }
    
    @PostMapping("/login")
    public String processLogin(@RequestParam Map<String, String> User, HttpServletResponse response) {

        System.out.println("meow");

        List<Users> getUser = UsersRepo.findByUsernameAndPassword( User.get("name"), User.get("password"));

        if (!getUser.isEmpty()){

            response.setStatus(200);
            return "test.html";

        } else {
            return "test.html";
        }
        
    }   

}
