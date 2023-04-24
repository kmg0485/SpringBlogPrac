package com.sparta.spartablog.controller;

import com.sparta.spartablog.dto.*;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    // 전체 게시글 조회
    @GetMapping("") // http://localhost:8080/blog
    public BlogListResponseDto getBlogList() {
        //테이블에 저장되어있는 모든 게시글 조회
        return blogService.getBlogList();
    }

    // 해당 게시글 조회
    @GetMapping("/{id}") // http://localhost:8080/blog/2
    public BlogOneResponseDto getBlog(@PathVariable Long id) {
        //테이블에 저장되어있는 해당 게시글 조회
        return blogService.getBlog(id);
    }

    // 게시글 작성
    @PostMapping("") // http://localhost:8080/blog
    public BlogOneResponseDto createBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        //객체형식으로 넘어오기때문에 Body 사용
        return blogService.createBlog(requestDto, request);
    }

    //게시글 수정
    @PutMapping("/{id}") // http://localhost:8080/blog/2
    public SuccessResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        // 수정하기 위해 받아온 id를 사용하여 해당 게시글의 인스턴스가 존재하는지 확인하고 가져옴
            return blogService.updateBlog(id, requestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}") // http://localhost:8080/blog/2
    public SuccessResponseDto blogDeleteDto(@PathVariable Long id, @RequestBody BlogRequestDto blogRequestDto, HttpServletRequest request) {
        return blogService.deleteBlog(id, request);
    }
}