package com.wanted.boardAPI.domain.post.entity;

import com.wanted.boardAPI.base.baseEntity.BaseTimeEntity;
import com.wanted.boardAPI.domain.member.entity.Member;
import com.wanted.boardAPI.domain.post.entity.request.CreatePostRequest;
import com.wanted.boardAPI.domain.post.entity.request.EditPostRequest;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Post(Member member, String title, String content){
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static Post of(Member member, CreatePostRequest createPostRequest){
        return new Post(member,
                createPostRequest.getTitle(),
                createPostRequest.getContent())
        ;
    }

    public void update(EditPostRequest editPostRequest) {
        if(editPostRequest.getTitle().trim().length() > 0)
            this.title = editPostRequest.getTitle();
        if(editPostRequest.getContent().trim().length() > 0)
            this.content = editPostRequest.getContent();
    }
}
