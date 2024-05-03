package com.example.webchatrealtime.controller;

import com.example.webchatrealtime.common.CommonString;
import com.example.webchatrealtime.dto.response.LoginReponseDTO;
import com.example.webchatrealtime.dto.user.ForgotPasswordDTO;
import com.example.webchatrealtime.dto.user.LoginDTO;
import com.example.webchatrealtime.dto.user.RegisterDTO;
import com.example.webchatrealtime.dto.response.BaseResponse;
import com.example.webchatrealtime.dto.user.ResetPasswordDTO;
import com.example.webchatrealtime.exception.OtpMismatchException;
import com.example.webchatrealtime.exception.PasswordMismatchException;
import com.example.webchatrealtime.exception.UserNotFoundException;
import com.example.webchatrealtime.security.CustomUserDetail;
import com.example.webchatrealtime.security.JwtTokenProvider;
import com.example.webchatrealtime.service.user.UserService;
import com.example.webchatrealtime.utils.Validate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {
    private Validate validate;
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(Validate validate, UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.validate = validate;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<BaseResponse> addNewUser(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = validate.validateInput(bindingResult);
            return new ResponseEntity<>(new BaseResponse(CommonString.INVALID_INPUT, HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        } else {
            if (userService.checkUserNameExisted(registerDTO.getUsername())) {
                return new ResponseEntity<>(new BaseResponse<>("Tên đăng nhập đã tồn tại", 2), HttpStatus.BAD_REQUEST);
            }

            if (userService.checkEmailExisted(registerDTO.getEmail())) {
                return new ResponseEntity<>(new BaseResponse<>("Email đã tồn tại", 3), HttpStatus.BAD_REQUEST);
            }
            String messageSentOTP = userService.sendOtpToVerifyEmail(registerDTO.getEmail());
            return new ResponseEntity<>(new BaseResponse<>(messageSentOTP, HttpStatus.OK.value()), HttpStatus.OK);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<BaseResponse> verifyEmail(@RequestBody RegisterDTO registerDTO, @RequestParam String otp) {
        RegisterDTO registerUser = userService.verifyEmailToRegisterUser(registerDTO, otp);
        if (registerUser != null) {
            return new ResponseEntity<>(new BaseResponse("Xác thực tài khoản thành công", HttpStatus.CREATED.value(), registerUser), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new BaseResponse("Xác thực tài khoản thất bại", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        if (userService.checkEmailExisted(forgotPasswordDTO.getEmail()) == false) {
            return new ResponseEntity<>(new BaseResponse<>(CommonString.NOT_FOUND, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } else {
            String messageSentOTP = userService.sendOtpToVerifyEmail(forgotPasswordDTO.getEmail());
            return new ResponseEntity<>(new BaseResponse<>(messageSentOTP, HttpStatus.OK.value()), HttpStatus.OK);
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<BaseResponse> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult bindingResult, @RequestParam String email) {
        if (bindingResult.hasErrors()) {
            List<String> errors = validate.validateInput(bindingResult);
            return new ResponseEntity<>(new BaseResponse(CommonString.INVALID_INPUT, HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        } else {
            try {
                userService.resetPassword(email, resetPasswordDTO);
                return new ResponseEntity<>(new BaseResponse<>("Khôi phục mật khẩu thành công", HttpStatus.OK.value()), HttpStatus.OK);
            } catch (UserNotFoundException e) {
                return new ResponseEntity<>(new BaseResponse<>(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
            } catch (PasswordMismatchException e) {
                return new ResponseEntity<>(new BaseResponse<>(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
            } catch (OtpMismatchException | NullPointerException e) {
                return new ResponseEntity<>(new BaseResponse<>("OTP không hợp lệ", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = validate.validateInput(bindingResult);
            return new ResponseEntity<>(new BaseResponse("Thông tin đăng nhập không hợp lệ", HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(customUserDetail);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetail);
        LoginReponseDTO loginReponseDTO = new LoginReponseDTO(jwtTokenProvider.getUserNameFromJwt(token), token, refreshToken);
        return new ResponseEntity<>(new BaseResponse<>("Đăng nhập thành công", HttpStatus.OK.value(), loginReponseDTO), HttpStatus.OK);
    }
}
