package com.example.webchatrealtime.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginReponseDTO {
    private String username;
    private String token;
    private String refreshToken;
}
