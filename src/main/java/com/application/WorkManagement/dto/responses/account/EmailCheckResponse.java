package com.application.WorkManagement.dto.responses.account;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckResponse {

    private Boolean exist;

    private String email;

    private HttpStatus status;

}
