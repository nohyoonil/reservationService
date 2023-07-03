package com.zb.reservationservice.service;

import com.zb.reservationservice.entity.User;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.LoginForm;
import com.zb.reservationservice.form.SignUpForm;
import com.zb.reservationservice.repository.UserRepository;
import com.zb.reservationservice.util.JWTUtils;
import com.zb.reservationservice.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void signUp(SignUpForm signUpForm) {

        if (userRepository.existsByPhone(signUpForm.getPhone())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS);
        }

        String encodedPassword = PasswordUtils.getEncodedPassword(signUpForm.getPassword());

        User user = User.builder()
                .name(signUpForm.getName())
                .phone(signUpForm.getPhone())
                .password(encodedPassword)
                .createdAt(LocalDateTime.now().withNano(0)).build();

        userRepository.save(user);
    }

    public String login(LoginForm loginForm) {

        final User user = userRepository.findFirstByPhone(loginForm.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        if (!PasswordUtils.equalPassword(loginForm.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        return JWTUtils.createUserToken(user.getId(), user.getName(), user.getPhone());
    }
}
