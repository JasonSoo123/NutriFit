package com.gtg.gtg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


import com.gtg.gtg.models.Users;
import com.gtg.gtg.models.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.util.Map;
import java.sql.Date;

@Controller
public class UsersController {

    @Autowired
   private UsersRepository UsersRepo;

   @GetMapping("/")
   public RedirectView process(){
    return new RedirectView("login");
   }

    @GetMapping("/login")
    public String getLoginPage() {
        return "main/login";
    }
   

    @GetMapping("/add")
    public String getSignUpPage(){
        return "main/signup";
    }
    @PostMapping("/add")
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
    public String processLogin(@RequestParam Map<String, String> userMap, HttpServletRequest request, Model model) {
        String username = userMap.get("username");
        String password = userMap.get("password");
    
        List<Users> getUser = UsersRepo.findByUsernameAndPassword(username, password);
    
        if (!getUser.isEmpty()){
            Users user = getUser.get(0); // Assuming username & password combination is unique
            request.getSession().setAttribute("session_user", user); // Store user in session
            model.addAttribute("user", user);
            return "main/main"; // Change to the path of your protected page
        } else {
            return "main/login";        
        }        
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "main/login"; // Adjust the path as necessary
    }

}
