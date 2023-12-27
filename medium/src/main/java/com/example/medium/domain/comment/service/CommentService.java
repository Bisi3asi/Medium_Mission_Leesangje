package com.example.medium.domain.comment.service;

import com.example.medium.domain.comment.dto.CommentRequestDto;
import com.example.medium.domain.comment.entity.Comment;
import com.example.medium.domain.comment.repository.CommentRepository;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment get(long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.")
        );
    }
    
    @Transactional
    public ResponseData<Comment> create(Post post, CommentRequestDto commentRequestDto, Member member) {
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent().trim())
                .post(post)
                .author(member)
                .isPublished(commentRequestDto.isPublished())
                .build();

        commentRepository.save(comment);
        return ResponseData.of("200", "댓글이 등록되었습니다.", comment);
    }
    
    @Transactional
    public ResponseData<Comment> update(Comment comment, CommentRequestDto commentRequestDto, Member member){
        validateAuthor(comment, member);

        comment = comment.toBuilder()
                .content(commentRequestDto.getContent())
                .isPublished(commentRequestDto.isPublished())
                .build();

        commentRepository.save(comment);
        return ResponseData.of("200", "댓글이 수정되었습니다.", comment);
    }

    @Transactional
    public ResponseData delete(Comment comment, Member member){
        validateAuthor(comment, member);

        commentRepository.delete(comment);
        return ResponseData.of("200", "댓글이 삭제되었습니다.");
    }

    public void validateAuthor(Comment comment, Member member){
        if (!Objects.equals(comment.getAuthor().getUsername(), member.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error : 권한이 없는 사용자입니다.");
        }
    }
}
