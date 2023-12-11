package com.example.medium.domain.member.service;

import com.example.medium.domain.member.dto.MemberRequestDto;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.repository.MemberRepository;
import com.example.medium.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findByUsername(String username){
        Optional<Member> opMember = memberRepository.findByUsername(username);

        if (opMember.isEmpty()){
            throw new IllegalArgumentException("Member not found");
        }
        return opMember.get();
    }

    @Transactional
    public ResponseDto<Member> create(MemberRequestDto memberRequestDto){
        if (!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())){
            throw new IllegalArgumentException("please check out password, password confirm.");
        }
        if (memberRepository.findByUsername(memberRequestDto.getUsername()).isPresent()){
            throw new DuplicateKeyException("username already exists, please choose another one.");
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.of("200", "you have successfully joined", member);
    }
}
