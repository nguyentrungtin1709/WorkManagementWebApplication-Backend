package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    @NotBlank(message = "Không được bỏ trống")
    @Length(max = 120, message = "Tên thẻ không được vượt quá 120 ký tự")
    private String name;

}
