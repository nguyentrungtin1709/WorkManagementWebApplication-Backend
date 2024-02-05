package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.account.EmailRequest;
import com.application.WorkManagement.dto.requests.account.PasswordRequest;
import com.application.WorkManagement.dto.requests.authentication.LoginRequest;
import com.application.WorkManagement.dto.requests.account.ProfileRequest;
import com.application.WorkManagement.dto.requests.authentication.RegisterRequest;
import com.application.WorkManagement.dto.responses.account.AccountResponse;
import com.application.WorkManagement.dto.responses.account.EmailCheckResponse;
import com.application.WorkManagement.dto.responses.authentication.TokenResponse;
import com.application.WorkManagement.exceptions.custom.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AccountService {

    AccountResponse createAccount(RegisterRequest request) throws CustomDuplicateException;

    TokenResponse createToken(LoginRequest loginRequest);

    AccountResponse readAccount(String uuid) throws DataNotFoundException;

    AccountResponse updateProfileAccount(String uuid, ProfileRequest profile) throws DataNotFoundException;

    AccountResponse updateAvatarAccount(String uuid, MultipartFile file) throws URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException, DataNotFoundException;

    EmailCheckResponse checkExistEmail(EmailRequest emailRequest);

    AccountResponse updateEmailAccount(String uuid, EmailRequest emailRequest) throws CustomDuplicateException, DataNotFoundException;

    void updatePasswordAccount(String uuid, PasswordRequest request) throws DataNotFoundException, PasswordException;

    AccountResponse updateNotificationAccount(String uuid) throws DataNotFoundException;

    AccountResponse deleteAvatarAccount(String uuid) throws DataNotFoundException, URISyntaxException;

}
