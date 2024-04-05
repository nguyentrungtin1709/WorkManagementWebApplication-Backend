package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.requests.card.BasicUpdateRequest;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.services.Interface.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
}
