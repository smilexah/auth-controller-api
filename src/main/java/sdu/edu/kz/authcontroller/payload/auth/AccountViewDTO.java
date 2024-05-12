package sdu.edu.kz.authcontroller.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountViewDTO {
    private Long accountID;
    private String email;
    private String authorities;
}
