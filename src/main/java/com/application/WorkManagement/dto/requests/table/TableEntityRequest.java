package com.application.WorkManagement.dto.requests.table;

import com.application.WorkManagement.enums.TableScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableEntityRequest {

    @NotBlank(message = "Không được bỏ trống")
    @Length(max = 120, message = "Tên không gian làm việc không được vượt quá 120 ký tự")
    private String name;

    @Length(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @NotNull(message = "Phạm vi bảng không được bỏ trống")
    private TableScope scope;

}
