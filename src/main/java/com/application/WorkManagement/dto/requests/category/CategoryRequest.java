package com.application.WorkManagement.dto.requests.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Không được bỏ trống")
    @Length(max = 120, message = "Tên danh mục không được vượt quá 120 ký tự")
    private String name;

}
