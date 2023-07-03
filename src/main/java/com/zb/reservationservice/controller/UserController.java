package com.zb.reservationservice.controller;

import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.LoginForm;
import com.zb.reservationservice.form.SignUpForm;
import com.zb.reservationservice.model.LoginToken;
import com.zb.reservationservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;

    /*
     * 유저 회원가입
     */
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpForm signUpForm, Errors errors) {

        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_DATA_INPUT);
        }

        userService.signUp(signUpForm);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm loginForm, Errors errors) {

        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_DATA_INPUT);
        }

        String token = userService.login(loginForm);

        return ResponseEntity.ok().body(new LoginToken(token));
    }

}
