package com.application.WorkManagement.dto.requests.category;

import com.application.WorkManagement.enums.CategoryColor;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryColorRequest {

    @NotNull(message = "Màu nền không được bỏ trống")
    private CategoryColor color;

}
