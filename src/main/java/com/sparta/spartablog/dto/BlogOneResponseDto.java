package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Blog;
import lombok.Getter;

import java.util.List;

@Getter
public class BlogOneResponseDto extends SuccessResponseDto{

    private BlogResponseDto blogOne;

    public BlogOneResponseDto(StatusEnum status, Blog blog) {
        super(status);
        this.blogOne = new BlogResponseDto(blog);
    }

    public BlogOneResponseDto(StatusEnum status, Blog blog, List<CommentResponseDto> commentList) {
        super(status);
        this.blogOne = new BlogResponseDto(blog, commentList);
    }
}
