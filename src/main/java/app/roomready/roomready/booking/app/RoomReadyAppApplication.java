package app.roomready.roomready.booking.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				description = "OpenApi documentation for Spring Security",
				title = "OpenApi specification - Room Ready",
				version = "1.0",
				termsOfService = "Terms of service"
		),
		servers = {
				@Server(
						description = "Local ENV",
						url = "http://localhost:8080"
				),
				@Server(
						description = "Server ENV",
						url = "http://108.136.195.14:8080"
				)
		},
		security = {
				@SecurityRequirement(
						name = "bearerAuth"
				)
		}
)
@SecurityScheme(
		name = "bearerAuth",
		description = "JWT auth description",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
public class RoomReadyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomReadyAppApplication.class, args);
	}

}
