package com.wanted.boardAPI.domain.post.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreatePostRequest {

    @NotBlank(message = "제목은 필수값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수값입니다.")
    private String content;
}
