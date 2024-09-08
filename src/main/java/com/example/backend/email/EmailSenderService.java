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
@Primary
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    // 이메일 전송
    public void sendVerification(String email, String code){
        // 시간 포맷 설정
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd");

        LocalTime nowTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 메시지 정의
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
