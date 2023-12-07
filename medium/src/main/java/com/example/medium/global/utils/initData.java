package com.example.medium.global.utils;

import com.example.medium.domain.comment.entity.Comment;
import com.example.medium.domain.comment.repository.CommentRepository;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.repository.MemberRepository;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class initData {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Autowired
    @Lazy
    private initData self;

    @Bean
    public ApplicationRunner run() {
        return args -> {
            self.setInitData();
        };
    }

    @Transactional
    public void setInitData() {
        Member member = Member.builder()
                .username("sbbadmin")
                .password("12345678")
                .build();
        memberRepository.save(member);

        member = Member.builder()
                .username("testuser1")
                .password("12345678")
                .build();
        memberRepository.save(member);

        for (int i = 1; i < 100; i++) {
            Post post = (i % 2 == 0) ?
                    Post.builder()
                            .title(String.format("테스트 글 %d", i))
                            .content(String.format("테스트 내용 %d", i))
                            .author(member)
                            .isPublished(false)
                            .build()
                    :
                    Post.builder()
                            .title(String.format("테스트 글 %d", i))
                            .content(String.format("테스트 내용 %d", i))
                            .author(member)
                            .isPublished(true)
                            .build();
            postRepository.save(post);

            for (int j = 0; j < 3; j++) {
                Comment comment = Comment.builder()
                        .post(post)
                        .author(member)
                        .content(String.format("테스트 댓글 %d", i))
                        .build();
                commentRepository.save(comment);
            }
        }
    }
}
