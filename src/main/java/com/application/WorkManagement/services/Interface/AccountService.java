package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.LoginRequest;
import com.application.WorkManagement.dto.requests.ProfileRequest;
import com.application.WorkManagement.dto.requests.RegisterRequest;
import com.application.WorkManagement.dto.responses.AccountResponse;
import com.application.WorkManagement.dto.responses.TokenResponse;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;

public interface AccountService {

    AccountResponse createAccount(RegisterRequest request) throws CustomDuplicateException;

    TokenResponse createToken(LoginRequest loginRequest);

    AccountResponse readAccount(String email);

    AccountResponse updateProfileAccount(String email, ProfileRequest profile);

}
