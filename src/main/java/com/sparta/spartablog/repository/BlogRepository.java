package com.sparta.spartablog.repository;

import com.sparta.spartablog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//테이블 연결(jparepo 상속)
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByModifiedAtDesc(); //내림차순으로 정렬
}
