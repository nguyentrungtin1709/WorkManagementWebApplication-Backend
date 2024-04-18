package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineRequest {

    @Positive(message = "Ngày hết hạn không hợp lệ")
    private Long deadline;

    @Positive(message = "Ngày nhắc nhở không hợp lệ")
    private Integer reminder;

}
