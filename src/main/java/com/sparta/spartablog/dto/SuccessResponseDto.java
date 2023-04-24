package com.sparta.spartablog.dto;

import lombok.Getter;


@Getter
public class SuccessResponseDto {
    public Boolean success;
    public int statusCode;

    public SuccessResponseDto(Boolean success, int statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }
}