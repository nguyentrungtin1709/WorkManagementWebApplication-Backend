package com.application.WorkManagement.services.table;

import com.application.WorkManagement.dto.mappers.table.TableEntityMapper;
import com.application.WorkManagement.dto.mappers.table.TableMemberMapper;
import com.application.WorkManagement.dto.mappers.table.TableStarMapper;
import com.application.WorkManagement.dto.requests.table.TableMemberRequest;
import com.application.WorkManagement.dto.requests.table.TableScopeRequest;
import com.application.WorkManagement.dto.requests.table.TableUpdatingRequest;
import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.dto.responses.table.TableStarResponse;
import com.application.WorkManagement.entities.*;
import com.application.WorkManagement.enums.ActivityType;
import com.application.WorkManagement.enums.TableRole;
import com.application.WorkManagement.enums.TableScope;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    private final TableMemberRepository tableMemberRepository;

    private final TableStarRepository tableStarRepository;

    private final AccountRepository accountRepository;

    private final ActivityRepository activityRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final TableEntityMapper tableEntityMapper;

    private final TableStarMapper tableStarMapper;

    private final TableMemberMapper tableMemberMapper;

    @Autowired
    public TableServiceImpl(
            TableRepository tableRepository,
            TableMemberRepository tableMemberRepository,
            TableStarRepository tableStarRepository,
            AccountRepository accountRepository,
            ActivityRepository activityRepository,
            TablePermissionChecker tablePermissionChecker,
            TableEntityMapper tableEntityMapper, TableStarMapper tableStarMapper, TableMemberMapper tableMemberMapper
    ) {
        this.tableRepository = tableRepository;
        this.tableMemberRepository = tableMemberRepository;
        this.tableStarRepository = tableStarRepository;
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.tableEntityMapper = tableEntityMapper;
        this.tableStarMapper = tableStarMapper;
        this.tableMemberMapper = tableMemberMapper;
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

    @Override
    public TableEntityResponse updateTable(
            String accountId,
            UUID tableId,
            TableUpdatingRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkManagePermission(account, table);
        table.setName(request.getName());
        table.setDescription(request.getDescription());
        table = tableRepository.save(table);
        return tableEntityMapper
                .apply(
                        tableMemberRepository
                                .findTableMemberByAccountAndTable(account, table)
                                .orElseThrow()
                );
    }

    @Override
    @Transactional
    public TableEntityResponse updateScopeTable(
            String accountId,
            UUID tableId,
            TableScopeRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkManagePermission(account, table);
        table.setTableScope(request.getScope());
        table = tableRepository.save(table);
        if (table.getTableScope()
                .equals(TableScope.GROUP)
        ){
            tableStarRepository.findTableStarsByTable(table)
                    .forEach(tableStar -> {
                        if (!tableMemberRepository
                                .existsTableMemberByAccountAndTable(tableStar.getAccount(), tableStar.getTable())
                        ){
                            tableStarRepository.deleteTableStarByAccountAndTable(tableStar.getAccount(), tableStar.getTable());
                        }
                    });
        }
        activityRepository.save(
                Activity
                .builder()
                .activityType(ActivityType.CHANGE_SCOPE_TABLE)
                .account(account)
                .table(table)
                .category(null)
                .card(null)
                .build()
        );
        return tableEntityMapper.apply(
                tableMemberRepository
                        .findTableMemberByAccountAndTable(account, table)
                        .orElseThrow()
        );
    }

    @Override
    @Transactional
    public void deleteTable(
            String accountId,
            UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkManagePermission(account, table);
        tableRepository.deleteById(table.getUuid());
    }

    @Override
    public TableMemberResponse inviteMemberIntoTable(
            String accountId,
            UUID tableId,
            TableMemberRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        Account member = getAccountFromId(request.getAccountId());
        tablePermissionChecker.checkManagePermission(account, table);
        tablePermissionChecker.checkAccountIsMemberOfWorkspaceContainTable(member, table);
        checkAccountHaveBeenMemberOfTable(member, table);
        return tableMemberMapper.apply(
                tableMemberRepository.save(
                        TableMember.builder()
                                .account(member)
                                .table(table)
                                .tableRole(request.getRole())
                                .build()
                )
        );
    }

    @Override
    public TableMemberResponse joinInTable(
            String accountId,
            UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkJoinInPermission(account, table);
        checkAccountHaveBeenMemberOfTable(account, table);
        return tableMemberMapper.apply(
                tableMemberRepository.save(
                        TableMember
                                .builder()
                                .account(account)
                                .table(table)
                                .tableRole(TableRole.ADMIN)
                                .build()
                )
        );
    }

    @Override
    public List<TableMemberResponse> readTableMemberList(
            String accountId,
            UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        tablePermissionChecker.checkReadPermission(account, table);
        return tableMemberRepository
                .findTableMembersByTableOrderByTableRoleDesc(table)
                .stream()
                .map(tableMemberMapper)
                .collect(Collectors.toList());
    }

    @Override
    public TableMemberResponse updateRoleForMember(String accountId, UUID tableId, TableMemberRequest request) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        Account member = getAccountFromId(request.getAccountId());
        tablePermissionChecker.checkManagePermission(account, table);
        TableMember tableMember = tableMemberRepository
                .findTableMemberByAccountAndTable(member, table)
                .orElseThrow(() -> new DataNotFoundException("Tài khoản không là thành viên của bảng"));
        tableMember.setTableRole(request.getRole());
        return tableMemberMapper.apply(
                tableMemberRepository.save(tableMember)
        );
    }

    @Override
    @Transactional
    public void deleteMemberFromTable(
            String accountId,
            UUID tableId,
            UUID memberId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        TableEntity table = getTableFromId(tableId);
        Account member = getAccountFromId(memberId);
        if (!account.getUuid().equals(member.getUuid())){
            tablePermissionChecker.checkManagePermission(account, table);
        }
        tableMemberRepository.deleteTableMemberByAccountAndTable(member, table);
        tableStarRepository.deleteTableStarByAccountAndTable(member, table);
        tableMemberRepository.deleteCardMemberByAccount_UuidAndTable_Uuid(member.getUuid(), table.getUuid());
        tableMemberRepository.deleteCardFollowByAccount_UuidAndTable_Uuid(member.getUuid(), table.getUuid());
    }


    private Account getAccountFromAuthenticationName(String uuid) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    private Account getAccountFromId(UUID accountId) throws DataNotFoundException {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    public TableEntity getTableFromId(UUID tableId) throws DataNotFoundException {
        return tableRepository
                .findById(tableId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bảng công việc"));
    }

    private void checkAccountHaveBeenMemberOfTable(Account account, TableEntity table) throws CustomDuplicateException {
        Boolean isMember = tableMemberRepository.existsTableMemberByAccountAndTable(account, table);
        if (isMember){
            throw new CustomDuplicateException("Tài khoản đã là thành viên của bảng");
        }
    }
}
