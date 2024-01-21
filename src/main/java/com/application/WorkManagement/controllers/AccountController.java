package com.application.WorkManagement.controllers;

import com.application.WorkManagement.dto.responses.AccountResponse;
import com.application.WorkManagement.enums.UserRole;
import com.application.WorkManagement.services.Interface.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    private ResponseEntity<AccountResponse> readAccount(JwtAuthenticationToken authentication){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    accountService.readAccount(authentication.getName())
                );
    }
}
