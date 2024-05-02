package com.example.webchatrealtime.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Validate {
    public List<String> validateInput(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError ->
                fieldError.getField() + ":" + fieldError.getDefaultMessage()).collect(Collectors.toList());
        return errors;
    }

    public boolean validateOtp(Map<String, String> storageOtp, String email, String enterOtp) {
        if (storageOtp.get(email).equalsIgnoreCase(enterOtp)) {
            return true;
        }
        return false;
    }
}
