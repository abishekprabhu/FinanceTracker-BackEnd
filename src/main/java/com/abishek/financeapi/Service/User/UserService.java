package com.abishek.financeapi.Service.User;

import java.io.IOException;

import com.abishek.financeapi.DTO.ResetPasswordDTO;
import com.abishek.financeapi.DTO.UserDTO;
import com.abishek.financeapi.DTO.UserProfileDTO;
import com.abishek.financeapi.Model.User;

public interface UserService {

	User registerUser(UserDTO userDTO);
	
	User loginUser(UserDTO userDTO);
	
	void resetPassword(ResetPasswordDTO resetPasswordDTO);
	
	void uploadProfilePicture(UserProfileDTO userProfileDTO) throws IOException;
	
	byte[] getProfilePicture(Long userId);
	
}
