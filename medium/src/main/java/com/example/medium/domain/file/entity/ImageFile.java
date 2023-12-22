package com.example.medium.domain.file.entity;

import com.example.medium.domain.post.entity.Post;
import com.example.medium.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
public class ImageFile extends BaseEntity {
    // 원본 파일 이름과 서버 저장 파일 경로를 분리하는 이유 : 동일 이름이 가진 파일이 업로드되면 오류가 생기므로
    @Column(unique = true, nullable = false)
    private String filename;

    @Column(nullable = false)
    private long filesize;

    // OnetoOne 양방향은 LAZY 로딩 적용 X
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
