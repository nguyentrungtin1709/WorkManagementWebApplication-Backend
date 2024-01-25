package com.application.WorkManagement.dto.requests.workspace;

import com.application.WorkManagement.enums.WorkspaceRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    @Email(
            message = "Email không hợp lệ"
    )
    @NotBlank(
            message = "Email không được để trống"
    )
    private String email;

    @NotNull(message = "Không được để trống")
    private WorkspaceRole role;

}
