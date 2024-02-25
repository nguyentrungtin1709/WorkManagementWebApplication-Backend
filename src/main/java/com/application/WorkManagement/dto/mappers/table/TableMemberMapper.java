package com.application.WorkManagement.dto.mappers.table;

import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.entities.TableMember;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TableMemberMapper implements Function<TableMember, TableMemberResponse> {

    @Override
    public TableMemberResponse apply(TableMember tableMember) {
        return TableMemberResponse
                .builder()
                .id(tableMember.getAccount().getUuid())
                .name(tableMember.getAccount().getName())
                .email(tableMember.getAccount().getEmail())
                .avatar(tableMember.getAccount().getAvatar())
                .role(tableMember.getTableRole())
                .build();
    }

}
