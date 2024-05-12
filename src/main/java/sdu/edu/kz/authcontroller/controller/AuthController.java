package sdu.edu.kz.authcontroller.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import sdu.edu.kz.authcontroller.payload.auth.AccountDTO;
import sdu.edu.kz.authcontroller.payload.auth.AccountViewDTO;
import sdu.edu.kz.authcontroller.payload.auth.TokenDTO;
import sdu.edu.kz.authcontroller.payload.auth.UserLoginDTO;
import sdu.edu.kz.authcontroller.services.AccountService;
import sdu.edu.kz.authcontroller.services.TokenService;
import sdu.edu.kz.authcontroller.util.constants.AccountError;
import sdu.edu.kz.authcontroller.util.constants.AccountSuccess;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(),
                            userLoginDTO.getPassword()));

            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATOR_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(value = "/users/add", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and password length between from 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Account added")
    @Operation(summary = "Add a new User")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();

            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            account.setRole("ROLE_USER");

            accountService.save(account);

//            return new ResponseEntity<>(new Gson().toJson(AccountSuccess.ACCOUNT_ADDED.toString()), HttpStatus.OK);
            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString());
        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/users", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @Operation(summary = "List of users API")
    public List<AccountViewDTO> getUsers() {
        List<AccountViewDTO> accounts = new ArrayList<>();

        for (Account account : accountService.findAll()) {
            accounts.add(new AccountViewDTO(account.getAccountID(), account.getEmail(), account.getRole()));
        }

        return accounts;
    }
//    public ResponseEntity<List> getUsers() {
//        try {
//            List<Account> accounts = accountService.findAll();
//            return new ResponseEntity<>(accounts, HttpStatus.OK);
//        } catch (Exception e) {
//            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + ": " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
}
