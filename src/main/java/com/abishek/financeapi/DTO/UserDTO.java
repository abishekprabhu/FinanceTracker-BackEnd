package com.abishek.financeapi.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

	private String name;

	private String email;
	
	private String mobile;
	
	private String password;
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String confirmPassword; //Only for checking purpose 

}
