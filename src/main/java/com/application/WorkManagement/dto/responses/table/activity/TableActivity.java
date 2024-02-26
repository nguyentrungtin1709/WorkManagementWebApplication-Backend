package com.application.WorkManagement.dto.responses.table.activity;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableActivity {

    private UUID id;

    private String name;

}
