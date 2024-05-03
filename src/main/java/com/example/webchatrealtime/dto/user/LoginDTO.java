package com.example.webchatrealtime.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Không được để trống !")
    private String username;
    @NotBlank(message = "Không được để trống !")
    private String password;
}
