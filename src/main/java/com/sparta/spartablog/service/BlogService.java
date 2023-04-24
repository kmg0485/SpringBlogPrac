package com.sparta.spartablog.service;

import com.sparta.spartablog.dto.*;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.entity.User;
import com.sparta.spartablog.jwt.JwtUtil;
import com.sparta.spartablog.repository.BlogRepository;
import com.sparta.spartablog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service //서비스 부분임을 알려줌
@RequiredArgsConstructor
public class BlogService {
    //데이터베이스와 연결을 하기위하여 데이터베이스와 연결하는 부분인 BlogRepository를 사용할 수 있도록 추가
    public final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
//    public List<BlogResponseDto> getBlogList() {
        //테이블에 저장되어있는 모든 게시글 조회
        //뽑아온 데이터를 map을 통해 responsedto로 가공, collect가 list 타입으로 묶어줌.
//        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).collect(Collectors.toList());
//    }
    public BlogListResponseDto getBlogList() {
        BlogListResponseDto blogListResponseDto = new BlogListResponseDto(true, HttpStatus.OK.value());
        List<Blog> blogList = blogRepository.findAllByOrderByModifiedAtDesc();
        for (Blog blog : blogList) {
            blogListResponseDto.addBlog(new BlogResponseDto(blog));
        }
        return blogListResponseDto;
    }


    @Transactional(readOnly = true)
    public BlogOneResponseDto getBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new BlogOneResponseDto(true, HttpStatus.OK.value(), blog);
    }
//    public BlogResponseDto getBlog(Long id) {
//        //조회하기 위해 받아온 blog의 id를 사용해서 해당 blog 인스턴스가 존재하는지 확인하고 데이터를 가져옴
//        Blog blog = checkBlog(id);
//
//        return new BlogResponseDto(blog);
//    }
//
//    private Blog checkBlog(Long id) {
//        return blogRepository.findById(id).orElseThrow(
//                //예외처리?
//                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
//        );
//    }

//    @Transactional
//    public Blog createBlog(BlogRequestDto requestDto) {
//        Blog blog = new Blog(requestDto);
//        blogRepository.save(blog); //save 함수를 통해 저장
//        return blog;
//    }

    @Transactional
    public BlogOneResponseDto createBlog(BlogRequestDto requestDto, HttpServletRequest request) {
        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 작성 가능
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
            Blog blog = blogRepository.saveAndFlush(new Blog(requestDto, user.getUsername()));

            return new BlogOneResponseDto(true, HttpStatus.OK.value(), blog);

        } else {
            return  null;
        }

    }

    @Transactional
    public SuccessResponseDto updateBlog(Long id, BlogRequestDto requestDto, HttpServletRequest request){
        //해당 id에 맞는 데이터가 존재하는지 확인
//        Blog blog = blogRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );

        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 수정 가능
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

            // 입력 받은 id, 토큰에서 가져온 username과 일치하는 DB 조회
            Blog blog = blogRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
            );
            blog.update(requestDto, user.getUsername());

            return new BlogOneResponseDto(true, HttpStatus.OK.value(), blog);

        } else {
            return new SuccessResponseDto(false, HttpStatus.NOT_FOUND.value());
        }

    }

    public SuccessResponseDto deleteBlog(Long id, HttpServletRequest request) {
//        Blog blog = blogRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
//        );
        // Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 수정 가능
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

            // 입력 받은 id, 토큰에서 가져온 username과 일치하는 DB 조회
            Blog blog = blogRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
            );
            blogRepository.deleteById(id);

            return new SuccessResponseDto(true, HttpStatus.OK.value());

        } else {
            return new SuccessResponseDto(false, HttpStatus.NOT_FOUND.value());
        }
    }
}
