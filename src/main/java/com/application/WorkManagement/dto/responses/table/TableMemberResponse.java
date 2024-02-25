package com.application.WorkManagement.dto.responses.table;

import com.application.WorkManagement.enums.TableRole;
import lombok.*;

import java.net.URI;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableMemberResponse {

    private UUID id;

    private String name;

    private String email;

    private URI avatar;

    private TableRole role;

}
