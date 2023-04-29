package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Blog;
import lombok.Getter;

import java.util.List;

@Getter
public class BlogOneResponseDto extends SuccessResponseDto{

    private BlogResponseDto blogOne;

    public BlogOneResponseDto(Boolean success, int statusCode, Blog blog) {
        super(success, statusCode);
        this.blogOne = new BlogResponseDto(blog);
    }

    public BlogOneResponseDto(Boolean success, int statusCode, Blog blog, List<CommentResponseDto> commentList) {
        super(success, statusCode);
        this.blogOne = new BlogResponseDto(blog, commentList);
    }
}
