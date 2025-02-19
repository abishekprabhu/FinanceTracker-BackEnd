package com.abishek.financeapi.Controller;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationException;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abishek.financeapi.DTO.ResetPasswordDTO;
import com.abishek.financeapi.DTO.UserDTO;
import com.abishek.financeapi.DTO.UserProfileDTO;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Service.User.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin("*")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
 //   @CacheEvict(value = "users", allEntries = true) // Clear cache when a new user is registered
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        User createdUser = userService.registerUser(userDTO);
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
    }

    @PostMapping("/login")
 //   @Cacheable(value = "users", key = "#userDTO.email") // Cache login data by email
    public ResponseEntity<?> LoginUser(@RequestBody UserDTO userDTO) {
        try {
            User login = userService.loginUser(userDTO);
            if (login != null) {
                return ResponseEntity.status(HttpStatus.OK).body(login);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SerializationException e) {
            log.error("Serialization issue: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Serialization issue");
        }
    }

    // Reset Password Endpoint
    @PostMapping("/reset-password")
  //  @CacheEvict(value = "users", key = "#resetPasswordDTO.email") // Clear specific user's cache after password reset
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return ResponseEntity.ok("Password updated successfully");
    }

    // Endpoint to upload profile picture
    @PostMapping("/{userId}/uploadProfilePicture")
 //   @CacheEvict(value = "users", key = "#userId") // Clear cache after profile picture upload
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            UserProfileDTO userProfileDTO = new UserProfileDTO(userId, profilePicture);
            userService.uploadProfilePicture(userProfileDTO);
            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
        }
    }

    // Endpoint to retrieve profile picture
    @GetMapping("/{userId}/profilePicture")
 //   @Cacheable(value = "users", key = "#userId") // Cache profile picture retrieval by userId
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long userId) {
        try {
            byte[] image = userService.getProfilePicture(userId);
            if (image != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"profilePicture.jpg\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
