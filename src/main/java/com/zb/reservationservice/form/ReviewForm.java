package com.zb.reservationservice.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewForm {

    @Min(0) @Max(5)
    private int score;

    @Size(min = 20)
    private String content;
}
