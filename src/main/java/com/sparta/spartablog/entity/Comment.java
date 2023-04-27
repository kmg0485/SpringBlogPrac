package com.sparta.spartablog.entity;

import com.sparta.spartablog.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "BLOG_ID", nullable = false)
    private Blog blog;

    public Comment(CommentRequestDto commentRequestDto, String username, Blog blog) {
        this.comment = commentRequestDto.getComment();
        this.username = username;
        this.blog = blog;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}
