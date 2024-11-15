package clubbook.backend.controller;

import clubbook.backend.dtos.LoginUserDto;
import clubbook.backend.dtos.RegisterUserDto;
import clubbook.backend.model.User;
import clubbook.backend.responses.LoginResponse;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.AuthenticationService;
import clubbook.backend.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related operations,
 * including user registration, login, and logout.
 */
@Validated
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Constructs an AuthenticationController with the specified JWT and authentication services.
     *
     * @param jwtService the service for handling JWT operations.
     * @param authenticationService the service for handling authentication logic.
     */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Registers a new user. Requires the user to have the role of 'ADMINISTRATOR'.
     *
     * @param registerUserDto the DTO containing the user registration data.
     * @return a ResponseEntity containing a response wrapper with the result of the registration operation.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @PostMapping("/signup")
    public ResponseEntity<ResponseWrapper<User>> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        System.out.println(registerUserDto.toString());
        User registeredUser = authenticationService.signup(registerUserDto);
        if (registeredUser == null)
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.INCORRECT_REGISTER, null));
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.CORRECT_REGISTER, registeredUser));
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginUserDto the DTO containing the user login data.
     * @return a ResponseEntity containing a response wrapper with the login response data, including the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<LoginResponse>> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setUser(authenticatedUser);

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.CORRECT_LOG_IN, loginResponse));
    }

    /**
     * Logs out a user by invalidating the provided JWT token.
     *
     * @param token the JWT token from the request header.
     * @return a ResponseEntity with no content (HTTP 200).
     */
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        jwtService.invalidateToken(jwtToken);
        return ResponseEntity.ok().build();
    }

    /**
     * Handles validation exceptions for request bodies.
     *
     * @param ex the validation exception that was thrown.
     * @return a ResponseEntity containing a message about the validation failure.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getBindingResult().toString());
    }
}
