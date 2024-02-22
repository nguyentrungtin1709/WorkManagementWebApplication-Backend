package com.application.WorkManagement.dto.mappers.table;

import com.application.WorkManagement.dto.responses.table.TableStarResponse;
import com.application.WorkManagement.entities.TableStar;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TableStarMapper implements Function<TableStar, TableStarResponse> {


    @Override
    public TableStarResponse apply(TableStar tableStar) {
        return TableStarResponse
                .builder()
                .id(tableStar.getTable().getUuid())
                .name(tableStar.getTable().getName())
                .background(tableStar.getTable().getBackground())
                .workspaceId(tableStar.getTable().getWorkspace().getUuid())
                .build();
    }
}
