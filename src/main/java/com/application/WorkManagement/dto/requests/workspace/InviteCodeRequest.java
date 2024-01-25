package com.application.WorkManagement.dto.requests.workspace;

import com.application.WorkManagement.enums.WorkspaceRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeRequest {

    @NotNull(message = "Không được để trống")
    private WorkspaceRole role;

}
