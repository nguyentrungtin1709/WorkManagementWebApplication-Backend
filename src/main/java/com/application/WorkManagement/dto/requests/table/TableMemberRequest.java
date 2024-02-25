package com.application.WorkManagement.dto.requests.table;

import com.application.WorkManagement.enums.TableRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TableMemberRequest {

    @NotNull(message = "Không được bỏ trống")
    private UUID accountId;

    @NotNull(message = "Không được bỏ trống")
    private TableRole role;

}
