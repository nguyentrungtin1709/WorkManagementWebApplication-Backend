package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.workspace.MemberRequest;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
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
}
