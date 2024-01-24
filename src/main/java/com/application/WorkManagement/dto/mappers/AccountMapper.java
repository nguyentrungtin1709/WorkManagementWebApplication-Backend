package com.application.WorkManagement.dto.mappers;

import com.application.WorkManagement.dto.responses.account.AccountResponse;
import com.application.WorkManagement.entities.Account;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AccountMapper implements Function<Account, AccountResponse> {
    @Override
    public AccountResponse apply(Account account) {
        return AccountResponse
                .builder()
                .id(account.getUuid())
                .name(account.getName())
                .email(account.getEmail())
                .avatar(account.getAvatar())
                .organization(account.getOrganization())
                .department(account.getDepartment())
                .title(account.getTitle())
                .notification(account.getNotification())
                .role(account.getUserRole())
                .build();
    }
}
