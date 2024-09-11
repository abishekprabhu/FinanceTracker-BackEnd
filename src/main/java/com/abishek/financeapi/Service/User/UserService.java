package com.abishek.financeapi.Service.User;

import com.abishek.financeapi.DTO.UserDTO;
import com.abishek.financeapi.Model.User;

public interface UserService {

	User registerUser(UserDTO userDTO);
	User loginUser(UserDTO userDTO);
}
