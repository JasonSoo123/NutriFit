package com.gtg.gtg.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtg.gtg.RecipeResult; // Ensure RecipeResult is in the com.gtg.gtg package
import com.gtg.gtg.models.SavedRecipe;
import com.gtg.gtg.models.SavedRecipesRepository;
import com.gtg.gtg.models.Users;
import com.gtg.gtg.models.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UsersController {

    @Autowired
   private UsersRepository UsersRepo;

    @Autowired
   private SavedRecipesRepository savedRecipesRepo;

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
        Users user = new Users(username, name, email, password, 1, birthday);
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

        if (!getUser.isEmpty()) {
            Users user = getUser.get(0); // Assuming username & password combination is unique
            request.getSession().setAttribute("session_user", user); // Store user in session

            if (user.getUsertype() == 0) {
                // If the user is an admin, fetch all users from the database and add to the model
                List<Users> users = UsersRepo.findByUsertype(1);
                model.addAttribute("adminUser", user); // Add the admin user to the model
                model.addAttribute("users", users); // Add the list of all users to the model

                return "main/admin";
            } else {
                model.addAttribute("user", user);
                return "main/main"; // Change to the path of your protected page
            }
        } else {
            //No user mathced password/username
            model.addAttribute("loginError", "Incorrect Username or Password");
            return "main/login";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("selectedUsers") List<String> selectedUsers,
     @RequestParam("adminUsername") String adminUsername, Model model) {

        if (selectedUsers != null) {
            for (String username : selectedUsers) {
                System.out.println("Deleting user: " + username);
                List<Users> getUsers = UsersRepo.findByUsername(username);
                Users user = getUsers.get(0);
                UsersRepo.delete(user);
            }
        }
        
        // model to reload admin page.
        List<Users> getUsers = UsersRepo.findByUsername(adminUsername);
        Users user = getUsers.get(0);
        model.addAttribute("adminUser", user);
        List<Users> users = UsersRepo.findByUsertype(1);
        model.addAttribute("users", users);

        return "main/admin"; // Redirect to admin page after deletion
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "main/login"; // Adjust the path as necessary
    }

    @GetMapping("/meals")
    public String showMealsPage() {
        return "main/meals"; // Assuming the template is directly under src/main/resources/templates/
    }

    @GetMapping("/progress")
    public String showProgressPage() {
        return "main/progress"; // Path to the progress template
    }

    @GetMapping("/support")
    public String showSupportPage() {
        return "main/support"; // Path to the support template
    }

    @GetMapping("/shopping")
    public String showShoppingPage() {
        return "main/shopping"; // Path to the shopping template
    }

    @Value("${edamam.api.id}")
    private String apiId;

    @Value("${edamam.api.key}")
    private String apiKey;

    @PostMapping("/search-recipe")
    public String searchRecipe(@RequestParam String ingredient,
                               @RequestParam(required = false) List<String> diet,
                               @RequestParam(required = false) List<String> health,
                               Model model) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.edamam.com/api/recipes/v2")
                .queryParam("type", "public")
                .queryParam("q", "") // Placeholder for the ingredient, will be set after encoding
                .queryParam("app_id", apiId)
                .queryParam("app_key", apiKey);
    
        // Join diet and health lists with commas if they are not empty
        if (diet != null && !diet.isEmpty()) {
            String diets = String.join(",", diet);
            builder.queryParam("diet", diets);
        }
        if (health != null && !health.isEmpty()) {
            String healthLabels = String.join(",", health);
            builder.queryParam("health", healthLabels);
        }
    
        try {
            String encodedIngredient = URLEncoder.encode(ingredient.trim(), "UTF-8");
            builder.replaceQueryParam("q", encodedIngredient); // Replace placeholder with encoded value
            System.out.println("Encoded Ingredient: " + encodedIngredient); // Debug log
        } catch (UnsupportedEncodingException e) {
            model.addAttribute("error", "Error encoding ingredients");
            return "main/meals";
        }
    
        String urlTemplate = builder.build().encode().toUriString();
        System.out.println("URL Template: " + urlTemplate); // Debug log
    
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(urlTemplate, String.class);
    
            if(response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                RecipeResult recipeResult = mapper.readValue(response.getBody(), RecipeResult.class);
                model.addAttribute("recipes", recipeResult.getHits());
            } else {
                model.addAttribute("error", "Failed to fetch recipes: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "API request error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            model.addAttribute("error", "Error parsing recipe data");
            e.printStackTrace();
        }
    
        return "main/meals";
    }

    @PostMapping("/save-recipe")
public ResponseEntity<String> saveRecipe(HttpServletRequest request, @RequestBody Map<String, String> payload) {
    Users sessionUser = (Users)request.getSession().getAttribute("session_user");
    if (sessionUser == null) {
        return new ResponseEntity<>("User must be logged in to save recipes.", HttpStatus.FORBIDDEN);
    }
    
    String recipeUri = payload.get("recipeUri");
    if (recipeUri == null || recipeUri.isEmpty()) {
        return new ResponseEntity<>("Recipe URI is required.", HttpStatus.BAD_REQUEST);
    }

    // Debugging: log the user ID and recipe URI
    System.out.println("Checking for existing recipe. User ID: " + sessionUser.getUid() + ", Recipe URI: " + recipeUri);
    
    // boolean recipeExists = savedRecipesRepo.existsByUserIdAndRecipeUri(sessionUser.getUid(), recipeUri);
    // if (recipeExists) {
    //     System.out.println("Recipe already saved. User ID: " + sessionUser.getUid() + ", Recipe URI: " + recipeUri);
    //     return new ResponseEntity<>("Recipe already saved.", HttpStatus.BAD_REQUEST);
    // }

    // Debugging: log the actual save attempt
    System.out.println("Saving recipe. User ID: " + sessionUser.getUid() + ", Recipe URI: " + recipeUri);

    SavedRecipe savedRecipe = new SavedRecipe();
    savedRecipe.setUserId(sessionUser.getUid());
    savedRecipe.setRecipeUri(recipeUri);
    savedRecipesRepo.save(savedRecipe);

    System.out.println("Recipe saved successfully. User ID: " + sessionUser.getUid() + ", Recipe URI: " + recipeUri);
    return new ResponseEntity<>("Recipe saved successfully.", HttpStatus.CREATED);
    }


    @RequestMapping("/saved")
    public String showSavedRecipes(HttpServletRequest request, Model model) {
        Users sessionUser = (Users)request.getSession().getAttribute("session_user");
        if (sessionUser == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }
    
        List<SavedRecipe> savedRecipes = savedRecipesRepo.findByUserId(sessionUser.getUid());
        if (savedRecipes.isEmpty()) {
            System.out.println("No saved recipes found for user ID: " + sessionUser.getUid());
        } else {
            System.out.println("Saved recipes found for user ID: " + sessionUser.getUid() + ", Count: " + savedRecipes.size());
        }
    
        model.addAttribute("savedRecipes", savedRecipes);
        return "main/saved"; 
    }

    // Sample DTO class for recipe details
    public class RecipeDetailsDto {
        private String uri;
        private String label;
        private String imageUrl;
        // Constructor, getters, setters
    }

    // Sample method to fetch recipe details
    public RecipeDetailsDto getRecipeDetailsByUri(String uri) {
        // Fetch and return recipe details based on the URI
        // This is pseudocode and needs to be replaced with your actual data fetching logic
        return new RecipeDetailsDto();
    }

    
}
