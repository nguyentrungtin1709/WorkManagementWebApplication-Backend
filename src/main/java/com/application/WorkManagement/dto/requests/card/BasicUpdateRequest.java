package com.application.WorkManagement.dto.requests.card;

import com.application.WorkManagement.enums.CardProgress;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicUpdateRequest {

    @Length(max = 120, message = "Tên thẻ không được vượt quá 120 ký tự")
    private String name;

    @Length(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    private CardProgress progress;

}
