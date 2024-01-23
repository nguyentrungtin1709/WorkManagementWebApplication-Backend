package com.application.WorkManagement.dto.requests.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    @Email(
            message = "Email không hợp lệ"
    )
    @NotBlank(
            message = "Email không được để trống"
    )
    private String email;

}
