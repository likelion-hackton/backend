package com.example.backend.memberInfo.service;

import com.example.backend.image.service.ImageService;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.memberInfo.converter.MemberInfoConverter;
import com.example.backend.memberInfo.entity.MemberInfo;
import com.example.backend.memberInfo.entity.MemberInfoImage;
import com.example.backend.memberInfo.entity.dto.request.EditMemberInfoRequestDTO;
import com.example.backend.memberInfo.entity.dto.response.MemberInfoDetailResponseDTO;
import com.example.backend.memberInfo.repository.MemberInfoImageRepository;
import com.example.backend.memberInfo.repository.MemberInfoRepository;
import com.example.backend.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberInfoImageRepository memberInfoImageRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoService.class);

    public MemberInfoDetailResponseDTO getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        MemberInfo memberInfo = memberInfoRepository.findByMember(member).orElse(null);
        if (memberInfo == null) {
            logger.warn("사용자 정보 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보 찾을 수 없음");
        }
        return MemberInfoConverter.memberInfoDetailConverter(memberInfo);
    }

    @Transactional
    public void initMemberInfo(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        String tag = randomTag("사용자");

        MemberInfo memberInfo = MemberInfoConverter.initMemberInfoConverter(member, tag);
        memberInfoRepository.save(memberInfo);

    }

    @Transactional
    public MemberInfoDetailResponseDTO editMemberInfo(EditMemberInfoRequestDTO req, String email, MultipartFile image){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        MemberInfo memberInfo = member.getMemberInfo();
        if(memberInfo == null){
            logger.warn("사용자 정보 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보 찾을 수 없음");
        }

        String tag = memberInfo.getTag();
        // 기존 닉네임과 다르다면
        if (!req.getNickname().equals(member.getMemberInfo().getNickname())){
            tag = randomTag(req.getNickname());
        }
        // 이름, 태그, 소개 변경
        memberInfo = MemberInfoConverter.editMemberInfoConverter(req, memberInfo, tag);

        // 바꿀 이미지가 존재한다면
        if(image != null && !image.isEmpty()){
            changeImage(memberInfo, image);
        }

        memberInfoRepository.flush();

        MemberInfo updateMemberInfo = memberInfoRepository.findById(memberInfo.getId()).orElse(null);

        if(updateMemberInfo == null){
            logger.warn("업데이트 사용자 정보 불러오기 실패");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "업데이트 사용자 정보 불러오기 실패");
        }

        // 저장
        MemberInfo saveMemberInfo = memberInfoRepository.save(updateMemberInfo);
        logger.info("멤버 정보 저장 성공: {}", saveMemberInfo.getId());
        return MemberInfoConverter.memberInfoDetailConverter(saveMemberInfo);
    }

    private String randomTag(String nickname){
        String tag = "0000";
        // 태그가 겹치지 않을 때까지
        while (memberInfoRepository.existsByNicknameAndTag(nickname, tag)){
            tag = RandomStringUtils.randomNumeric(4);
        }
        return tag;
    }

    // 이미지 변경
    private void changeImage(MemberInfo memberInfo, MultipartFile image){
        // 기존에 이미지가 이미 있다면
        if (memberInfo.getMemberInfoImage() != null){
            MemberInfoImage oldImage = memberInfo.getMemberInfoImage();
            memberInfo.setMemberInfoImage(null);
            memberInfoRepository.saveAndFlush(memberInfo);

            memberInfoImageRepository.delete(oldImage);
            memberInfoImageRepository.flush();

            storageService.deleteFile(oldImage.getImageUrl());
        }
        String imageUrl = storageService.storeFile(image);
        MemberInfoImage memberInfoImage = new MemberInfoImage();
        memberInfoImage.setImageUrl(imageUrl);
        memberInfoImage.setMemberInfo(memberInfo);

        memberInfoImageRepository.saveAndFlush(memberInfoImage);
        memberInfo.setMemberInfoImage(memberInfoImage);
        memberInfoRepository.saveAndFlush(memberInfo);
    }

}
