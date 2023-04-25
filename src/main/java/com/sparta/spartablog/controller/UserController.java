package com.sparta.spartablog.controller;


import com.sparta.spartablog.dto.LoginRequestDto;
import com.sparta.spartablog.dto.SignupRequestDto;
import com.sparta.spartablog.dto.SuccessResponseDto;
import com.sparta.spartablog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 가입(아이디 중복검사 포함)
    @PostMapping ("/signup") // http://localhost:8080/user/signup
    public SuccessResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 로그인(성공시 토큰 발급)
    @PostMapping("/login") // http://localhost:8080/user/login
    public SuccessResponseDto login(
            @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }

}