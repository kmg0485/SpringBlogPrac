package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Blog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor //매개변수가 없는 기본생성자
public class BlogResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();


    public BlogResponseDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUsername();
        this.content = blog.getContent();
        this.createdAt=blog.getCreatedAt();
        this.modifiedAt=blog.getModifiedAt();
    }

    public BlogResponseDto(Blog blog, List<CommentResponseDto> commentList) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUsername();
        this.content = blog.getContent();
        this.createdAt=blog.getCreatedAt();
        this.modifiedAt=blog.getModifiedAt();
        this.commentList=commentList;

    }
}

