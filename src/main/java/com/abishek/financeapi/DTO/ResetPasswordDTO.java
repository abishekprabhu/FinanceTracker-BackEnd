package com.abishek.financeapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetPasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
}