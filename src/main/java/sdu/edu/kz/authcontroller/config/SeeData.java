package sdu.edu.kz.authcontroller.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sdu.edu.kz.authcontroller.entity.Account;
import sdu.edu.kz.authcontroller.services.AccountService;
import sdu.edu.kz.authcontroller.util.constants.Authority;

@Component
public class SeeData implements CommandLineRunner {
    private final AccountService accountService;

    public SeeData(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        Account account = new Account();
        Account account1 = new Account();

        account.setEmail("user@user.com");
        account.setPassword("user12");
        account.setAuthorities(Authority.USER.toString());
        accountService.save(account);

        account1.setEmail("admin@admin.com");
        account1.setPassword("admin12");
        account1.setAuthorities(Authority.ADMIN.toString() + " " + Authority.USER.toString());
        accountService.save(account1);
    }
}
