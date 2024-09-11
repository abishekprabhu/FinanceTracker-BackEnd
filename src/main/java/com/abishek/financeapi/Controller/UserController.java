package com.abishek.financeapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.UserDTO;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Service.User.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
		User createdUser = userService.registerUser(userDTO);
		if(createdUser != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);			
		}else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> LoginUser(@RequestBody UserDTO userDTO){
		User login = userService.loginUser(userDTO);
		if(login != null) {
			return ResponseEntity.status(HttpStatus.OK).body(login);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	
}
