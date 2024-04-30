package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompleteRequest {

    @NotNull(message = "Không được bỏ trống")
    private Boolean complete;

}
