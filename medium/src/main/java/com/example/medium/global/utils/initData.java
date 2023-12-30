package com.example.medium.global.utils;

import com.example.medium.domain.comment.dto.CommentRequestDto;
import com.example.medium.domain.comment.service.CommentService;
import com.example.medium.domain.member.dto.MemberJoinRequestDto;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.entity.Role;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.domain.post.dto.PostRequestDto;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import com.example.medium.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;


@Configuration
@RequiredArgsConstructor
public class initData {
    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;

    @Profile("!dev")
    @Bean
    public ApplicationRunner initTestData() {
        return new ApplicationRunner() {
            BindingResult brs;

            @Override
            @Transactional
            @SneakyThrows
            public void run(ApplicationArguments args) {
                memberService.create(new MemberJoinRequestDto(
                                "mediumadmin",
                                "12345678",
                                "12345678")
                        , brs
                );
                Member admin = memberService.findByUsername("mediumadmin");
                memberService.setAuthority(admin, Role.ADMIN);
                memberService.setPrime(admin);

                memberService.create(new MemberJoinRequestDto(
                                "testuser1",
                                "12345678",
                                "12345678")
                        , brs
                );
                memberService.create(new MemberJoinRequestDto(
                                "testuser2",
                                "12345678",
                                "12345678")
                        , brs
                );

                for (int i = 1; i < 100; i++) {
                    ResponseData<Post> postResp;
                    if (i % 2 == 0) {
                        postResp = postService.create(new PostRequestDto(
                                        true,
                                        false,
                                        String.format("테스트 글 %d", i),
                                        String.format("테스트 내용 %d", i),
                                        null
                                ),
                                memberService.findByUsername("testuser1")
                        );
                    } else {
                        postResp = postService.create(new PostRequestDto(
                                        true,
                                        true,
                                        String.format("유료 글 %d", i),
                                        String.format("유료 내용 %d", i),
                                        null
                                ),
                                memberService.findByUsername("testuser2")
                        );
                    }
                    for (int j = 0; j < 3; j++) {
                        Post post = postResp.getData();
                        commentService.create(post, new CommentRequestDto(
                                        "testuser1",
                                        true,
                                        String.format("테스트 댓글 %d", j)
                                ),
                                memberService.findByUsername("testuser1")
                        );
                    }
                }
            }
        };
    }
}

