package com.example.backend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@Primary
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    // 이메일 전송
    public void sendVerification(String email, String code){
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy. mm. dd");

        LocalTime nowTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 메일 발송");

        message.setText("""
                %s
                
                %s
                """
                .formatted(nowDate.format(dateFormatter) + " " + nowTime.format(timeFormatter),
                        code));
        mailSender.send(message);
    }
}
