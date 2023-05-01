package com.sparta.spartablog.service;

import com.sparta.spartablog.dto.CommentOneResponseDto;
import com.sparta.spartablog.dto.CommentRequestDto;
import com.sparta.spartablog.dto.StatusEnum;
import com.sparta.spartablog.dto.SuccessResponseDto;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.entity.Comment;
import com.sparta.spartablog.entity.User;
import com.sparta.spartablog.entity.UserRoleEnum;
import com.sparta.spartablog.jwt.JwtUtil;
import com.sparta.spartablog.repository.BlogRepository;
import com.sparta.spartablog.repository.CommentRepository;
import com.sparta.spartablog.repository.UserRepository;
import com.sparta.spartablog.util.exception.CustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.sparta.spartablog.util.exception.ErrorCode.*;

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
                () -> new CustomException(NOT_FOUND_BLOG)
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
                throw new CustomException(INVALID_TOKEN);
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(NOT_FOUND_USER)
            );

            // 요청 받은 DTO로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.save(new Comment(commentRequestDto, user.getUsername(), blog));

            return new CommentOneResponseDto(StatusEnum.OK, comment);

        } else {
            throw new CustomException(INVALID_TOKEN);

        }
    }

    @Transactional
    public SuccessResponseDto updateComment(Long blogId, Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
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
                throw new CustomException(INVALID_TOKEN);
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(NOT_FOUND_USER)
            );

//            // 댓글의 DB 저장 유무 확인
//            Comment comment = commentRepository.findByIdAndUsername(commentId, user.getUsername()).orElseThrow(
//                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
//            );
            // 사용자 권한 가져와서 ADMIN 이면 무조건 수정 가능, USER 면 본인이 작성한 댓글일 때만 수정 가능
            UserRoleEnum userRoleEnum = user.getRole();
            Comment comment;

            if (userRoleEnum == UserRoleEnum.ADMIN) {
                // 입력 받은 댓글 id와 일치하는 DB 조회
                comment = commentRepository.findById(commentId).orElseThrow(
                        () -> new CustomException(NOT_FOUND_COMMENT)
                );
            } else {
                // 입력 받은 댓글 id, 토큰에서 가져온 username과 일치하는 DB 조회
                comment = commentRepository.findByIdAndUsername(commentId, user.getUsername()).orElseThrow(
                        () -> new CustomException(AUTHORIZATION)
                );
            }

            // 요청 받은 DTO로 DB에 업데이트
            comment.update(commentRequestDto);

            return new CommentOneResponseDto(StatusEnum.OK, comment);

        } else {
            throw new CustomException(INVALID_TOKEN);

        }
    }

    @Transactional
    public SuccessResponseDto deleteComment(Long blogId, Long commentId, HttpServletRequest request) {
        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
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
                throw new CustomException(INVALID_TOKEN);
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(NOT_FOUND_USER)
            );

//            // 댓글의 DB 저장 유무 확인
//            Comment comment = commentRepository.findByIdAndUsername(commentId, user.getUsername()).orElseThrow(
//                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
//            );
            // 사용자 권한 가져와서 ADMIN 이면 무조건 수정 가능, USER 면 본인이 작성한 댓글일 때만 수정 가능
            UserRoleEnum userRoleEnum = user.getRole();

            Comment comment;

            if (userRoleEnum == UserRoleEnum.ADMIN) {
                // 입력 받은 댓글 id와 일치하는 DB 조회
                comment = commentRepository.findById(commentId).orElseThrow(
                        () -> new CustomException(NOT_FOUND_COMMENT)
                );
            } else {
                // 입력 받은 댓글 id, 토큰에서 가져온 username과 일치하는 DB 조회
                comment = commentRepository.findByIdAndUsername(commentId, user.getUsername()).orElseThrow(
                        () -> new CustomException(AUTHORIZATION)
                );
            }

            // 해당 댓글 삭제
            return new SuccessResponseDto(StatusEnum.OK);

        } else {
            throw new CustomException(INVALID_TOKEN);
        }
    }
}