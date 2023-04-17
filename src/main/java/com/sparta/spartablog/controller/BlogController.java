package com.sparta.spartablog.controller;

import com.sparta.spartablog.dto.BlogRequestDto;
import com.sparta.spartablog.dto.BlogResponseDto;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/list") // http://localhost:8080/blog/list
    public List<BlogResponseDto> getBlogList() {
        //테이블에 저장되어있는 모든 게시글 조회
        return blogService.getBlogList();
    }

    @GetMapping("/list/{id}") // http://localhost:8080/blog/list/2
    public BlogResponseDto getBlog(@PathVariable Long id) {
        //테이블에 저장되어있는 해당 게시글 조회
        return blogService.getBlog(id);
    }


    @PostMapping("/create") // http://localhost:8080/blog/create
    public Blog createBlog(@RequestBody BlogRequestDto requestDto) { //객체형식으로 넘어오기때문에 Body 사용
        return blogService.createBlog(requestDto);
    }
}
