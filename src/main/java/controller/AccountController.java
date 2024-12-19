package controller;

import java.util.List;

import model.Account;
import services.AccountService;
import services.BasicService;

public class AccountController implements BasicController<Account> {
    private AccountService service = new AccountService();

    public List<Account> getAccountsByCpf(String cpf) {
        return service.getAccountsByCpf(cpf);
    }
    
    public boolean isMaximumAccountNumberReached(String cpf) {
    	return service.isMaximumAccountNumberReached(cpf);
    }

    @Override
    public BasicService<Account> getService() {
        return service;
    }
}