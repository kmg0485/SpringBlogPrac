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

    @GetMapping("") // http://localhost:8080/blog
    public List<BlogResponseDto> getBlogList() {
        //테이블에 저장되어있는 모든 게시글 조회
        return blogService.getBlogList();
    }

    @GetMapping("/{id}") // http://localhost:8080/blog/2
    public BlogResponseDto getBlog(@PathVariable Long id) {
        //테이블에 저장되어있는 해당 게시글 조회
        return blogService.getBlog(id);
    }

    @PostMapping("") // http://localhost:8080/blog
    public Blog createBlog(@RequestBody BlogRequestDto requestDto) { //객체형식으로 넘어오기때문에 Body 사용

        return blogService.createBlog(requestDto);
    }

    @PutMapping("/{id}") // http://localhost:8080/blog/2
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        // 수정하기 위해 받아온 id를 사용하여 해당 게시글의 인스턴스가 존재하는지 확인하고 가져옴
            return blogService.updateBlog(id, requestDto);
    }
}