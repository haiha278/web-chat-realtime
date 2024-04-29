package com.example.webchatrealtime.service.user;

import com.example.webchatrealtime.dto.RegisterDTO;

public interface UserService {
    RegisterDTO registerNewUser(RegisterDTO registerDTO);

    boolean checkUserNameExisted(String username);

    boolean checkEmailExisted(String email);

    String sendOtpToVerifyEmail(RegisterDTO registerDTO);

    RegisterDTO verifyEmail(RegisterDTO registerDTO, String otp);
}
