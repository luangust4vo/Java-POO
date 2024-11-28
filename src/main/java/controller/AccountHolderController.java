package controller;

import model.AccountHolder;
import services.AccountHolderService;

public class AccountHolderController {
    private AccountHolderService service = new AccountHolderService();

    public AccountHolder store(AccountHolder accountHolder) {
        return service.store(accountHolder);
    }

    public AccountHolder findByCpf(String cpf) {
        return service.findByCpf(cpf);
    }
}
