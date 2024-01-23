package com.application.WorkManagement.dto.requests.account;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @Length(
            max = 50,
            message = "Tên chứa tối đa 50 ký tự"
    )
    @NotBlank(
            message = "Tên không được để trống"
    )
    private String name;

    @Length(
            max = 255,
            message = "Tên tổ chức chứa tối đa 255 ký tự"
    )
    private String organization;

    @Length(
            max = 255,
            message = "Tên phòng ban chứa tối đa 255 ký tự"
    )
    private String department;


    @Length(
            max = 255,
            message = "Tên chức danh chứa tối đa 255 ký tự"
    )
    private String title;

}
