package com.example.backend.memberInfo.entity;

import com.example.backend.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "MemberInfo")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotBlank
    private String nickname;

    @NotBlank
    private String tag;

    private String introduction;

    @JsonManagedReference
    @OneToOne(mappedBy = "memberInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private MemberInfoImage memberInfoImage;

    public void addImage(MemberInfoImage image) {
        memberInfoImage = image;
        image.setMemberInfo(this);
    }
}
