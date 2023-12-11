package com.example.medium.global.utils;

import com.example.medium.domain.comment.entity.Comment;
import com.example.medium.domain.comment.repository.CommentRepository;
import com.example.medium.domain.member.dto.MemberRequestDto;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.domain.post.dto.PostRequestDto;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.service.PostService;
import com.example.medium.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("!dev")
@Configuration
@RequiredArgsConstructor
public class initData {
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    @Bean
    public ApplicationRunner run() {
        return new ApplicationRunner(){
            @Override
            @Transactional
            @SneakyThrows
            public void run(ApplicationArguments args) {
                memberService.create(new MemberRequestDto(
                        "sbbadmin"
                        , "12345678",
                        "12345678")
                );
                memberService.create(new MemberRequestDto(
                        "testuser1"
                        , "12345678",
                        "12345678")
                );

                for (int i = 1; i < 100; i++) {
                    ResponseDto<Post> postResp;
                    if (i % 2 == 0) {
                        postResp = postService.create(new PostRequestDto(
                                        memberService.findByUsername("testuser1"),
                                        false,
                                        String.format("테스트 글 %d", i),
                                        String.format("테스트 내용 %d", i)
                                )
                        );
                    } else {
                        postResp = postService.create(new PostRequestDto(
                                        memberService.findByUsername("testuser1"),
                                        true,
                                        String.format("테스트 글 %d", i),
                                        String.format("테스트 내용 %d", i)
                                )
                        );
                    }
                    for (int j = 0; j < 3; j++) {
                        Post post = postResp.getData();
                        Comment comment = Comment.builder()
                                .post(post)
                                .author(post.getAuthor())
                                .content(String.format("테스트 댓글 %d", i))
                                .build();
                        commentRepository.save(comment);
                    }
                }
            }
        };
    }
}

