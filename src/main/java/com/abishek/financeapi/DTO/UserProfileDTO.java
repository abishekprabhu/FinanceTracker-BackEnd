package com.abishek.financeapi.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long userId;
    
    private MultipartFile profilePicture; // to upload image as binary data
    
}	