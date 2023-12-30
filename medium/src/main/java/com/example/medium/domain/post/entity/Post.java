package com.example.medium.domain.post.entity;

import com.example.medium.domain.comment.entity.Comment;
import com.example.medium.domain.file.entity.ImageFile;
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

    @Column(nullable = false)
    private boolean isPrime;

    @Column(columnDefinition = "int default 0", nullable = false)
    @Builder.Default
    private int viewCount = 0;

    public void incrViewCount(){
        viewCount++;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    // OnetoOne 양방향은 LAZY 로딩 적용 X
    // 연관관계 주인이 mappedBy = post에 있음을 알림
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_file_id")
    private ImageFile imageFile;
}
