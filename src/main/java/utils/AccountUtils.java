package utils;

import java.util.List;

import model.Account;
import services.AccountService;

public class AccountUtils {
    private static final AccountService service = new AccountService();

    public static boolean isMaximumAccountNumberReached(String cpf) {
		List<Account> accounts = service.getAccountsByCpf(cpf);

        return accounts.size() >= 3;
	}
}
