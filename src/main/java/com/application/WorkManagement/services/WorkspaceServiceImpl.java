package com.application.WorkManagement.services;

import com.application.WorkManagement.dto.mappers.WorkspaceMapper;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Workspace;
import com.application.WorkManagement.entities.WorkspaceMember;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
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

    @Autowired
    public WorkspaceServiceImpl(
            AccountRepository accountRepository,
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            WorkspaceInviteCodeRepository workspaceInviteCodeRepository,
            WorkspaceMapper workspaceMapper
    ) {
        this.accountRepository = accountRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceInviteCodeRepository = workspaceInviteCodeRepository;
        this.workspaceMapper = workspaceMapper;
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
        if (member.isEmpty()){
            throw new CustomAccessDeniedException("Truy cập tài nguyên bị từ chối");
        }
        return workspaceMapper.apply(member.orElseThrow());
    }

    @Override
    public WorkspaceResponse updateWorkspaceById(
            String accountId,
            UUID workspaceId,
            WorkspaceRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Workspace workspace = getWorkspaceFromId(workspaceId);
        WorkspaceMember member = checkAdminPermissionInWorkspace(account, workspace);
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
        WorkspaceMember member = checkAdminPermissionInWorkspace(
                getAccountFromAuthenticationName(accountId),
                getWorkspaceFromId(workspaceId)
        );
        workspaceRepository.deleteById(workspaceId);
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

    private WorkspaceMember checkPermissionInWorkspace(
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
        return member;
    }

    private WorkspaceMember checkAdminPermissionInWorkspace(
            Account account,
            Workspace workspace
    ) throws CustomAccessDeniedException {
        return checkPermissionInWorkspace(
                account,
                workspace,
                List.of(WorkspaceRole.ADMIN)
        );
    }
}
