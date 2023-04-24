package com.sparta.spartablog.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlogListResponseDto extends SuccessResponseDto{
    List<BlogResponseDto> blogList = new ArrayList<>();

    public BlogListResponseDto(Boolean success, int statusCode) {
        super(success, statusCode);
    }

    public void addBlog(BlogResponseDto blogResponseDto) {

        blogList.add(blogResponseDto);
    }
}
