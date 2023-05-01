package com.sparta.spartablog.controller;

import com.sparta.spartablog.dto.CommentRequestDto;
import com.sparta.spartablog.dto.SuccessResponseDto;
import com.sparta.spartablog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(commentService.createComment(id, commentRequestDto, request));
    }

    // 댓글 수정
    @PutMapping("/{blogId}/{commentId}")
    public ResponseEntity<SuccessResponseDto> updateComment(
            @PathVariable Long blogId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(commentService.updateComment(blogId, commentId, commentRequestDto, request));
    }

    // 댓글 삭제
    @DeleteMapping("/{blogId}/{commentId}")
    public ResponseEntity<SuccessResponseDto> deleteComment(
            @PathVariable Long blogId,
            @PathVariable Long commentId, HttpServletRequest request) {
        return ResponseEntity.ok().body(commentService.deleteComment(blogId, commentId, request));
    }
}
