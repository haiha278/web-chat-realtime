package com.example.webchatrealtime.service.user;

import com.example.webchatrealtime.dto.RegisterDTO;
import com.example.webchatrealtime.entities.User;
import com.example.webchatrealtime.mapper.Mapper;
import com.example.webchatrealtime.repository.UserRepository;
import com.example.webchatrealtime.utils.EmailUtils;
import com.example.webchatrealtime.utils.OtpUtils;
import com.example.webchatrealtime.utils.Validate;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String sendOtpToVerifyEmail(RegisterDTO registerDTO) {
        String otp = otpUtils.generate();
        try {
            emailUtils.sendVerifyEmail(registerDTO.getEmail(), otp);
            storageOtp.put(registerDTO.getEmail(), otp);
            scheduledExecutorService.schedule(() -> {
                storageOtp.remove(registerDTO.getEmail());
            }, 2, TimeUnit.MINUTES);
            return "OTP đã gửi đến email";
        } catch (MessagingException e) {
            return "Không thể gửi OTP";
        }
    }

    @Override
    public RegisterDTO verifyEmail(RegisterDTO registerDTO, String otp) {
        if (validate.validateOtp(storageOtp, registerDTO.getEmail(), otp)) {
            storageOtp.remove(registerDTO.getEmail());
            return registerNewUser(registerDTO);
        }
        return null;
    }
}
