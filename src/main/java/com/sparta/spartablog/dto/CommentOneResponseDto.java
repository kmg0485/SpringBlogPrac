package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Comment;
import lombok.Getter;

@Getter
public class CommentOneResponseDto extends SuccessResponseDto {
    private CommentResponseDto commentOne;
    public CommentOneResponseDto(StatusEnum status, Comment comment) {
        super(status);
        this.commentOne = new CommentResponseDto(comment);
    }
}
