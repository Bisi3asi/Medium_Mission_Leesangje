package com.example.medium.domain.post.repository;

import com.example.medium.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsPublishedTrue(Pageable pageable);

    Page<Post> findAllByAuthorUsernameAndIsPublishedTrue(String username, Pageable pageable);
    Page<Post> findAllByAuthorUsername(String username, Pageable pageable);
    Optional<Post> findTopByOrderByIdDesc();

}
