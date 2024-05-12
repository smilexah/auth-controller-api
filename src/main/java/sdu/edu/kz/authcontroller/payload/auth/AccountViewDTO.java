package sdu.edu.kz.authcontroller.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountViewDTO {
    private Long accountID;
    private String email;
    private String authorities;
}
