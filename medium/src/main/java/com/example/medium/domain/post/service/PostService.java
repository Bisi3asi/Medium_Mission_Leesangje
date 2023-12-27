package com.example.medium.domain.post.service;

import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.post.dto.PostRequestDto;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.domain.post.repository.PostRepository;
import com.example.medium.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public Post getLatest() {
        return postRepository.findTopByOrderByIdDesc()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR: 게시글을 찾을 수 없습니다.")
                );
    }

    public Page<Post> getTotalList(int page, int pageSize) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return postRepository.findAllByIsPublishedTrue(pageable);
    }

    public Page<Post> getMemberList(int page, int pageSize, String username) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return postRepository.findAllByAuthorUsernameAndIsPublishedTrue(username, pageable);
    }

    @Transactional
    public ResponseData<Post> create(PostRequestDto req, Member member) {

        Post post = Post.builder()
                .author(member)
                .title(req.getTitle())
                .content(req.getContent())
                .isPublished(req.isPublished())
                .build();

        postRepository.save(post);
        return ResponseData.of("200", "게시글이 등록되었습니다.", post);
    }

    @Transactional
    public ResponseData<Post> modify(PostRequestDto req, Long id, Member member) {
        Post post = get(id);
        validateAuthor(post, member);

        post = post.toBuilder()
                .author(member)
                .title(req.getTitle())
                .content(req.getContent())
                .isPublished(req.isPublished())
                .build();

        postRepository.save(post);
        return ResponseData.of("200", "게시글이 수정되었습니다.", post);
    }

    @Transactional
    public ResponseData delete(Long id, Member member) {
        Post post = get(id);
        validateAuthor(post, member);

        postRepository.delete(post);
        return ResponseData.of("200", "게시글이 삭제되었습니다.");
    }

    @Transactional
    public void incrViewCount(Post post) {
        post.incrViewCount();
        postRepository.save(post);
    }

    public Post get(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR: 게시글을 찾을 수 없습니다.")
        );
    }

    public void validateAuthor(Post post, Member member) {
        if (!Objects.equals(post.getAuthor().getUsername(), member.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR : 권한이 없는 사용자입니다.");
        }
    }
}
