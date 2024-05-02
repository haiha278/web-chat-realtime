package com.example.webchatrealtime.service.user;

import com.example.webchatrealtime.common.CommonString;
import com.example.webchatrealtime.dto.response.BaseResponse;
import com.example.webchatrealtime.dto.user.RegisterDTO;
import com.example.webchatrealtime.dto.user.ResetPasswordDTO;
import com.example.webchatrealtime.entities.User;
import com.example.webchatrealtime.exception.OtpMismatchException;
import com.example.webchatrealtime.exception.PasswordMismatchException;
import com.example.webchatrealtime.exception.UserNotFoundException;
import com.example.webchatrealtime.mapper.Mapper;
import com.example.webchatrealtime.repository.UserRepository;
import com.example.webchatrealtime.utils.EmailUtils;
import com.example.webchatrealtime.utils.OtpUtils;
import com.example.webchatrealtime.utils.Validate;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private OtpUtils otpUtils;
    private EmailUtils emailUtils;
    private Map<String, String> storageOtp = new HashMap();
    private ScheduledExecutorService scheduledExecutorService;
    private Validate validate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, OtpUtils otpUtils, EmailUtils emailUtils, ScheduledExecutorService scheduledExecutorService, Validate validate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpUtils = otpUtils;
        this.emailUtils = emailUtils;
        this.scheduledExecutorService = scheduledExecutorService;
        this.validate = validate;
    }

    @Override
    public RegisterDTO registerNewUser(RegisterDTO registerDTO) {
        User user = Mapper.mapDtoToEntity(registerDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        User savedUser = userRepository.saveAndFlush(user);

        registerDTO = Mapper.mapEntityToDTO(savedUser, RegisterDTO.class);
        return registerDTO;
    }

    @Override
    public boolean checkUserNameExisted(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public boolean checkEmailExisted(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public String sendOtpToVerifyEmail(String email) {
        String otp = otpUtils.generate();
        try {
            emailUtils.sendVerifyEmail(email, otp);
            storageOtp.put(email, otp);
            scheduledExecutorService.schedule(() -> {
                storageOtp.remove(email);
            }, 2, TimeUnit.MINUTES);
            return "OTP đã gửi đến email";
        } catch (MessagingException e) {
            return "Không thể gửi OTP";
        }
    }

    @Override
    public RegisterDTO verifyEmailToRegisterUser(RegisterDTO registerDTO, String otp) {
        if (validate.validateOtp(storageOtp, registerDTO.getEmail(), otp)) {
            storageOtp.remove(registerDTO.getEmail());
            return registerNewUser(registerDTO);
        }
        return null;
    }

    @Override
    public void resetPassword(String email, ResetPasswordDTO resetPasswordDTO) {
        if (!checkEmailExisted(email)) {
            throw new UserNotFoundException("Không tìm thấy người dùng");
        }
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
            throw new PasswordMismatchException("Mật khẩu không khớp");
        }
        if (!storageOtp.get(email).equals(resetPasswordDTO.getOtp())) {
            throw new OtpMismatchException("OTP không hợp lệ");
        } else {
            User user = userRepository.findUserByEmail(email);
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.saveAndFlush(user);
        }
    }
}
