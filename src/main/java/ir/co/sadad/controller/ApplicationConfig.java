package ir.co.sadad.controller;

import static ir.co.sadad.security.AuthoritiesConstants.ADMIN;
import static ir.co.sadad.security.AuthoritiesConstants.USER;
import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;


@LoginConfig(
        authMethod = "MP-JWT",
        realmName = "MP-JWT"
)
@DeclareRoles({ADMIN, USER})
@ApplicationPath("resources")
@OpenAPIDefinition(
        info = @Info(
                title="USER SERVICE API",
                version = "1.0"
        ),
                security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        securitySchemeName = "bearerAuth",
        description = "authentication needed to access User service app",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class ApplicationConfig extends Application {
}
