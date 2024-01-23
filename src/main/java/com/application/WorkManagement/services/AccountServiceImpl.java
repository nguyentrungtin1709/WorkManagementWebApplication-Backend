package com.application.WorkManagement.services;

import com.application.WorkManagement.dto.mappers.AccountMapper;
import com.application.WorkManagement.dto.requests.LoginRequest;
import com.application.WorkManagement.dto.requests.ProfileRequest;
import com.application.WorkManagement.dto.requests.RegisterRequest;
import com.application.WorkManagement.dto.responses.AccountResponse;
import com.application.WorkManagement.dto.responses.TokenResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Avatar;
import com.application.WorkManagement.enums.UserRole;
import com.application.WorkManagement.exceptions.custom.CustomDuplicateException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.EmptyImageException;
import com.application.WorkManagement.exceptions.custom.InvalidFileExtensionException;
import com.application.WorkManagement.repositories.AccountRepository;
import com.application.WorkManagement.repositories.AvatarRepository;
import com.application.WorkManagement.services.Interface.AccountService;
import com.application.WorkManagement.services.Interface.UploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    private final JsonWebTokenService jsonWebTokenService;

    private final AccountMapper accountMapper;

    private final UploadImageService uploadImageService;

    private final AvatarRepository avatarRepository;

    @Autowired
    public AccountServiceImpl(
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            DaoAuthenticationProvider daoAuthenticationProvider,
            AccountMapper accountMapper,
            JsonWebTokenService jsonWebTokenService, UploadImageService uploadImageService, AvatarRepository avatarRepository
    ) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.accountMapper = accountMapper;
        this.jsonWebTokenService = jsonWebTokenService;
        this.uploadImageService = uploadImageService;
        this.avatarRepository = avatarRepository;
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

    @Override
    public TokenResponse createToken(LoginRequest loginRequest){
        daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        Account account = accountRepository
                .findAccountByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email không được đăng ký"));
        return new TokenResponse(
                jsonWebTokenService.generateToken(account)
        );
    }

    @Override
    public AccountResponse readAccount(String uuid) throws DataNotFoundException {
        return accountMapper.apply(
                accountRepository
                    .findById(UUID.fromString(uuid))
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"))
        );
    }

    @Override
    public AccountResponse updateProfileAccount(String uuid, ProfileRequest profile) throws DataNotFoundException {
        Account account = accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
        if (!account.getName().equals(profile.getName())){
            account.setName(profile.getName());
        }
        if (checkUpdateOptionAttribute(account.getOrganization(), profile.getOrganization())){
            account.setOrganization(profile.getOrganization());
        }
        if (checkUpdateOptionAttribute(account.getDepartment(), profile.getDepartment())){
            account.setDepartment(profile.getDepartment());
        }
        if (checkUpdateOptionAttribute(account.getTitle(), profile.getTitle())){
            account.setTitle(profile.getTitle());
        }
        return accountMapper.apply(
                accountRepository.save(account)
        );
    }

    @Override
    @Transactional(rollbackFor = {
            IOException.class,
            URISyntaxException.class,
            InvalidFileExtensionException.class,
            EmptyImageException.class
    })
    public AccountResponse updateAvatarAccount(
            String uuid,
            MultipartFile file
    ) throws URISyntaxException, InvalidFileExtensionException, IOException, EmptyImageException, DataNotFoundException {
        Account account = accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
        if (account.getAvatar() != null){
            String path = account.getAvatar().getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            uploadImageService.removeFileFromS3(fileName);
        }
        Avatar avatar = avatarRepository.save(
                Avatar.builder()
                        .originalFileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .build()
        );
        URI avatarURI = uploadImageService.uploadImageToS3(avatar.getUuid(), file);
        account.setAvatar(avatarURI);
        return accountMapper.apply(
                accountRepository.save(account)
        );
    }

    public Boolean checkUpdateOptionAttribute(String origin, String other){
        return Objects.nonNull(other) && !other.equals(origin);
    }
}
