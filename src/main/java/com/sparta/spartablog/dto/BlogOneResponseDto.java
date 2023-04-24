package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Blog;
import lombok.Getter;

@Getter
public class BlogOneResponseDto extends SuccessResponseDto{

    BlogResponseDto blogOne;

    public BlogOneResponseDto(Boolean success, int statusCode, Blog blog) {
        super(success, statusCode);
        this.blogOne = new BlogResponseDto(blog);
    }
}
