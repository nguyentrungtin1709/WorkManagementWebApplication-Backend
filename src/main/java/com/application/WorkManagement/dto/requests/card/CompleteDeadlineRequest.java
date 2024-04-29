package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteDeadlineRequest {

    @NotNull(message = "Không được bỏ trống")
    private Boolean complete;

}
