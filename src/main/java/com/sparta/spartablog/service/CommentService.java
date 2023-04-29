package com.sparta.spartablog.service;

import com.sparta.spartablog.dto.CommentOneResponseDto;
import com.sparta.spartablog.dto.CommentRequestDto;
import com.sparta.spartablog.dto.SuccessResponseDto;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.entity.Comment;
import com.sparta.spartablog.entity.User;
import com.sparta.spartablog.jwt.JwtUtil;
import com.sparta.spartablog.repository.BlogRepository;
import com.sparta.spartablog.repository.CommentRepository;
import com.sparta.spartablog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 로그인한 회원이 댓글을 등록
    @Transactional
    public SuccessResponseDto createComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청 받은 DTO로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.save(new Comment(commentRequestDto, user.getUsername(), blog));

            return new CommentOneResponseDto(true, HttpStatus.OK.value(), comment);

        } else {
            return  null;
        }
    }

    @Transactional
    public SuccessResponseDto updateComment(Long blogId, Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 수정 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 댓글의 DB 저장 유무 확인
            Comment comment = commentRepository.findByIdAndUsername(commentId, user.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            // 요청 받은 DTO로 DB에 저장할 객체 만들기
            comment.update(commentRequestDto);

            return new CommentOneResponseDto(true, HttpStatus.OK.value(), comment);

        } else {
            return  null;
        }
    }

    @Transactional
    public SuccessResponseDto deleteComment(Long blogId, Long commentId, HttpServletRequest request) {
        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 수정 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 댓글의 DB 저장 유무 확인
            Comment comment = commentRepository.findByIdAndUsername(commentId, user.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            // 요청 받은 DTO로 DB에 저장할 객체 만들기
            commentRepository.deleteById(commentId);

            return new SuccessResponseDto(true, HttpStatus.OK.value());

        } else {
            return new SuccessResponseDto(false, HttpStatus.NOT_FOUND.value());
        }
    }
}