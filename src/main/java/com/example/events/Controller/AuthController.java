package com.example.events.Controller;

import com.example.events.DTOs.*;
import com.example.events.Exceptions.AppException;
import com.example.events.Service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

//   @PostMapping("/login")
//   public ResponseEntity<?> login(@RequestBody @Valid CredentialsDto credentialsDto) {
//       try {
//           UserDto userDto = userService.login(credentialsDto);
//           return ResponseEntity.ok(userDto);
//       } catch (AppException e) {
//           HttpStatus status = e.getStatus();
//           String errorMessage = e.getMessage();
//           return ResponseEntity.status(status).body(errorMessage);
//       }
//   }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        try {
            UserDto userDto = userService.login(credentialsDto);

            String token = userService.generateToken(userDto);

            return ResponseEntity.ok(new LoginResponse(userDto, token));
        } catch (AppException e) {
            HttpStatus status = e.getStatus();
            String errorMessage = e.getMessage();
            return ResponseEntity.status(status).body(errorMessage);
        }
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logOut();
        return "You are logged out";
    }
    @PostMapping("/admin")
    public ResponseEntity<UserDto> createAdminUser(@RequestBody RegistrationRequest user) {

            UserDto createdUser = userService.registerUserRole(user, "ADMIN");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

    }

    @PostMapping("/organizer")
    public ResponseEntity<?> createOrganizerUser(@RequestBody RegistrationRequest user) {
        if (userService.isAdminActive()) {
            UserDto createdUser = userService.registerUserRole(user, "ORGANIZER");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } else {
            return ResponseEntity.badRequest().body(("Only admin users can create organizer users"));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationRequest user) {
        logger.info("Registering user with the following details:");
        logger.info("First Name: {}", user.getFirstName());
        logger.info("Last Name: {}", user.getLastName());
        logger.info("Email Address: {}", user.getEmailAddress());
        logger.info("Login: {}", user.getUsername());
        logger.info("Password: {}", user.getPassword());

        UserDto createdUser = userService.registerUser(user);
       //createdUser.setToken(userAuthenticationProvider.createToken(createdUser.getLogin()));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody @Valid UserUpdateRequest updateRequest) {
        try {
            UserDto updatedUser = userService.updateUser(username, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return userDto;
    }

    @GetMapping("/allUsers")
    public List<UserDto> getAllUsers() {
        return  userService.getAllUsers();
    }

}
