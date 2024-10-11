package controller;

import model.Account;
import services.AccountService;

public class AccountController {
	AccountService service = new AccountService();
	
	public Account insert(Account account) {
		return service.insert(account);
	}
}
