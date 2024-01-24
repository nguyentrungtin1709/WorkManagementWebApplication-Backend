package com.application.WorkManagement.dto.requests.workspace;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequest {

    @NotBlank(message = "Không được bỏ trống")
    @Length(max = 120, message = "Tên không gian làm việc không được vượt quá 120 ký tự")
    private String name;

    @Length(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

}
