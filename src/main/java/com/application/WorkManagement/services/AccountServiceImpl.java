package com.application.WorkManagement.services;

import com.application.WorkManagement.dto.mappers.AccountMapper;
import com.application.WorkManagement.dto.requests.RegisterRequest;
import com.application.WorkManagement.dto.responses.AccountResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.enums.UserRole;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.repositories.AccountRepository;
import com.application.WorkManagement.services.Interface.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    private AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            DaoAuthenticationProvider daoAuthenticationProvider,
            AccountMapper accountMapper
    ) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountResponse createAccount(RegisterRequest request) throws CustomDuplicateException {
        if (
            accountRepository.existsAccountByEmail(request.getEmail())
        ){
            throw new CustomDuplicateException("Email đã được đăng ký");
        }
        Account account = Account
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                    passwordEncoder.encode(request.getPassword())
                )
                .notification(LocalDateTime.now())
                .userRole(UserRole.USER)
                .build();
        return accountMapper.apply(
                accountRepository.save(account)
        );
    }
}
