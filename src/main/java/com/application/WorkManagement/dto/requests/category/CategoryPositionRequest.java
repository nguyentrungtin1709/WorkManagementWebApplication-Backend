package com.application.WorkManagement.dto.requests.category;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPositionRequest {

    @Positive(message = "Vị trí cần lớn hơn 0")
    private Integer position;

}
