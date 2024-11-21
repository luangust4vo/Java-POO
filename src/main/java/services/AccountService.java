package services;

import java.util.List;

import dao.AccountDAO;
import model.Account;
import utils.AccountUtils;
import utils.ValidationUtils;

public class AccountService {
    private final AccountDAO dao;

    public AccountService() {
		dao = AccountDAO.getInstance();
	}

    public Account store(Account account) {
        return ValidationUtils.execute(() -> {
            if (AccountUtils.isMaximumAccountNumberReached(account.getAccountHolder().getCpf())) {
                throw new Exception("Número máximo de contas atingido!");
            }

            Account account2 = dao.store(account);
            return account2;
        });
    }

    public List<Account> getAccountsByCpf(String cpf) {
        return dao.getAccountsByCpf(cpf);
    }
}
