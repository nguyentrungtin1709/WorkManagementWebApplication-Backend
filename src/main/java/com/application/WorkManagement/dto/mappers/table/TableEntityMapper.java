package com.application.WorkManagement.dto.mappers.table;

import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.entities.TableMember;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TableEntityMapper implements Function<TableMember, TableEntityResponse> {

    @Override
    public TableEntityResponse apply(TableMember member) {
        return TableEntityResponse
                .builder()
                .id(member.getTable().getUuid())
                .name(member.getTable().getName())
                .description(member.getTable().getDescription())
                .scope(member.getTable().getTableScope())
                .background(member.getTable().getBackground())
                .role(member.getTableRole())
                .workspaceId(member.getTable().getWorkspace().getUuid())
                .createdAt(member.getTable().getCreatedAt())
                .build();
    }

}
