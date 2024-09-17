package com.abishek.financeapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
	
	private String name;
	
	private String email;
	
	private String mobile;
	
	private String password;
	
	private String confirmPassword; //Only for checking purpose 

}
