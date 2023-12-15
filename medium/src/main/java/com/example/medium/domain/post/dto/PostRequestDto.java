package com.example.medium.domain.post.dto;

import com.example.medium.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Member author;

    @NotNull
    private boolean isPublished = true; // default

    @NotBlank(message = "title can not be blank.")
    @Size(max = 50, message = "title can not exceed 50 length.")
    private String title;

    @NotBlank(message = "content can not be blank.")
    @Size(max = 1000, message = "content can not exceed 1000 length.")
    private String content;

    private MultipartFile multipartFile;
}
