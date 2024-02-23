package com.application.WorkManagement.dto.requests.table;

import com.application.WorkManagement.enums.TableScope;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TableScopeRequest {

    @NotNull(message = "Phạm vi bảng không được bỏ trống")
    private TableScope scope;

}
