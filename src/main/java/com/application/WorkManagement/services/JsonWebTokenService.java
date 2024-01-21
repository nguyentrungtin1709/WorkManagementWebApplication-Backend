package com.application.WorkManagement.services;

import com.application.WorkManagement.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class JsonWebTokenService {

    private final JwtEncoder jwtEncoder;

    @Autowired
    public JsonWebTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Account account){
        Instant now = Instant.now();
        String scope = account
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("work-management-web-application")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(account.getEmail())
                .claim("scope", scope)
                .build();
        return jwtEncoder
                .encode(
                    JwtEncoderParameters.from(claims)
                )
                .getTokenValue();
    }
}
