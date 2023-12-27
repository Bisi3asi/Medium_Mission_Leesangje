package com.example.medium.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CommentRequestDto {
    private String username;

    @NotNull
    private boolean isPublished = true; // default

    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(max = 300, message = "댓글은 300자를 초과할 수 없습니다.")
    private String content;
}
