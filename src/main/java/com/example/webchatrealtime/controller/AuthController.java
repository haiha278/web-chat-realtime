package com.example.webchatrealtime.controller;

import com.example.webchatrealtime.common.CommonString;
import com.example.webchatrealtime.dto.RegisterDTO;
import com.example.webchatrealtime.dto.response.BaseResponse;
import com.example.webchatrealtime.service.user.UserService;
import com.example.webchatrealtime.utils.Validate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {
    private Validate validate;
    private UserService userService;

    @Autowired
    public AuthController(Validate validate, UserService userService) {
        this.validate = validate;
        this.userService = userService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<BaseResponse> addNewUser(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = validate.validate(bindingResult);
            return new ResponseEntity<>(new BaseResponse(CommonString.INVALID_INPUT, HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        } else {
            if (userService.checkUserNameExisted(registerDTO.getUsername())) {
                return new ResponseEntity<>(new BaseResponse<>("Tên đăng nhập đã tồn tại", 2), HttpStatus.BAD_REQUEST);
            }

            if (userService.checkEmailExisted(registerDTO.getEmail())) {
                return new ResponseEntity<>(new BaseResponse<>("Email đã tồn tại", 3), HttpStatus.BAD_REQUEST);
            }
            String messageSentOTP = userService.sendOtpToVerifyEmail(registerDTO);
            return new ResponseEntity<>(new BaseResponse<>(messageSentOTP, HttpStatus.OK.value()), HttpStatus.OK);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<BaseResponse> verifyEmail(@RequestBody RegisterDTO registerDTO, @RequestParam String otp) {
        RegisterDTO registerUser = userService.verifyEmail(registerDTO, otp);
        if (registerUser != null) {
            return new ResponseEntity<>(new BaseResponse("Xác thực tài khoản thành công", HttpStatus.CREATED.value(), registerUser), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new BaseResponse("Xác thực tài khoản thất bại", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }
}
