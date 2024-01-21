package com.application.WorkManagement.dto.mappers;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRoleMapper implements Function<String, String> {

    @Override
    public String apply(String role) {
        return String.format("ROLE_%s", role);
    }

}
