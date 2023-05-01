package com.sparta.spartablog.dto;

import lombok.Getter;


@Getter
public class SuccessResponseDto {
    private int statusCode;
    private String msg;

    public SuccessResponseDto(StatusEnum status) {
        this.statusCode = status.statusCode;
        this.msg = status.msg;
    }
}