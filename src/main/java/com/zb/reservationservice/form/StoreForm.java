package com.zb.reservationservice.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class StoreForm {

    @Size(min = 4, max = 10)
    private String name;
    @Size(min = 20, max = 200)
    private String description;

    @Min(0)
    private double lat;
    @Min(0)
    private double lnt;
}
