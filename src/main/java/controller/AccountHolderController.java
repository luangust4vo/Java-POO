package controller;

import model.AccountHolder;
import services.AccountHolderService;
import services.BasicService;

public class AccountHolderController implements BasicController<AccountHolder> {
    private AccountHolderService service = new AccountHolderService();

    public AccountHolder findByCpf(String cpf) {
        return service.findByCpf(cpf);
    }

    @Override
    public BasicService<AccountHolder> getService() {
        return service;
    }
}
