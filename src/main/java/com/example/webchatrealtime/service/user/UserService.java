package com.example.webchatrealtime.service.user;

import com.example.webchatrealtime.dto.response.BaseResponse;
import com.example.webchatrealtime.dto.user.RegisterDTO;
import com.example.webchatrealtime.dto.user.ResetPasswordDTO;

public interface UserService {
    RegisterDTO registerNewUser(RegisterDTO registerDTO);

    boolean checkUserNameExisted(String username);

    boolean checkEmailExisted(String email);

    String sendOtpToVerifyEmail(String email);

    RegisterDTO verifyEmailToRegisterUser(RegisterDTO registerDTO, String otp);

    void resetPassword(String email, ResetPasswordDTO resetPasswordDTO);
}
