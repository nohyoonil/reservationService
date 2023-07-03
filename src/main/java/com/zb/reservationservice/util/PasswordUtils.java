package com.zb.reservationservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class PasswordUtils {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean equalPassword(String target, String original) {
        return passwordEncoder.matches(target, original);
    }
}
