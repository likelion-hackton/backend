package com.example.backend.memberInfo.controller;

import com.example.backend.memberInfo.entity.dto.request.EditMemberInfoRequestDTO;
import com.example.backend.memberInfo.entity.dto.response.MemberInfoDetailResponseDTO;
import com.example.backend.memberInfo.service.MemberInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member-info")
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @PatchMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberInfoDetailResponseDTO> editInfo(@RequestPart("info") @Valid EditMemberInfoRequestDTO req,
                                                                @RequestPart("image") MultipartFile image,
                                                                Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(memberInfoService.editMemberInfo(req, email ,image));
    }

}