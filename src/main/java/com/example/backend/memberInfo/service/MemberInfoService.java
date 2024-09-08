package com.example.backend.memberInfo.service;

import com.example.backend.image.service.ImageService;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.memberInfo.converter.MemberInfoConverter;
import com.example.backend.memberInfo.entity.MemberInfo;
import com.example.backend.memberInfo.entity.MemberInfoImage;
import com.example.backend.memberInfo.entity.dto.request.EditMemberInfoRequestDTO;
import com.example.backend.memberInfo.entity.dto.response.MemberInfoDetailResponseDTO;
import com.example.backend.memberInfo.repository.MemberInfoRepository;
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
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoService.class);

    @Transactional
    public MemberInfoDetailResponseDTO editMemberInfo(EditMemberInfoRequestDTO req, String email, MultipartFile image){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        String tag = member.getMemberInfo().getTag();
        // 기존 닉네임과 다르다면
        if (!req.getNickname().equals(member.getMemberInfo().getNickname())){
            tag = "0000";
            // 태그가 겹치지 않을 때까지
            while (memberInfoRepository.existsByNicknameAndTag(req.getNickname(), tag)){
                tag = RandomStringUtils.randomNumeric(4);
            }

        }
        // 객체 변환
        MemberInfo memberInfo = MemberInfoConverter.editMemberInfoConverter(req, member, tag);
        imageService.procesAndAddImages(memberInfo, List.of(image),
                url -> {
                    MemberInfoImage memberInfoImage = new MemberInfoImage();
                    memberInfoImage.setImageUrl(url);
                    return memberInfoImage;
                }, MemberInfo::addImage);
        // 저장
        MemberInfo saveMemberInfo = memberInfoRepository.save(memberInfo);
        return MemberInfoConverter.memberInfoDetailConverter(saveMemberInfo);
    }

}
