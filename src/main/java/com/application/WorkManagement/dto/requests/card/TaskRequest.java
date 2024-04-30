package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "Không được bỏ trống")
    @Length(max = 120, message = "Tên công việc không được vượt quá 120 ký tự")
    private String name;

}
