package com.example.webchatrealtime.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtils {
    public String generate() {
        Random random = new Random();
        String otp = String.valueOf(random.nextInt(99999));
        while (otp.length() < 5) ;
        {
            otp = "0" + otp;
        }
        System.out.println(otp);
        return otp;
    }
}
