package com.example.backend.memberInfo.service;

import com.example.backend.memberInfo.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoService.class);

}
