package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.table.TableScopeRequest;
import com.application.WorkManagement.dto.requests.table.TableUpdatingRequest;
import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.dto.responses.table.TableStarResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TableService {

    TableEntityResponse readTableById(String accountId, UUID tableId) throws DataNotFoundException, CustomAccessDeniedException;

    TableStarResponse tickStar(String accountId, UUID tableId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    List<TableStarResponse> readTableStarList(String accountId) throws DataNotFoundException;

    TableStarResponse readTableStarByTableId(String accountId, UUID tableId) throws DataNotFoundException;

    void deleteTableStar(String accountId, UUID tableId) throws DataNotFoundException;

    TableEntityResponse updateTable(String accountId, UUID tableId, TableUpdatingRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    TableEntityResponse updateScopeTable(String accountId, UUID tableId, TableScopeRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteTable(String accountId, UUID tableId) throws DataNotFoundException, CustomAccessDeniedException;
}
