package com.zb.reservationservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInfo {

    private Long id;
    private String name;
    private String phone;
}
