package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.workspace.InviteCodeRequest;
import com.application.WorkManagement.dto.requests.workspace.MemberRequest;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.workspace.InviteCodeResponse;
import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.enums.WorkspaceRole;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceResponse createWorkspace(String accountId, WorkspaceRequest request) throws DataNotFoundException;

    List<WorkspaceResponse> readWorkspaceList(String accountId) throws DataNotFoundException;

    WorkspaceResponse readWorkspaceById(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException;

    WorkspaceResponse updateWorkspaceById(String accountId, UUID workspaceId, WorkspaceRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteWorkspaceById(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException;

    MemberResponse addMemberFromEmail(String accountId, UUID workspaceId, MemberRequest request) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    InviteCodeResponse createInviteCodeToJoinInWorkspace(String accountId, UUID workspaceId, InviteCodeRequest request) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    InviteCodeResponse readInviteCode(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteInviteCode(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException;

    WorkspaceResponse checkInviteCode(UUID workspaceId, UUID inviteCode) throws DataNotFoundException;

    WorkspaceResponse joinInWorkspaceFromInviteCode(String accountId, UUID workspaceId, UUID inviteCode) throws DataNotFoundException, CustomDuplicateException;

    List<MemberResponse> readMemberListOfWorkspace(String accountId, UUID workspaceId, String keyword) throws DataNotFoundException, CustomAccessDeniedException;

    MemberResponse updateRoleOfMemberInWorkspace(String accountId, UUID memberId, UUID workspaceId, InviteCodeRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteMemberInWorkspace(String accountId, UUID workspaceId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException;

}
