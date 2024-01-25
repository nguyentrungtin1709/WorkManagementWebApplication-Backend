package com.application.WorkManagement.services;

import com.application.WorkManagement.dto.mappers.workspace.InviteCodeMapper;
import com.application.WorkManagement.dto.mappers.workspace.WorkspaceMapper;
import com.application.WorkManagement.dto.mappers.workspace.WorkspaceMemberMapper;
import com.application.WorkManagement.dto.requests.workspace.InviteCodeRequest;
import com.application.WorkManagement.dto.requests.workspace.MemberRequest;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.workspace.InviteCodeResponse;
import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.entities.WorkspaceInviteCode;
import com.application.WorkManagement.entities.WorkspaceMember;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.repositories.AccountRepository;
import com.application.WorkManagement.repositories.WorkspaceInviteCodeRepository;
import com.application.WorkManagement.repositories.WorkspaceMemberRepository;
import com.application.WorkManagement.repositories.WorkspaceRepository;
import com.application.WorkManagement.services.Interface.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final WorkspaceMapper workspaceMapper;

    private final WorkspaceMemberMapper workspaceMemberMapper;

    private final InviteCodeMapper inviteCodeMapper;

    @Autowired
    public WorkspaceServiceImpl(
            AccountRepository accountRepository,
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            WorkspaceInviteCodeRepository workspaceInviteCodeRepository,
            WorkspaceMapper workspaceMapper,
            WorkspaceMemberMapper workspaceMemberMapper,
            InviteCodeMapper inviteCodeMapper
    ) {
        this.accountRepository = accountRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceInviteCodeRepository = workspaceInviteCodeRepository;
        this.workspaceMapper = workspaceMapper;
        this.workspaceMemberMapper = workspaceMemberMapper;
        this.inviteCodeMapper = inviteCodeMapper;
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
                .readWorkspaceMembersByAccount(account)
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
        checkAdminPermissionInWorkspace(account, workspace);
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
        checkAdminPermissionInWorkspace(
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
        checkAdminPermissionInWorkspace(
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
        checkAdminPermissionInWorkspace(account, workspace);
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

    private Account getAccountFromAuthenticationName(String uuid) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    private Workspace getWorkspaceFromId(UUID workspaceId) throws DataNotFoundException {
        return workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy không gian làm việc"));
    }

    private void checkPermissionInWorkspace(
            Account account,
            Workspace workspace,
            List<WorkspaceRole> workspaceRoleList
    ) throws CustomAccessDeniedException {
        WorkspaceMember member = workspaceMemberRepository
                .readWorkspaceMemberByAccountAndWorkspace(account, workspace)
                .orElseThrow(() -> new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối"));
        if (!workspaceRoleList.contains(member.getWorkspaceRole())){
            throw new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối");
        }
    }

    private void checkAdminPermissionInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        checkPermissionInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.ADMIN)
        );
    }

}
