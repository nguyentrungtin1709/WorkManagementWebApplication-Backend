package com.application.WorkManagement.dto.requests.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    @Pattern(
            regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Email không hợp lệ"
    )
    private String email;

}
