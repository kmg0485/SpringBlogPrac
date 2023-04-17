package com.sparta.spartablog.entity;

import com.sparta.spartablog.dto.BlogRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Blog extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String password;

    public Blog(String title, String username, String content, String password) {
        this.title = title;
        this.username = username;
        this.content = content;
        this.password = password;
    }

    public Blog(BlogRequestDto requestDto) {
        //오버로딩된 생성자를 통해서 주입
        //dto에 private임에도 불구하고 getter 메서드를 사용하여 데이터를 가져옴.
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.content = requestDto.getContent();
        this.password = requestDto.getPassword();
    }
}
