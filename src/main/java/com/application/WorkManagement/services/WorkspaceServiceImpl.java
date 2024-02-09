package com.application.WorkManagement.services;

import com.application.WorkManagement.dto.mappers.table.TableEntityMapper;
import com.application.WorkManagement.dto.mappers.workspace.InviteCodeMapper;
import com.application.WorkManagement.dto.mappers.workspace.WorkspaceInviteCodeMapper;
import com.application.WorkManagement.dto.mappers.workspace.WorkspaceMapper;
import com.application.WorkManagement.dto.mappers.workspace.WorkspaceMemberMapper;
import com.application.WorkManagement.dto.requests.table.TableEntityRequest;
import com.application.WorkManagement.dto.requests.workspace.InviteCodeRequest;
import com.application.WorkManagement.dto.requests.workspace.MemberRequest;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.dto.responses.workspace.InviteCodeResponse;
import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.entities.*;
import com.application.WorkManagement.enums.TableRole;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.*;
import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.UploadImageService;
import com.application.WorkManagement.services.Interface.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final AccountRepository accountRepository;

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final WorkspaceInviteCodeRepository workspaceInviteCodeRepository;

    private final TableRepository tableRepository;

    private final TableMemberRepository tableMemberRepository;

    private final WorkspacePermissionChecker workspacePermissionChecker;

    private final TableEntityMapper tableEntityMapper;

    private final WorkspaceMapper workspaceMapper;

    private final WorkspaceMemberMapper workspaceMemberMapper;

    private final InviteCodeMapper inviteCodeMapper;

    private final WorkspaceInviteCodeMapper workspaceInviteCodeMapper;

    private final UploadImageService uploadImageService;

    @Autowired
    public WorkspaceServiceImpl(
            AccountRepository accountRepository,
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            WorkspaceInviteCodeRepository workspaceInviteCodeRepository,
            TableRepository tableRepository,
            TableMemberRepository tableMemberRepository, WorkspacePermissionChecker workspacePermissionChecker,
            TableEntityMapper tableEntityMapper,
            WorkspaceMapper workspaceMapper,
            WorkspaceMemberMapper workspaceMemberMapper,
            InviteCodeMapper inviteCodeMapper,
            WorkspaceInviteCodeMapper workspaceInviteCodeMapper,
            UploadImageService uploadImageService
    ) {
        this.accountRepository = accountRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceInviteCodeRepository = workspaceInviteCodeRepository;
        this.tableRepository = tableRepository;
        this.tableMemberRepository = tableMemberRepository;
        this.workspacePermissionChecker = workspacePermissionChecker;
        this.tableEntityMapper = tableEntityMapper;
        this.workspaceMapper = workspaceMapper;
        this.workspaceMemberMapper = workspaceMemberMapper;
        this.inviteCodeMapper = inviteCodeMapper;
        this.workspaceInviteCodeMapper = workspaceInviteCodeMapper;
        this.uploadImageService = uploadImageService;
    }

    @Override
    public WorkspaceResponse createWorkspace(
            String accountId,
            WorkspaceRequest request
    ) throws DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = workspaceRepository.save(
                Workspace
                    .builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .account(account)
                    .build()
        );
        WorkspaceMember workspaceMember = workspaceMemberRepository.save(
                WorkspaceMember
                    .builder()
                        .account(account)
                        .workspace(workspace)
                        .workspaceRole(WorkspaceRole.ADMIN)
                        .build()
        );
        return workspaceMapper.apply(workspaceMember);
    }

    @Override
    public List<WorkspaceResponse> readWorkspaceList(String accountId) throws DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        return workspaceMemberRepository
                .readWorkspaceMembersByAccountOrderByWorkspace_Name(account)
                .stream()
                .map(workspaceMapper)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceResponse readWorkspaceById(
            String accountId,
            UUID workspaceId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        Optional<WorkspaceMember> member = workspaceMemberRepository
                .readWorkspaceMemberByAccountAndWorkspace(account, workspace);
        return workspaceMapper
                .apply(
                        member.orElseThrow(() -> new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối"))
                );
    }

    @Override
    public WorkspaceResponse updateWorkspaceById(
            String accountId,
            UUID workspaceId,
            WorkspaceRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace = workspaceRepository.save(workspace);
        return workspaceMapper.apply(
                workspaceMemberRepository
                        .readWorkspaceMemberByAccountAndWorkspace(account, workspace)
                        .orElseThrow()
        );
    }

    @Override
    public void deleteWorkspaceById(
            String accountId,
            UUID workspaceId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        workspacePermissionChecker
                .checkAdminPermissionInWorkspace(
                        getAccountFromAuthenticationName(accountId),
                        getWorkspaceFromId(workspaceId)
                );
        workspaceRepository.deleteById(workspaceId);
    }

    @Override
    public MemberResponse addMemberFromEmail(
            String accountId,
            UUID workspaceId,
            MemberRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account member = accountRepository
                .findAccountByEmail(request.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Tài khoản không tồn tại"));
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(
                getAccountFromAuthenticationName(accountId),
                workspace
        );
        if (
            workspaceMemberRepository.existsByAccountAndWorkspace(member, workspace)
        ){
            throw new CustomDuplicateException("Tài khoản đã là thành viên");
        }
        WorkspaceMember workspaceMember = workspaceMemberRepository.save(
                WorkspaceMember
                        .builder()
                        .account(member)
                        .workspace(workspace)
                        .workspaceRole(request.getRole())
                        .build()
        );
        return workspaceMemberMapper
                .apply(workspaceMember);
    }

    @Override
    public InviteCodeResponse createInviteCodeToJoinInWorkspace(
            String accountId,
            UUID workspaceId,
            InviteCodeRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        if (
            workspaceInviteCodeRepository.existsByAccountAndWorkspace(account, workspace)
        ){
            throw new CustomDuplicateException("Tài khoản đã tạo mã mời");
        }
        return inviteCodeMapper.apply(
                workspaceInviteCodeRepository.save(
                        WorkspaceInviteCode
                                .builder()
                                .account(account)
                                .workspace(workspace)
                                .workspaceRole(request.getRole())
                                .inviteCode(UUID.randomUUID())
                                .build()
                )
        );
    }

    @Override
    public InviteCodeResponse readInviteCode(
            String accountId,
            UUID workspaceId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        return inviteCodeMapper.apply(
                getWorkspaceInviteCodeFromAccountAndWorkspace(account, workspace)
        );
    }

    @Override
    @Transactional
    public void deleteInviteCode(
            String accountId,
            UUID workspaceId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        workspaceInviteCodeRepository.deleteWorkspaceInviteCodeByAccountAndWorkspace(account, workspace);
    }

    @Override
    public WorkspaceResponse checkInviteCode(UUID workspaceId, UUID inviteCode) throws DataNotFoundException {
        return workspaceInviteCodeMapper.apply(
                workspaceInviteCodeRepository
                        .findWorkspaceInviteCodeByWorkspace_UuidAndInviteCode(workspaceId, inviteCode)
                        .orElseThrow(() -> new DataNotFoundException("Mã mời không tồn tại"))
        );
    }

    @Override
    public WorkspaceResponse joinInWorkspaceFromInviteCode(
            String accountId,
            UUID workspaceId,
            UUID inviteCode
    ) throws DataNotFoundException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        WorkspaceInviteCode workspaceInviteCode = workspaceInviteCodeRepository
                .findWorkspaceInviteCodeByWorkspace_UuidAndInviteCode(workspaceId, inviteCode)
                .orElseThrow(() -> new DataNotFoundException("Mã mời không tồn tại"));
        if (
            workspaceMemberRepository.existsByAccountAndWorkspace(account, workspace)
        ){
            throw new CustomDuplicateException("Tài khoản đã là thành viên của không gian làm việc");
        }
        WorkspaceMember workspaceMember = workspaceMemberRepository.save(
                WorkspaceMember
                        .builder()
                        .account(account)
                        .workspace(workspace)
                        .workspaceRole(workspaceInviteCode.getWorkspaceRole())
                        .build()

        );
        return workspaceMapper.apply(workspaceMember);
    }

    @Override
    public List<MemberResponse> readMemberListOfWorkspace(
            String accountId,
            UUID workspaceId,
            String keyword
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkHasAnyPermissionInWorkspace(account, workspace);
        if (keyword == null){
            return workspaceMemberRepository
                    .readWorkspaceMembersByWorkspaceOrderByWorkspaceRoleDesc(workspace)
                    .stream()
                    .map(workspaceMemberMapper)
                    .collect(Collectors.toList());
        }
        return workspaceMemberRepository
                .readWorkspaceMembersByWorkspace_UuidAndAccount_NameLikeKeywordDESC(workspaceId, keyword)
                .stream()
                .map(workspaceMemberMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(
            rollbackFor = {NotExistAdminInWorkspaceException.class}
    )
    public MemberResponse updateRoleOfMemberInWorkspace(
            String accountId,
            UUID memberId,
            UUID workspaceId,
            InviteCodeRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, NotExistAdminInWorkspaceException {
        Account member = getAccountFromId(memberId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(
                getAccountFromAuthenticationName(accountId),
                workspace
        );
        WorkspaceMember workspaceMember = workspaceMemberRepository
                .readWorkspaceMemberByAccountAndWorkspace(member, workspace)
                .orElseThrow(() -> new DataNotFoundException("Tài khoản không là thành viên của không gian làm việc"));
        workspaceMember.setWorkspaceRole(request.getRole());
        MemberResponse response = workspaceMemberMapper.apply(
                workspaceMemberRepository.save(workspaceMember)
        );
        checkNotExistAdminInWorkspaceException(workspace);
        return response;
    }

    @Override
    @Transactional(
            rollbackFor = {NotExistAdminInWorkspaceException.class}
    )
    public void deleteMemberInWorkspace(
            String accountId,
            UUID workspaceId,
            UUID memberId
    ) throws DataNotFoundException, CustomAccessDeniedException, NotExistAdminInWorkspaceException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        Account member = getAccountFromId(memberId);
        if (account.getUuid().equals(member.getUuid())){
            workspacePermissionChecker.checkHasAnyPermissionInWorkspace(account, workspace);
        } else {
            workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        }
        workspaceMemberRepository.deleteWorkspaceMemberByAccount_UuidAndWorkspace_UuidCustom(
                member.getUuid(),
                workspace.getUuid()
        );
        workspaceMemberRepository.deleteInviteCodeByAccount_UuidAndWorkspace_Uuid(
                member.getUuid(),
                workspace.getUuid()
        );
        workspaceMemberRepository.deleteTableStarByAccount_UuidAndWorkspace_Uuid(
                member.getUuid(),
                workspace.getUuid()
        );
        workspaceMemberRepository.deleteTableMemberByAccount_UuidAndWorkspace_Uuid(
                member.getUuid(),
                workspace.getUuid()
        );
        workspaceMemberRepository.deleteCardMemberByAccount_UuidAndWorkspace_Uuid(
                member.getUuid(),
                workspace.getUuid()
        );
        workspaceMemberRepository.deleteCardFollowByAccount_UuidAndWorkspace_Uuid(
                member.getUuid(),
                workspace.getUuid()
        );
        checkNotExistAdminInWorkspaceException(workspace);
    }

    @Override
    public TableEntityResponse createTableInWorkspace(
            String accountId,
            UUID workspaceId,
            TableEntityRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkMemberPermissionInWorkspace(account, workspace);
        if (
            tableRepository.existsTableEntityByWorkspaceAndName(workspace, request.getName())
        ){
            throw new CustomDuplicateException("Bảng đã tồn tại trong không gian làm việc");
        }
        TableEntity table = tableRepository.save(
                TableEntity
                    .builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .tableScope(request.getScope())
                    .account(account)
                    .workspace(workspace)
                    .build()
        );
        TableMember tableMember = tableMemberRepository.save(
                TableMember
                        .builder()
                        .account(account)
                        .table(table)
                        .tableRole(TableRole.ADMIN)
                        .build()
        );
        return tableEntityMapper.apply(tableMember);
    }

    @Override
    public List<TableEntityResponse> readTablesInWorkspace(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkHasAnyPermissionInWorkspace(account, workspace);
        if (
            workspaceMemberRepository.existsWorkspaceMemberByAccountAndWorkspaceAndWorkspaceRoleIn(
                    account,
                    workspace,
                    List.of(WorkspaceRole.ADMIN)
            )
        ){
            return tableRepository
                    .findTableEntitiesByWorkspace(workspace)
                    .stream()
                    .map(table -> {
                        Optional<TableMember> member = tableMemberRepository
                                .findTableMemberByAccountAndTable(account, table);
                        if (member.isPresent()){
                            return member.orElseThrow();
                        }
                        return TableMember
                                .builder()
                                .account(account)
                                .table(table)
                                .tableRole(null)
                                .build();
                    })
                    .map(tableEntityMapper)
                    .collect(Collectors.toList());
        }
        return tableRepository
                .findTableEntitiesInWorkspace(
                        workspace.getUuid(),
                        account.getUuid()
                )
                .stream()
                .map(table -> {
                    Optional<TableMember> member = tableMemberRepository
                            .findTableMemberByAccountAndTable(account, table);
                    if (member.isPresent()){
                        return member.orElseThrow();
                    }
                    return TableMember
                            .builder()
                            .account(account)
                            .table(table)
                            .tableRole(null)
                            .build();
                })
                .map(tableEntityMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = {
            IOException.class,
            URISyntaxException.class,
            InvalidFileExtensionException.class,
            EmptyImageException.class
    })
    public WorkspaceResponse updateWorkspaceBackground(
            String accountId,
            UUID workspaceId,
            MultipartFile file
    ) throws DataNotFoundException, CustomAccessDeniedException, URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        if (workspace.getBackground() != null){
            uploadImageService.removeFileFromS3(workspace.getBackground());
        }
        URI backgroundUri = uploadImageService.uploadImageToS3(file);
        workspace.setBackground(backgroundUri);
        workspace = workspaceRepository.save(workspace);
        return workspaceMapper.apply(
                workspaceMemberRepository
                        .readWorkspaceMemberByAccountAndWorkspace(account, workspace)
                        .orElseThrow()
        );
    }

    @Override
    public WorkspaceResponse deleteWorkspaceBackground(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException, URISyntaxException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        workspacePermissionChecker.checkAdminPermissionInWorkspace(account, workspace);
        if (workspace.getBackground() != null){
            uploadImageService.removeFileFromS3(workspace.getBackground());
        }
        workspace.setBackground(null);
        workspace = workspaceRepository.save(workspace);
        return workspaceMapper.apply(
                workspaceMemberRepository
                        .readWorkspaceMemberByAccountAndWorkspace(account, workspace)
                        .orElseThrow()
        );
    }

    private Account getAccountFromAuthenticationName(String uuid) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    private Account getAccountFromId(UUID uuid) throws DataNotFoundException {
        return accountRepository
                .findById(uuid)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }


    private Workspace getWorkspaceFromId(UUID workspaceId) throws DataNotFoundException {
        return workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy không gian làm việc"));
    }

    private WorkspaceInviteCode getWorkspaceInviteCodeFromAccountAndWorkspace(
            Account account,
            Workspace workspace
    ) throws DataNotFoundException {
        return workspaceInviteCodeRepository
                .findWorkspaceInviteCodeByAccountAndWorkspace(account, workspace)
                .orElseThrow(() -> new DataNotFoundException("Mã mời không tồn tại"));

    }

    private void checkNotExistAdminInWorkspaceException(
            Workspace workspace
    ) throws NotExistAdminInWorkspaceException {
        if (
                !workspaceMemberRepository.existsByWorkspaceAndWorkspaceRole(workspace, WorkspaceRole.ADMIN)
        ){
            throw new NotExistAdminInWorkspaceException("Không gian làm việc phải có ít nhật một quản trị viên");
        }
    }
}
