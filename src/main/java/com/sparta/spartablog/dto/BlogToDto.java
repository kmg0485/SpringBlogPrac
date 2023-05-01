package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Blog;

import java.util.List;

public class BlogToDto {

    private BlogResponseDto blogOne;

    public BlogToDto(Blog blog) {
        this.blogOne = new BlogResponseDto(blog);
    }

    public BlogToDto(Blog blog, List<CommentResponseDto> commentList) {
        this.blogOne = new BlogResponseDto(blog, commentList);
    }
}
