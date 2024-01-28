package com.application.WorkManagement.dto.mappers.table;

import com.application.WorkManagement.dto.responses.table.TableListResponse;
import com.application.WorkManagement.entities.TableEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TableListMapper implements Function<TableEntity, TableListResponse> {


    @Override
    public TableListResponse apply(TableEntity table) {
        return TableListResponse
                .builder()
                .id(table.getUuid())
                .name(table.getName())
                .description(table.getDescription())
                .scope(table.getTableScope())
                .background(table.getBackground())
                .workspaceId(table.getWorkspace().getUuid())
                .createdAt(table.getCreatedAt())
                .build();
    }

}
