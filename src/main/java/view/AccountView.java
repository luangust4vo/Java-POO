package view;

import java.util.Date;

import controller.AccountController;
import model.Account;

public class AccountView {

	public static void main(String[] args) {
		AccountController controller = new AccountController();
		Account account = new Account();
		account.setAccountHolderCpf("99999999999");
		account.setTransactionDate(new Date());
		account.setDescription("Teste");
		account.setTransactionType("depósito");
		account.setValue(500.);
		
		controller.insert(account);
	}

}
