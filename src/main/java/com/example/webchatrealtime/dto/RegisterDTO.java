package com.example.webchatrealtime.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class RegisterDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Length(min = 8, max = 32, message = "Tên đăng nhập tối thiếu có 8 kí tự và tối đa 32 kí tự")
    @Pattern(regexp = "^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$", message = "Tên đăng nhập chỉ chứa chữ cái, số, và kí tự đặc biệt")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Length(min = 8, max = 32, message = "Mật khẩu tối thiếu có 8 kí tự và tối đa 32 kí tự")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=_]).+$",
            message = "Mật khẩu phải chứa ít nhất một chữ thường, một chữ hoa, một chữ số và một ký tự đặc biệt")
    private String password;

    @NotBlank(message = "Tên người dùng không được để trống")
    @Length(max = 32, message = "Tên người dùng có tối đa 32 kí tự")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Tên người dùng chỉ được chứa các chữ cái, dấu cách, dấu chấm, dấu gạch nối và dấu.")
    private String name;

    private String avatar;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Past (message = "Ngày không hợp lệ")
    @NotNull(message = "Không được để trống")
    private LocalDate dateOfBirth;

    @NotNull(message = "Không được để trống")
    private Integer gender;
}
