package com.abishek.financeapi.Service.User;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abishek.financeapi.DTO.ResetPasswordDTO;
import com.abishek.financeapi.DTO.UserDTO;
import com.abishek.financeapi.DTO.UserProfileDTO;
import com.abishek.financeapi.Exception.CustomException;
import com.abishek.financeapi.Exception.DuplicateUserException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	
	@Override
	public User registerUser(UserDTO userDTO){
		Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
		
		if(existingUser.isPresent())
			throw new DuplicateUserException("User with email " + userDTO.getEmail() + " already exists");
		
		if(!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
			throw new CustomException("Password Do not Match!");
		
		//save User
		User user = new User();
		user.setEmail(userDTO.getEmail());
		user.setMobile(userDTO.getMobile());
		user.setName(userDTO.getName());
		user.setPassword(userDTO.getPassword());	
			
		return userRepository.save(user);
	}

	@Override
	public User loginUser(UserDTO userDTO) {
		
		User user = userRepository
				.findByEmail(userDTO.getEmail())
				.orElseThrow(()->new UserNotFoundException("User with Email this  " + userDTO.getEmail() + " is not found"));
		if(!user.getPassword().equals(userDTO.getPassword()))
			throw new CustomException("Invalid Password Or Wrong Password");	
		
		return user;
	}
	
    // Reset Password
	@Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + resetPasswordDTO.getEmail()));

        // Check if the old password matches
        if (!user.getPassword().equals(resetPasswordDTO.getOldPassword())) {
            throw new CustomException("Old password does not match");
        }

        // Update with new password
        user.setPassword(resetPasswordDTO.getNewPassword());
        userRepository.save(user);
    }
	
	@Override
    public void uploadProfilePicture(UserProfileDTO userProfileDTO) throws IOException {
        User user = userRepository.findById(userProfileDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Convert MultipartFile to byte[] and set as profile picture
        MultipartFile profilePicture = userProfileDTO.getProfilePicture();
        if (profilePicture != null && !profilePicture.isEmpty()) {
            user.setProfilePicture(profilePicture.getBytes());
        }

        userRepository.save(user);
    }
	
	@Override
    public byte[] getProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getProfilePicture();
    }

}
