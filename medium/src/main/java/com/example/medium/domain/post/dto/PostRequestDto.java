package com.example.medium.domain.post.dto;

import com.example.medium.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private Member author; // username으로 수정도 생각해봐야..
    private boolean isPublished;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
    private String content;

    private MultipartFile multipartFile;
}
