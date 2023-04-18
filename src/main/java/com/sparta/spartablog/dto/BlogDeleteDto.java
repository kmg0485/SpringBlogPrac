package com.sparta.spartablog.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogDeleteDto {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}
