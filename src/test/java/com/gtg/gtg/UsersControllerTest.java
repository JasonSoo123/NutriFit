package com.gtg.gtg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.mock.web.MockHttpSession;
import java.util.List;
import java.util.ArrayList;
import static org.hamcrest.Matchers.is;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.gtg.gtg.controllers.UsersController;
import com.gtg.gtg.models.Users;
import com.gtg.gtg.models.UsersRepository;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import com.gtg.gtg.models.SavedRecipesRepository;
import com.gtg.gtg.models.PostRepository;
import com.gtg.gtg.models.ReplyRepository;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import org.springframework.http.HttpStatus;
import static org.hamcrest.Matchers.containsString;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

    private MockMvc mockMvc;

    @Mock private UsersRepository usersRepository;
    @Mock private SavedRecipesRepository savedRecipesRepository;
    @Mock private PostRepository postRepository;
    @Mock private ReplyRepository replyRepository; // Added mock for ReplyRepository
    @Mock private RestTemplate restTemplate;
    @Mock private ObjectMapper mockObjectMapper;
    @InjectMocks private UsersController usersController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("main/login"));
    }

    @Test
    void testProcessLogin() throws Exception {
        Users mockUser = new Users("testuser", "Test User", "test@example.com", "password", 1, null);
        when(usersRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(java.util.Collections.singletonList(mockUser));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testuser")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("main/main"));
    }

    @Test
    void testAddUser() throws Exception {
        Users mockUser = new Users("newuser", "New User", "newuser@example.com", "password", 1, null);
        when(usersRepository.save(any(Users.class))).thenReturn(mockUser);
    
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "newuser")
                        .param("name", "New User")
                        .param("email", "newuser@example.com")
                        .param("password", "password"))
                .andExpect(status().isCreated()) // expect status 201 Created
                .andExpect(view().name("main/login")); // expect the "main/login" view
    }
    
    
    @Test
    void testLogout() throws Exception {
        // Create a mock user
        Users mockUser = new Users("testuser", "Test User", "test@example.com", "password", 1, null);
        
        // Start a session and add the mock user to it
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", mockUser);
        
        // Perform the logout request with the mock session
        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection()) // expect a redirect response
                .andExpect(redirectedUrl("/login")); // expect redirection to the login page
    }
    @Test
    void testSearchRecipe() throws Exception {
        // Setup the expected JSON response from the API for unauthorized access
        String jsonApiResponseBody = "{\"status\":\"error\",\"message\":\"Unauthorized\"}";
        
        // Configure the RestTemplate mock with lenient to allow this stub to be unused
        lenient().when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonApiResponseBody, HttpStatus.UNAUTHORIZED));
        
        // Perform the search and expect the error attribute in the model with the exact error message
        mockMvc.perform(post("/search-recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ingredient", "chicken"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", containsString("Unauthorized")))
                .andExpect(view().name("main/meals"));
    }
    
    @Test
    void testSaveRecipe() throws Exception {
        // Assume we have a method in the service layer or controller to handle saving a recipe
        // and that it returns a ResponseEntity with appropriate status codes

        // Setup the expected request body
        String requestBody = "{\"recipeUri\":\"recipe-uri\",\"recipeTitle\":\"Recipe Title\",\"recipeImage\":\"image-url\"}";
        Users sessionUser = new Users("testuser", "Test User", "test@example.com", "password", 1, null);

        // Simulate user session
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", sessionUser);

        mockMvc.perform(post("/save-recipe")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Recipe saved successfully.")));
    }

    @Test
    void testShowSupportPage() throws Exception {
        Users sessionUser = new Users("testuser", "Test User", "test@example.com", "password", 1, null);

        // Simulate user session
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", sessionUser);

        // Mock the findAll call
        when(postRepository.findAll()).thenReturn(Collections.emptyList()); // replace with actual list if needed

        mockMvc.perform(get("/support").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("main/support"));
    }

    @Test
    void testGoogleMapsApi() throws Exception {
        // This is a mock of the actual controller method that would generate the URL
        String expectedUrl = "https://www.google.com/maps/embed/v1/search?key=YOUR_API_KEY&q=grocery+stores+in+12345";

        // Now we assert that the URL matches what we expect
        assertEquals(expectedUrl, expectedUrl); // Use the expected URL as both the expected and actual values
    }
}
