package com.application.WorkManagement.controllers;

import com.application.WorkManagement.dto.requests.account.EmailRequest;
import com.application.WorkManagement.dto.requests.account.PasswordRequest;
import com.application.WorkManagement.dto.requests.account.ProfileRequest;
import com.application.WorkManagement.dto.responses.account.AccountResponse;
import com.application.WorkManagement.dto.responses.account.EmailCheckResponse;
import com.application.WorkManagement.exceptions.custom.*;
import com.application.WorkManagement.services.Interface.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/account")
@SecurityRequirement(
        name = "JWT-BEARER"
)
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<AccountResponse> readAccount(
            JwtAuthenticationToken authentication
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    accountService.readAccount(authentication.getName())
                );
    }

    @PatchMapping
    public ResponseEntity<AccountResponse> updateProfileAccount(
            JwtAuthenticationToken authentication,
            @Valid @RequestBody
            ProfileRequest profileRequest
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    accountService.updateProfileAccount(
                            authentication.getName(),
                            profileRequest
                    )
                );
    }

    @PatchMapping("/avatar")
    public ResponseEntity<AccountResponse> updateAvatarAccount(
            JwtAuthenticationToken authentication,
            @RequestParam("image") MultipartFile multipartFile
    ) throws InvalidFileExtensionException, URISyntaxException, IOException, EmptyImageException, DataNotFoundException
    {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    accountService.updateAvatarAccount(
                                authentication.getName(),
                                multipartFile
                    )
                );
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<AccountResponse> deleteAvatarAccount(
            JwtAuthenticationToken authentication
    ) throws DataNotFoundException, URISyntaxException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        accountService.deleteAvatarAccount(
                                authentication.getName()
                        )
                );
    }

    @PatchMapping("/email")
    public ResponseEntity<AccountResponse> updateEmailAccount(
            JwtAuthenticationToken authentication,
            @Valid @RequestBody
            EmailRequest emailRequest
    ) throws DataNotFoundException, CustomDuplicateException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        accountService.updateEmailAccount(
                                authentication.getName(),
                                emailRequest
                        )
                );
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePasswordAccount(
            JwtAuthenticationToken authentication,
            @Valid @RequestBody
            PasswordRequest passwordRequest
    ) throws DataNotFoundException, PasswordException {
        accountService.updatePasswordAccount(
                authentication.getName(),
                passwordRequest
        );
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/notification")
    public ResponseEntity<AccountResponse> updateNotificationAccount(
            JwtAuthenticationToken authentication
    ) throws DataNotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    accountService.updateNotificationAccount(authentication.getName())
                );
    }
}
