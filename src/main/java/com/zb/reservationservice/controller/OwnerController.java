package com.zb.reservationservice.controller;

import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.LoginForm;
import com.zb.reservationservice.form.SignUpForm;
import com.zb.reservationservice.model.LoginToken;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.service.OwnerService;
import com.zb.reservationservice.util.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/owner")
@RestController
public class OwnerController {

    private final OwnerService ownerService;

    /*
     * 점주 회원가입
     */
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpForm signUpForm, Errors errors) {

        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_DATA_INPUT);
        }

        ownerService.signUp(signUpForm);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm loginForm, Errors errors) {

        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_DATA_INPUT);
        }

        String token = ownerService.login(loginForm);

        return ResponseEntity.ok().body(new LoginToken(token));
    }

    /**
     * 점주 파트너 가입 - 점주
     */
    @PostMapping("/partner")
    public ResponseEntity<?> joinPartner(@RequestHeader("JWT-TOKEN") String token) {
        TokenInfo tokenInfo = JWTUtils.getOwnerTokenInfo(token);
        ownerService.joinPartner(tokenInfo);

        return ResponseEntity.ok().build();
    }


}
