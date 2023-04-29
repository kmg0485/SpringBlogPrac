package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Comment;
import lombok.Getter;

@Getter
public class CommentOneResponseDto extends SuccessResponseDto {
    private CommentResponseDto commentOne;
    public CommentOneResponseDto(boolean success, int statusCode, Comment comment) {
        super(success, statusCode);
        this.commentOne = new CommentResponseDto(comment);
    }
}
