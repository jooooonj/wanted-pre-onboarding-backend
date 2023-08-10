package com.wanted.boardAPI.domain.post.entity.response;

import com.wanted.boardAPI.domain.post.entity.Post;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PostResponse {
    private String author;
    private Long postId;
    private String title;

    private String content;
}
