package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PositionUpdateRequest {

    @NotNull(message = "Mã danh mục không được bỏ trống")
    private UUID categoryId;

    @NotNull(message = "Vị trí không được bỏ trống")
    @Positive(message = "Ví trí phải lớn hơn 0")
    private Integer location;

}
