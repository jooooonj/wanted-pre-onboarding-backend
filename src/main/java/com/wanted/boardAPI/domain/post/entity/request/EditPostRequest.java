package com.wanted.boardAPI.domain.post.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class EditPostRequest {
    private String title;

    private String content;
}
