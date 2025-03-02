package springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.user.UserLoginRequestDto;
import springboot.dto.user.UserLoginResponseDto;
import springboot.dto.user.UserRegistrationRequestDto;
import springboot.dto.user.UserResponseDto;
import springboot.exeptions.RegistrationException;
import springboot.security.AuthenticationService;
import springboot.service.UserService;

@Tag(name = "Authentication", description = "Controller for user registration and login")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user in the system",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User successfully registered"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid registration data"),
                    @ApiResponse(responseCode = "409",
                            description = "User with this email already exists")
            }
    )
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user with email and password",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successful login"),
                    @ApiResponse(responseCode = "401",
                            description = "Invalid email or password"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid login data")
            }
    )
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}

