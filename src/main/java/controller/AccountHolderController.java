package controller;

import model.AccountHolder;
import services.AccountHolderService;

public class AccountHolderController {
    private AccountHolderService service = new AccountHolderService();

    public AccountHolder store(AccountHolder accountHolder) {
        return service.store(accountHolder);
    }
}
