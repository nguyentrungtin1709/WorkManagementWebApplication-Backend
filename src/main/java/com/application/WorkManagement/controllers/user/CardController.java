package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.requests.card.*;
import com.application.WorkManagement.dto.responses.card.CardMemberResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.dto.responses.card.ListResponse;
import com.application.WorkManagement.dto.responses.card.TaskResponse;
import com.application.WorkManagement.dto.responses.card.comment.CardCommentResponse;
import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.exceptions.custom.*;
import com.application.WorkManagement.services.Interface.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@SecurityRequirement(
        name = "JWT-BEARER"
)
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> readCardById(
            JwtAuthenticationToken authenticationToken,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    cardService.readCardById(
                            authenticationToken.getName(),
                            cardId
                    )
                );
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CardResponse> updateCard(
            JwtAuthenticationToken authenticationToken,
            @PathVariable("cardId") UUID cardId,
            @Valid @RequestBody BasicUpdateRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.updateCard(
                                authenticationToken.getName(),
                                cardId,
                                request
                        )
                );
    }

    @PatchMapping("/{cardId}/location")
    public ResponseEntity<CardResponse> updatePositionOfCard(
            JwtAuthenticationToken authenticationToken,
            @PathVariable("cardId") UUID cardId,
            @Valid @RequestBody PositionUpdateRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException, InvalidPositionException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.updatePositionOfCard(
                                authenticationToken.getName(),
                                cardId,
                                request
                        )
                );
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            JwtAuthenticationToken authenticationToken,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.deleteCard(authenticationToken.getName(), cardId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{cardId}/members/{memberId}")
    public ResponseEntity<CardMemberResponse> addMemberToCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("memberId") UUID memberId
    ) throws DataNotFoundException, CustomDuplicateException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.addMemberToCard(
                                token.getName(),
                                cardId,
                                memberId
                        )
                );
    }

    @PostMapping("/{cardId}/members/participation")
    public ResponseEntity<CardMemberResponse> joinInCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomDuplicateException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.joinInCard(
                                token.getName(),
                                cardId
                        )
                );
    }

    @GetMapping("/{cardId}/members/not-in-card")
    public ResponseEntity<List<TableMemberResponse>> readMemberListInTableButNotInCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readMemberListInTableButNotInCard(
                                token.getName(),
                                cardId
                        )
                );
    }

    @GetMapping("/{cardId}/members")
    public ResponseEntity<List<CardMemberResponse>> readMembersListOfCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readMembersListOfCard(
                                token.getName(),
                                cardId
                        )
                );
    }

    @GetMapping("/{cardId}/members/{memberId}")
    public ResponseEntity<CardMemberResponse> readMemberById(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("memberId") UUID memberId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readMemberById(
                                token.getName(),
                                cardId,
                                memberId
                        )
                );
    }

    @DeleteMapping("/{cardId}/members/{memberId}")
    public ResponseEntity<Void> deleteMemberOfCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("memberId") UUID memberId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.deleteMemberOfCard(
                token.getName(),
                cardId,
                memberId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{cardId}/members/leave")
    public ResponseEntity<Void> leaveMembersList(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.leaveMembersList(
                token.getName(),
                cardId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{cardId}/follow-list")
    public ResponseEntity<Void> followCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        cardService.followCard(
                token.getName(),
                cardId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{cardId}/follow-list")
    public ResponseEntity<Void> unfollowCard(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.unfollowCard(
                token.getName(),
                cardId
        );
        return ResponseEntity
                .noContent()
                .build();

    }

    @PostMapping("/{cardId}/deadline")
    public ResponseEntity<Void> setDeadline(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @Valid @RequestBody DeadlineRequest request
    ) throws DataNotFoundException, InvalidDeadlineException, CustomAccessDeniedException, CustomDuplicateException {
        cardService.setDeadline(token.getName(), cardId, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{cardId}/deadline")
    public ResponseEntity<Void> removeDeadline(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.removeDeadline(
                token.getName(),
                cardId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{cardId}/deadline/complete")
    public ResponseEntity<Void> completeDeadline(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @Valid @RequestBody CompleteDeadlineRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.updateCompleteFieldOfDeadline(
                token.getName(),
                cardId,
                request
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{cardId}/comments")
    public ResponseEntity<CardCommentResponse> createComment(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @Valid @RequestBody CommentRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService
                                .createComment(
                                        token.getName(),
                                        cardId,
                                        request
                                )
                );
    }

    @GetMapping("/{cardId}/comments")
    public ResponseEntity<List<CardCommentResponse>> readCommentsList(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    cardService.readCommentsList(
                            token.getName(),
                            cardId
                    )
                );
    }

    @GetMapping("/{cardId}/comments/{commentId}")
    public ResponseEntity<CardCommentResponse> readComment(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("commentId") UUID commentId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readComment(
                                token.getName(),
                                cardId,
                                commentId
                        )
                );
    }

    @PatchMapping("/{cardId}/comments/{commentId}")
    public ResponseEntity<CardCommentResponse> revokeComment(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("commentId") UUID commentId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.revokeComment(
                                token.getName(),
                                cardId,
                                commentId
                        )
                );
    }

    @DeleteMapping("/{cardId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("commentId") UUID commentId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.deleteComment(
                token.getName(),
                cardId,
                commentId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{cardId}/to-do-list")
    public ResponseEntity<ListResponse> createToDoList(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @Valid @RequestBody ListRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.createToDoList(
                            token.getName(),
                            cardId,
                            request
                        )
                );
    }

    @GetMapping("/{cardId}/to-do-list")
    public ResponseEntity<List<ListResponse>> readToDoLists(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readToDoLists(
                            token.getName(),
                            cardId
                        )
                );
    }

    @GetMapping("/{cardId}/to-do-list/{listId}")
    public ResponseEntity<ListResponse> readToDoListById(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readToDoListById(
                            token.getName(),
                            cardId,
                            listId
                        )
                );
    }

    @PatchMapping("/{cardId}/to-do-list/{listId}")
    public ResponseEntity<ListResponse> updateToDoList(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId,
            @Valid @RequestBody ListRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.updateToDoList(
                               token.getName(),
                               cardId,
                               listId,
                               request
                        )
                );
    }

    @DeleteMapping("/{cardId}/to-do-list/{listId}")
    public ResponseEntity<Void> deleteToDoList(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.deleteToDoList(
                token.getName(),
                cardId,
                listId
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{cardId}/to-do-list/{listId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId,
            @Valid @RequestBody TaskRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    cardService.createTask(
                            token.getName(),
                            cardId,
                            listId,
                            request
                    )
                );
    }

    @GetMapping("/{cardId}/to-do-list/{listId}/tasks")
    public ResponseEntity<List<TaskResponse>> readTaskList(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.readTaskList(
                            token.getName(),
                            cardId,
                            listId
                        )
                );
    }

    @GetMapping("/{cardId}/to-do-list/{listId}/tasks/{taskId}")
    public ResponseEntity<TaskResponse> readTask(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId,
            @PathVariable("taskId") UUID taskId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService
                                .readTask(
                                        token.getName(),
                                        cardId,
                                        listId,
                                        taskId
                                )
                );
    }

    @PatchMapping("/{cardId}/to-do-list/{listId}/tasks/{taskId}/name")
    public ResponseEntity<TaskResponse> updateTaskName(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId,
            @PathVariable("taskId") UUID taskId,
            @Valid @RequestBody TaskRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        cardService.updateTaskName(
                            token.getName(),
                            cardId,
                            listId,
                            taskId,
                            request
                        )
                );
    }

    @PatchMapping("/{cardId}/to-do-list/{listId}/tasks/{taskId}/complete")
    public ResponseEntity<TaskResponse> updateTaskComplete(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId,
            @PathVariable("taskId") UUID taskId,
            @Valid @RequestBody TaskCompleteRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    cardService.updateTaskComplete(
                            token.getName(),
                            cardId,
                            listId,
                            taskId,
                            request
                    )
                );
    }

    @DeleteMapping("/{cardId}/to-do-list/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            JwtAuthenticationToken token,
            @PathVariable("cardId") UUID cardId,
            @PathVariable("listId") UUID listId,
            @PathVariable("taskId") UUID taskId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        cardService.deleteTask(
                token.getName(),
                cardId,
                listId,
                taskId
        );
        return ResponseEntity
                .noContent()
                .build();
    }
}
