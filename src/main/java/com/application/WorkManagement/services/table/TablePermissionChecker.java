package com.application.WorkManagement.services.table;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.enums.TableScope;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TablePermissionChecker {

    private final TableRoleChecker tableRoleChecker;

    public TablePermissionChecker(TableRoleChecker tableRoleChecker) {
        this.tableRoleChecker = tableRoleChecker;
    }

    private Boolean checkTableScope(TableEntity table, TableScope scope){
        return table.getTableScope().equals(scope);
    }

    public void checkReadPermission(Account account, TableEntity table) throws CustomAccessDeniedException {
        if (checkTableScope(table, TableScope.WORKSPACE)){
            tableRoleChecker.checkHasAnyRoleInWorkspace(account, table.getWorkspace());
        } else {
            if (
                !tableRoleChecker.isAdminRoleInWorkspace(account, table.getWorkspace()) &&
                !tableRoleChecker.isAnyRoleInTable(account, table)
            ){
                throw new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối");
            }
        }
    }


}
