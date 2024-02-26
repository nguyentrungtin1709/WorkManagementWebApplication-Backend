package com.application.WorkManagement.dto.responses.table;

import com.application.WorkManagement.dto.responses.table.activity.AccountActivity;
import com.application.WorkManagement.dto.responses.table.activity.CardActivity;
import com.application.WorkManagement.dto.responses.table.activity.CategoryActivity;
import com.application.WorkManagement.dto.responses.table.activity.TableActivity;
import com.application.WorkManagement.enums.ActivityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableActivityResponse {

    private UUID id;

    private AccountActivity account;

    private TableActivity table;

    private CategoryActivity category;

    private CardActivity card;

    private ActivityType type;

    private String message;

    private LocalDateTime createdAt;

}
