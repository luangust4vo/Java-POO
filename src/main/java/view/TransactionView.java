package view;

import java.util.Date;

import controller.TransactionController;
import model.Transaction;

public class TransactionView {

	public static void main(String[] args) {
		TransactionController controller = new TransactionController();
		Transaction account = new Transaction();
		account.setAccountHolderCpf("99999999999");
		account.setTransactionDate(new Date());
		account.setDescription("Teste");
		account.setTransactionType("depósito");
		account.setValue(500.);
		
		controller.insert(account);
	}

}
