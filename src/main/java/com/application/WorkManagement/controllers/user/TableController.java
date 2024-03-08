package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.requests.table.*;
import com.application.WorkManagement.dto.responses.table.TableActivityResponse;
import com.application.WorkManagement.dto.responses.table.TableEntityResponse;
import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.dto.responses.table.TableStarResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.services.Interface.TableService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tables")
@SecurityRequirement(
        name = "JWT-BEARER"
)
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<TableEntityResponse> readTableById(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    tableService.readTableById(
                            authentication.getName(),
                            tableId
                    )
                );
    }

    @PostMapping("/stars")
    public ResponseEntity<TableStarResponse> tickStar(
            JwtAuthenticationToken authentication,
            @RequestBody TableStarRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    tableService.tickStar(
                          authentication.getName(),
                          request.getTableId()
                    )
                );
    }

    @GetMapping("/stars")
    public ResponseEntity<List<TableStarResponse>> readTableStarList(
        JwtAuthenticationToken authentication
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.readTableStarList(
                                authentication.getName()
                        )
                );
    }

    @GetMapping("/stars/{tableId}")
    public ResponseEntity<TableStarResponse> readTableStarByTableId(
        JwtAuthenticationToken authentication,
        @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    tableService.readTableStarByTableId(
                            authentication.getName(),
                            tableId
                    )
                );
    }

    @DeleteMapping("/stars/{tableId}")
    public ResponseEntity<Void> deleteTableStar(
        JwtAuthenticationToken authentication,
        @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException {
        tableService.deleteTableStar(authentication.getName(), tableId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{tableId}")
    public ResponseEntity<TableEntityResponse> updateTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId,
            @Valid @RequestBody TableUpdatingRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.updateTable(
                            authentication.getName(),
                                tableId,
                                request
                        )
                );
    }

    @PatchMapping("/{tableId}/scope")
    public ResponseEntity<TableEntityResponse> updateScopeTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId,
            @Valid @RequestBody TableScopeRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.updateScopeTable(
                                authentication.getName(),
                                tableId,
                                request
                        )
                );
    }

    @DeleteMapping("/{tableId}")
    public ResponseEntity<Void> deleteTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        tableService.deleteTable(authentication.getName(), tableId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{tableId}/members")
    public ResponseEntity<TableMemberResponse> inviteMemberIntoTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId,
            @Valid @RequestBody TableMemberRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.inviteMemberIntoTable(
                                authentication.getName(),
                                tableId,
                                request
                        )
                );
    }

    @PostMapping("/{tableId}/members/participation")
    public ResponseEntity<TableMemberResponse> joinInTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException, CustomDuplicateException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.joinInTable(
                            authentication.getName(),
                            tableId
                        )
                );
    }

    @GetMapping("/{tableId}/members")
    public ResponseEntity<List<TableMemberResponse>> readTableMemberList(
        JwtAuthenticationToken authentication,
        @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.readTableMemberList(
                                authentication.getName(),
                                tableId
                        )
                );
    }

    @PatchMapping("/{tableId}/members")
    public ResponseEntity<TableMemberResponse> updateRoleForMember(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId,
            @Valid @RequestBody TableMemberRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.updateRoleForMember(
                                authentication.getName(),
                                tableId,
                                request
                        )
                );
    }

    @DeleteMapping("/{tableId}/members/{memberId}")
    public ResponseEntity<Void> deleteMemberFromTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId,
            @PathVariable("memberId") UUID memberId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        tableService.deleteMemberFromTable(
                authentication.getName(),
                tableId,
                memberId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{tableId}/activities")
    public ResponseEntity<List<TableActivityResponse>> readActivitiesInTable(
            JwtAuthenticationToken authentication,
            @PathVariable("tableId") UUID tableId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.readActivitiesInTable(
                                authentication.getName(),
                                tableId
                        )
                );
    }

    @PatchMapping("/{tableId}/background")
    public ResponseEntity<TableEntityResponse> updateBackgroundTable(
            @PathVariable("tableId") UUID tableId,
            JwtAuthenticationToken authentication,
            @Valid @RequestBody ImageGalleryRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.updateBackgroundTable(
                                authentication.getName(),
                                tableId,
                                request
                        )
                );
    }

    @DeleteMapping("/{tableId}/background")
    public ResponseEntity<TableEntityResponse> deleteBackgroundTable(
            @PathVariable("tableId") UUID tableId,
            JwtAuthenticationToken authentication
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        tableService.deleteBackgroundTable(
                                authentication.getName(),
                                tableId
                        )
                );
    }
}
