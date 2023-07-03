package com.zb.reservationservice.service;

import com.zb.reservationservice.entity.Owner;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.LoginForm;
import com.zb.reservationservice.form.SignUpForm;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.repository.OwnerRepository;
import com.zb.reservationservice.util.JWTUtils;
import com.zb.reservationservice.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public void signUp(SignUpForm signUpForm) {
        if (ownerRepository.existsByPhone(signUpForm.getPhone())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS);
        }

        String encodedPassword = PasswordUtils.getEncodedPassword(signUpForm.getPassword());

        Owner owner = Owner.builder()
                .name(signUpForm.getName())
                .phone(signUpForm.getPhone())
                .password(encodedPassword)
                .createdAt(LocalDateTime.now().withNano(0))
                .isPartner(false).build();

        ownerRepository.save(owner);
    }

    public String login(LoginForm loginForm) {

        Owner owner = ownerRepository.findFirstByPhone(loginForm.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        if (!PasswordUtils.equalPassword(loginForm.getPassword(), owner.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        return JWTUtils.createOwnerToken(owner.getId(), owner.getName(), owner.getPhone());
    }

    /**
     * 점주 파트너 가입
     */
    public void joinPartner(TokenInfo tokenInfo) {
        Owner owner = ownerRepository.findById(tokenInfo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        if (owner.isPartner()) {
            throw new CustomException(ErrorCode.ALREADY_JOIN_PARTNER);
        }

        owner.setPartner(true);
        ownerRepository.save(owner);
    }
}
