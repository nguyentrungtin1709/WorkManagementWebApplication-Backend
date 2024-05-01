package com.application.WorkManagement.controllers.user;

import com.application.WorkManagement.dto.responses.table.TableActivityResponse;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.services.Interface.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@SecurityRequirement(
        name = "JWT-BEARER"
)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<TableActivityResponse>> readNotifications(
            JwtAuthenticationToken token
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        notificationService.readNotifications(
                                token.getName()
                        )
                );
    }
}
