package com.application.WorkManagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Work Management Web Application",
                description = "Ứng dụng quản lý công việc cho phép những nhóm có thể lập kế hoạch thực hiện những công việc, dự án một cách hiệu quả.",
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "JWT-BEARER",
        description = "Xác thực với JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}
