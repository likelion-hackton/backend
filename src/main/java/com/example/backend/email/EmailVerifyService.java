package com.example.backend.email;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Primary
public class EmailVerifyService {
    // 코드 저장
    private final Map<String, String> verificationCode = new ConcurrentHashMap<>();
    // 만료시간
    private static final Duration EXPIRATION = Duration.ofMinutes(10);

    // 코드 생성
    public String randomCode(){
        return RandomStringUtils.randomAlphanumeric(10);
    }

    // 코드 발급 및 저장
    public String generateVerificationCode(String email){
        String code = randomCode();
        verificationCode.remove(email);
        verificationCode.put(email, code);

        // 만료시 키 삭제 로직
        Executors.newSingleThreadScheduledExecutor().schedule(
                () -> verificationCode.remove(email),
                EXPIRATION.toMillis(),
                TimeUnit.MILLISECONDS
        );

        return code;
    }

    // 이메일과 코드 유효성 확인
    public boolean verifyCode(String email, String code){
        return Optional.ofNullable(verificationCode.remove(email))
                .map(storageCode -> storageCode.equals(code))
                .orElse(false);
    }
}
