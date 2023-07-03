package com.zb.reservationservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.model.TokenInfo;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public abstract class JWTUtils {

    private static final String USER_KEY = "reservationServiceUserKey";
    private static final String OWNER_KEY = "reservationServiceOwnerKey";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1 week

    public static String createOwnerToken(Long id, String name, String phone) {
        return createToken(id, name, phone, OWNER_KEY);
    }

    public static String createUserToken(Long id, String name, String phone) {
        return createToken(id, name, phone, USER_KEY);
    }

    private static String createToken(Long id, String name, String phone, String KEY) {

        final Date now = new Date();

        return JWT.create()
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
                .withClaim("id", id)
                .withSubject(name)
                .withIssuer(phone)
                .sign(Algorithm.HMAC512(KEY.getBytes(StandardCharsets.UTF_8)));
    }

    public static TokenInfo getOwnerTokenInfo(String token) {
        return getTokenInfo(token, OWNER_KEY);
    }

    public static TokenInfo getUserTokenInfo(String token) {
        return getTokenInfo(token, USER_KEY);
    }

    private static TokenInfo getTokenInfo(String token, String KEY) {
        DecodedJWT jwt;

        try {
            jwt = JWT.require(Algorithm.HMAC512(KEY.getBytes(StandardCharsets.UTF_8)))
                    .build().verify(token);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.REQUIRED_LOGIN_AGAIN);
        }

        final Long id = jwt.getClaim("id").asLong();
        final String name = jwt.getSubject();
        final String phone = jwt.getIssuer();

        return new TokenInfo(id, name, phone);
    }
}
