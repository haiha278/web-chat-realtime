package com.example.webchatrealtime.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerifyEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Xác thưc Email");
        mimeMessageHelper.setText("""
                <div>
                OTP để xác thực email: %s
                </div>
                """.formatted(otp), true);
        javaMailSender.send(mimeMessage);
    }
}
