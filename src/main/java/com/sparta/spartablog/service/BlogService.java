package com.sparta.spartablog.service;

import com.sparta.spartablog.dto.BlogRequestDto;
import com.sparta.spartablog.dto.BlogResponseDto;
import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service //서비스 부분임을 알려줌
@RequiredArgsConstructor
public class BlogService {//데이터베이스와 연결을 하기위하여 데이터베이스와 연결하는 부분인 BlogRepository를 사용할 수 있도록 추가
    public final BlogRepository blogRepository;


    public List<BlogResponseDto> getBlogList() {
        //테이블에 저장되어있는 모든 게시글 조회
        //뽑아온 데이터를 map을 통해 responsedto로 가공, collect가 list 타입으로 묶어줌.
        return blogRepository.findAll().stream().map(BlogResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Blog createBlog(BlogRequestDto requestDto) {
        Blog blog = new Blog(requestDto);
        blogRepository.save(blog); //save 함수를 통해 저장
        return blog;
    }

    public BlogResponseDto getBlog(Long id) {
        //조회하기 위해 받아온 blog의 id를 사용해서 해당 blog 인스턴스가 확인하고 데이터를 가져옴
        Blog blog = checkBlog(id);

        return new BlogResponseDto(blog);
    }

    private Blog checkBlog(Long id) {
        return blogRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );
    }
}