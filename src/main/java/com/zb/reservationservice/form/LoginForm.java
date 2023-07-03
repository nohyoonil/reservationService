package com.zb.reservationservice.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LoginForm {

    @NotBlank(message = "필수 입력값입니다")
    private String phone;

    @NotBlank(message = "필수 입력값입니다")
    @Size(min = 8, max = 20)
    private String password;
}
