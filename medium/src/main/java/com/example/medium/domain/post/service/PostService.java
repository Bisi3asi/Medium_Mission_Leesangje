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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

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
    public Post findById(Long id) {
        Optional<Post> opPost = postRepository.findById(id);
        if (opPost.isPresent()){
            Post post = opPost.get();
            post.incrViewCount();
            return post;
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR: Post not found.");
    }

    public Post getLatest() {
        return postRepository.findTopByOrderByIdDesc()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR: Post not found.")
                );
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
        return ResponseData.of("200", "Your work has been successfully posted.", post);
    }

    @Transactional
    public ResponseData<Post> modify(PostRequestDto req, Long id, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ERROR: Post not found.")
        );
        if (!Objects.equals(post.getAuthor().getUsername(), member.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ERROR: you are not authorized to modify this post."
            );
        }
        post = post.toBuilder()
                .author(member)
                .title(req.getTitle())
                .content(req.getContent())
                .isPublished(req.isPublished())
                .build();

        postRepository.save(post);
        return ResponseData.of("200", "Your post has been successfully updated.", post);
    }

    @Transactional
    public ResponseData<Post> delete(Long id, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found.")
        );
        if (!Objects.equals(post.getAuthor().getUsername(), member.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ERROR: you are not authorized to modify this post"
            );
        }
        postRepository.delete(post);
        return ResponseData.of("200", "Your post has been successfully deleted.", null);
    }
}
