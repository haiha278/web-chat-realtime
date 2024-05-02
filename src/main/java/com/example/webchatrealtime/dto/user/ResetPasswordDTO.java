package com.example.webchatrealtime.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPasswordDTO {
    private String otp;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Length(min = 8, max = 32, message = "Mật khẩu tối thiếu có 8 kí tự và tối đa 32 kí tự")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=_]).+$",
            message = "Mật khẩu phải chứa ít nhất một chữ thường, một chữ hoa, một chữ số và một ký tự đặc biệt")
    private String newPassword;
    private String confirmNewPassword;
}
