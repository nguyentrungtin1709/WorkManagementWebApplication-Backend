package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.table.TableEntityRequest;
import com.application.WorkManagement.dto.requests.workspace.InviteCodeRequest;
import com.application.WorkManagement.dto.requests.workspace.MemberRequest;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.dto.responses.workspace.InviteCodeResponse;
import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.enums.TableSortType;
import com.application.WorkManagement.exceptions.custom.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
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

    MemberResponse updateRoleOfMemberInWorkspace(String accountId, UUID memberId, UUID workspaceId, InviteCodeRequest request) throws DataNotFoundException, CustomAccessDeniedException, NotExistAdminInWorkspaceException;

    void deleteMemberInWorkspace(String accountId, UUID workspaceId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException, NotExistAdminInWorkspaceException;

    TableEntityResponse createTableInWorkspace(String accountId, UUID workspaceId, TableEntityRequest request) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    List<TableEntityResponse> readTablesInWorkspace(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException;

    WorkspaceResponse updateWorkspaceBackground(String accountId, UUID workspaceId, MultipartFile file) throws DataNotFoundException, CustomAccessDeniedException, URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException;

    WorkspaceResponse deleteWorkspaceBackground(String accountId, UUID workspaceId) throws DataNotFoundException, CustomAccessDeniedException, URISyntaxException;

    List<TableEntityResponse> readTableListInWorkspaceByKeywordAndSortType(String accountId, UUID workspaceId, String keyword, TableSortType sortType) throws DataNotFoundException, CustomAccessDeniedException;

}
