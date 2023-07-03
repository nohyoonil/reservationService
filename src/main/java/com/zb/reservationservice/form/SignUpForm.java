package com.zb.reservationservice.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpForm {

    @NotBlank(message = "필수 입력값입니다")
    private String name;

    @NotBlank(message = "필수 입력값입니다")
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank(message = "필수 입력값입니다")
    private String phone;
}
