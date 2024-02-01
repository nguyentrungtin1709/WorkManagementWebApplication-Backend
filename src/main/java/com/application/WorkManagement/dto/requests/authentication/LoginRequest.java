package com.application.WorkManagement.dto.requests.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Pattern(
            regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Email không hợp lệ"
    )
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+-]).{8,}$",
            message = "Mật khẩu cần có tối thiểu 8 ký tự, bao gồm ít nhất một chữ hoa, chữ thường, kí tự và số."
    )
    private String password;
}
