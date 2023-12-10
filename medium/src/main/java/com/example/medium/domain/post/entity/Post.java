package com.example.medium.domain.post.entity;

import com.example.medium.domain.comment.entity.Comment;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
public class Post extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "text", length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean isPublished;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
}
