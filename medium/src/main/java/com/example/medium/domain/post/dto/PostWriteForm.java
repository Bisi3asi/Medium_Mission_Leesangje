package com.example.medium.domain.post.dto;

import com.example.medium.global.dto.RsData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWriteForm {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 50, message = "내용은 1000자를 초과할 수 없습니다.")
    private final String body;

    private boolean isPublished;

    public static RsData<PostWriteForm> of(String title, String body, boolean isPublished){
        return RsData.of("200", "success", new PostWriteForm(title, body, isPublished));
    }

}
