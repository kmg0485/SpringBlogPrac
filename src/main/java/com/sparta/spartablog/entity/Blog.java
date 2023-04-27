package com.sparta.spartablog.entity;

import com.sparta.spartablog.dto.BlogRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity //Blog라는 클래스를 데이터베이스의 Blog라는 테이블과 mapping하기 위해 사용
@NoArgsConstructor
public class Blog extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    // cascade : 영속성 전이, 보통 @onetomany가 걸린 엔티티에 사용
    // cascade remove: 하위 엔티티까지 제거 작업을 지속
    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    @OrderBy("modifiedAt DESC")
    private List<Comment> comments = new ArrayList<>();


    public Blog(BlogRequestDto requestDto, String username) {
        //오버로딩된 생성자를 통해서 주입
        //dto에 private임에도 불구하고 getter 메서드를 사용하여 데이터를 가져옴.
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void update(BlogRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void createComment(Comment comment) {
        this.comments.add(comment);
    }
}
