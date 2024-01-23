package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.LoginRequest;
import com.application.WorkManagement.dto.requests.ProfileRequest;
import com.application.WorkManagement.dto.requests.RegisterRequest;
import com.application.WorkManagement.dto.responses.AccountResponse;
import com.application.WorkManagement.dto.responses.TokenResponse;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AccountService {

    AccountResponse createAccount(RegisterRequest request) throws CustomDuplicateException;

    TokenResponse createToken(LoginRequest loginRequest);

    AccountResponse readAccount(String uuid) throws DataNotFoundException;

    AccountResponse updateProfileAccount(String uuid, ProfileRequest profile) throws DataNotFoundException;

    AccountResponse updateAvatarAccount(String uuid, MultipartFile file) throws URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException, DataNotFoundException;

}
