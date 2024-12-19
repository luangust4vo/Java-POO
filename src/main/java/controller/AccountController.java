package controller;

import java.util.List;

import model.Account;
import services.AccountService;

public class AccountController {
    private AccountService service = new AccountService();

    public Account store(Account account) {
       return service.store(account);
    }

    public List<Account> getAccountsByCpf(String cpf) {
        return service.getAccountsByCpf(cpf);
    }
    
    public boolean isMaximumAccountNumberReached(String cpf) {
    	return service.isMaximumAccountNumberReached(cpf);
    }
}