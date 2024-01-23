package com.application.WorkManagement.controllers;

import com.application.WorkManagement.dto.requests.account.EmailRequest;
import com.application.WorkManagement.dto.requests.authentication.LoginRequest;
import com.application.WorkManagement.dto.requests.authentication.RegisterRequest;
import com.application.WorkManagement.dto.responses.account.AccountResponse;
import com.application.WorkManagement.dto.responses.account.EmailCheckResponse;
import com.application.WorkManagement.dto.responses.authentication.TokenResponse;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.services.Interface.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AccountService accountService;

    @Autowired
    public AuthenticationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody
            RegisterRequest registerRequest
    ) throws CustomDuplicateException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    accountService.createAccount(registerRequest)
                );
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody
            LoginRequest loginRequest
    ){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        accountService.createToken(loginRequest)
                );
    }

    @GetMapping("/email")
    public ResponseEntity<EmailCheckResponse> checkExistEmail(
        @Valid @RequestBody
        EmailRequest emailRequest
    ){
        EmailCheckResponse emailCheckResponse = accountService.checkExistEmail(emailRequest);
        return ResponseEntity
                .status(emailCheckResponse.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    emailCheckResponse
                );
    }

}
