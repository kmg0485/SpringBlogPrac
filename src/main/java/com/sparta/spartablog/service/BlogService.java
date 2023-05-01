package com.sparta.spartablog.service;

import com.sparta.spartablog.dto.*;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.entity.Comment;
import com.sparta.spartablog.entity.User;
import com.sparta.spartablog.entity.UserRoleEnum;
import com.sparta.spartablog.jwt.JwtUtil;
import com.sparta.spartablog.repository.BlogRepository;
import com.sparta.spartablog.repository.UserRepository;
import com.sparta.spartablog.util.exception.CustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.spartablog.util.exception.ErrorCode.*;

@Service //서비스 부분임을 알려줌
@RequiredArgsConstructor
public class BlogService {
    //데이터베이스와 연결을 하기위하여 데이터베이스와 연결하는 부분인 BlogRepository를 사용할 수 있도록 추가
    public final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public SuccessResponseDto getBlogList() {
        BlogListResponseDto blogListResponseDto = new BlogListResponseDto(StatusEnum.OK);
        List<Blog> blogList = blogRepository.findAllByOrderByModifiedAtDesc();
        for (Blog blog : blogList) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : blog.getComments()) {
                commentList.add(new CommentResponseDto(comment));
            }
            blogListResponseDto.addBlog(new BlogResponseDto(blog, commentList));
        }
        return blogListResponseDto;
    }



    @Transactional(readOnly = true)
    public BlogOneResponseDto getBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : blog.getComments()) {
            commentList.add(new CommentResponseDto(comment));
        }
        return new BlogOneResponseDto(StatusEnum.OK, blog, commentList);
    }



    @Transactional
    public SuccessResponseDto createBlog(BlogRequestDto requestDto, HttpServletRequest request) {
        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 작성 가능
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
            Blog blog = blogRepository.saveAndFlush(new Blog(requestDto, user.getUsername()));

            return new BlogOneResponseDto(StatusEnum.OK, blog);

        } else {
            throw new CustomException(INVALID_TOKEN);

        }

    }

    @Transactional
    public SuccessResponseDto updateBlog(Long id, BlogRequestDto requestDto, HttpServletRequest request) {

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 수정 가능
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

            // 사용자 권한 가져와서 ADMIN 이면 무조건 수정 가능, USER 면 본인이 작성한 글일 때만 수정 가능
            UserRoleEnum userRoleEnum = user.getRole();

            Blog blog;

            if (userRoleEnum == UserRoleEnum.ADMIN) {
                // 입력 받은 게시글 id와 일치하는 DB 조회
                blog = blogRepository.findById(id).orElseThrow(
                        () -> new CustomException(NOT_FOUND_BLOG)
                );

            } else {
                // 입력 받은 게시글 id, 토큰에서 가져온 username과 일치하는 DB 조회
                blog = blogRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                        () -> new CustomException(AUTHORIZATION)
                );
            }

            blog.update(requestDto);

            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : blog.getComments()) {
                commentList.add(new CommentResponseDto(comment));
            }

            return new BlogOneResponseDto(StatusEnum.OK, blog, commentList);

        } else {
            throw new CustomException(INVALID_TOKEN);

        }

    }


    public SuccessResponseDto deleteBlog(Long id, HttpServletRequest request) {

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 수정 가능
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

            // 사용자 권한 가져와서 ADMIN 이면 무조건 삭제 가능, USER 면 본인이 작성한 글일 때만 삭제 가능
            UserRoleEnum userRoleEnum = user.getRole();

            Blog blog;

            if (userRoleEnum == UserRoleEnum.ADMIN) {
                // 입력 받은 게시글 id와 일치하는 DB 조회
                blog = blogRepository.findById(id).orElseThrow(
                        () -> new CustomException(NOT_FOUND_BLOG)
                );

            } else {
                // 입력 받은 게시글 id, 토큰에서 가져온 username과 일치하는 DB 조회
                blog = blogRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                        () -> new CustomException(AUTHORIZATION)
                );
            }

            blogRepository.deleteById(id);

            return new SuccessResponseDto(StatusEnum.OK);

        } else {
            throw new CustomException(INVALID_TOKEN);

        }
    }
}
