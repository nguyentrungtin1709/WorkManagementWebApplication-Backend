package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.requests.card.BasicUpdateRequest;
import com.application.WorkManagement.dto.requests.card.PositionUpdateRequest;
import com.application.WorkManagement.dto.responses.card.CardMemberResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.InvalidPositionException;
import com.application.WorkManagement.services.Interface.CardService;
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

}
