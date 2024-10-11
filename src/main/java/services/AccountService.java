package services;

import java.util.Date;

import dao.AccountDAO;
import model.Account;

public class AccountService {
	
	public Account insert(Account account) {
		account.setDescription("Operação de " + account.getTransactionType());
		account.setTransactionDate(new Date());
		return AccountDAO.getInstance().insert(account);
	}
}
