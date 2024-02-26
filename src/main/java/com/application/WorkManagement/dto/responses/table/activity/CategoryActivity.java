package com.application.WorkManagement.dto.responses.table.activity;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryActivity {

    private UUID id;

    private String name;

}
