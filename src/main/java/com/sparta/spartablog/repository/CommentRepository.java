package com.sparta.spartablog.repository;

import com.sparta.spartablog.entity.Blog;
import com.sparta.spartablog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBlogIdOrderByModifiedAtDesc(Blog blog);
    Optional<Comment> findByIdAndUsername(Long commentId, String username);
}
