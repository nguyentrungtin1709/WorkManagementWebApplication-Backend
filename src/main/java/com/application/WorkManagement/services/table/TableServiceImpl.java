package com.application.WorkManagement.services.table;

import com.application.WorkManagement.dto.mappers.table.TableEntityMapper;
import com.application.WorkManagement.dto.mappers.table.TableStarMapper;
import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.dto.responses.table.TableStarResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.TableEntity;
import com.application.WorkManagement.entities.TableMember;
import com.application.WorkManagement.entities.TableStar;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.TableService;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final TableRepository tableRepository;

    private final TableMemberRepository tableMemberRepository;

    private final TableStarRepository tableStarRepository;

    private final AccountRepository accountRepository;

    private final ActivityRepository activityRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final TableEntityMapper tableEntityMapper;

    private final TableStarMapper tableStarMapper;

    @Autowired
    public TableServiceImpl(
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            TableRepository tableRepository,
            TableMemberRepository tableMemberRepository,
            TableStarRepository tableStarRepository,
            AccountRepository accountRepository,
            ActivityRepository activityRepository,
            TablePermissionChecker tablePermissionChecker,
            TableEntityMapper tableEntityMapper, TableStarMapper tableStarMapper
    ) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.tableRepository = tableRepository;
        this.tableMemberRepository = tableMemberRepository;
        this.tableStarRepository = tableStarRepository;
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.tableEntityMapper = tableEntityMapper;
        this.tableStarMapper = tableStarMapper;
    }


    @Override
    public TableEntityResponse readTableById(
            String accountId,
            UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkReadPermission(account, table);
        Optional<TableMember> tableMemberOptional = tableMemberRepository
                .findTableMemberByAccountAndTable(account, table);
        if (tableMemberOptional.isEmpty()){
            return tableEntityMapper.apply(
                    TableMember
                            .builder()
                            .account(account)
                            .table(table)
                            .tableRole(null)
                            .build()

            );
        }
        return tableEntityMapper.apply(
                tableMemberOptional.orElseThrow()
        );
    }

    @Override
    public TableStarResponse tickStar(String accountId, UUID tableId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkReadPermission(account, table);
        if (
            tableStarRepository
                .findTableStarByAccountAndTable(account, table)
                .isPresent()
        ){
            throw new CustomDuplicateException("Bảng công việc đã được thêm vào danh sách đánh dấu sao");
        }
        return tableStarMapper.apply(
                tableStarRepository.save(
                        TableStar.builder()
                                .account(account)
                                .table(table)
                                .build()
                )
        );
    }

    @Override
    public List<TableStarResponse> readTableStarList(String accountId) throws DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        return tableStarRepository
                .findTableStarsByAccount(account)
                .stream()
                .map(tableStarMapper)
                .collect(Collectors.toList());
    }

    @Override
    public TableStarResponse readTableStarByTableId(String accountId, UUID tableId) throws DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        Optional<TableStar> tableStarOptional = tableStarRepository.findTableStarByAccountAndTable(account, table);
        if (tableStarOptional.isEmpty()){
            throw new DataNotFoundException("Bảng không được đánh dấu sao");
        }
        return tableStarMapper.apply(
                tableStarOptional.orElseThrow()
        );
    }

    @Override
    @Transactional
    public void deleteTableStar(String accountId, UUID tableId) throws DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tableStarRepository.deleteTableStarByAccountAndTable(account, table);
    }

    private Account getAccountFromAuthenticationName(String uuid) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    public TableEntity getTableFromId(UUID tableId) throws DataNotFoundException {
        return tableRepository
                .findById(tableId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bảng công việc"));
    }
}
