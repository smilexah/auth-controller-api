package sdu.edu.kz.authcontroller.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.authcontroller.entity.Account;
import sdu.edu.kz.authcontroller.payload.auth.*;
import sdu.edu.kz.authcontroller.services.AccountService;
import sdu.edu.kz.authcontroller.services.TokenService;
import sdu.edu.kz.authcontroller.util.constants.AccountError;
import sdu.edu.kz.authcontroller.util.constants.AccountSuccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Controller for Account management")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AccountService accountService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email or password length between from 6 to 20 characters")
    @ApiResponse(responseCode = "200", description = "Token generated")
    @Operation(summary = "Generate a new Token")
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(),
                            userLoginDTO.getPassword()));

            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATOR_ERROR.toString() + ": " + e.getMessage());
            return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/users/add", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and password length between from 6 to 20 characters")
    @ApiResponse(responseCode = "200", description = "Account added")
    @Operation(summary = "Add a new User")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();

            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());

            accountService.save(account);

            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString());
        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/users", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List of users API")
    @SecurityRequirement(name = "sduedu-demo-api")
    public List<AccountViewDTO> getUsers() {
        List<AccountViewDTO> accounts = new ArrayList<>();

        for (Account account : accountService.findAll()) {
            accounts.add(new AccountViewDTO(account.getAccountID(), account.getEmail(), account.getAuthorities()));
        }

        return accounts;
    }

    @PutMapping(value = "/users/{userId}/update-authorities", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Authority updated")
    @ApiResponse(responseCode = "400", description = "Invalid user id")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update authorities")
    @SecurityRequirement(name = "sduedu-demo-api")
    public ResponseEntity<AccountViewDTO> updateAuthorities(@Valid @RequestBody AuthoritiesDTO authoritiesDTO, @PathVariable Long userId) {
        Optional<Account> optionalAccount = accountService.findById(userId);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.save(account);

            return ResponseEntity.ok(new AccountViewDTO(account.getAccountID(), account.getEmail(), account.getAuthorities()));
        }

        return new ResponseEntity<>(new AccountViewDTO(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Profile")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "View Profile")
    @SecurityRequirement(name = "sduedu-demo-api")
    public ProfileDTO getProfile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        Account account = optionalAccount.get();

        return new ProfileDTO(account.getAccountID(), account.getEmail(), account.getAuthorities());
    }

    @PutMapping(value = "/profile/update-password", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Password updated")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update Password")
    @SecurityRequirement(name = "sduedu-demo-api")
    public AccountViewDTO updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        Account account = optionalAccount.get();
        account.setPassword(passwordDTO.getPassword());
        accountService.save(account);

        return new AccountViewDTO(account.getAccountID(), account.getEmail(), account.getAuthorities());
    }
}
