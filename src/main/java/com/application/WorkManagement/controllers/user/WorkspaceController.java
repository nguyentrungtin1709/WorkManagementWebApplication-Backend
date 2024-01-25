package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.requests.workspace.MemberRequest;
import com.application.WorkManagement.dto.requests.workspace.WorkspaceRequest;
import com.application.WorkManagement.dto.responses.workspace.MemberResponse;
import com.application.WorkManagement.dto.responses.workspace.WorkspaceResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.services.Interface.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            JwtAuthenticationToken authentication,
            @Valid @RequestBody
            WorkspaceRequest workspaceRequest
    ) throws DataNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    workspaceService.createWorkspace(
                            authentication.getName(),
                            workspaceRequest
                    )
                );
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> readWorkspaceList(
            JwtAuthenticationToken authentication
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        workspaceService.readWorkspaceList(
                                authentication.getName()
                        )
                );
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> readWorkspaceById(
            JwtAuthenticationToken authentication,
            @PathVariable("workspaceId") UUID workspaceId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        workspaceService.readWorkspaceById(
                                authentication.getName(),
                                workspaceId
                        )
                );
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            JwtAuthenticationToken authentication,
            @PathVariable("workspaceId") UUID workspaceId,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        workspaceService.updateWorkspaceById(
                                authentication.getName(),
                                workspaceId,
                                workspaceRequest
                        )
                );
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
        JwtAuthenticationToken authentication,
        @PathVariable("workspaceId") UUID workspaceId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        workspaceService.deleteWorkspaceById(
                authentication.getName(),
                workspaceId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{workspaceId}/members")
    public ResponseEntity<MemberResponse> addMemberFromEmail(
            JwtAuthenticationToken authentication,
            @PathVariable("workspaceId") UUID workspaceId,
            @Valid @RequestBody
            MemberRequest request
    ) throws DataNotFoundException, CustomDuplicateException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    workspaceService.addMemberFromEmail(
                            authentication.getName(),
                            workspaceId,
                            request
                    )
                );
    }
}
