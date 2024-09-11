package com.abishek.financeapi.DTO;

import lombok.Data;

@Data
public class UserDTO {
	String name;
	String email;
	String mobile;
	String password;
	String confirmPassword; //Only for checking purpose 

}
