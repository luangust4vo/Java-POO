package controller;

import model.Transaction;
import services.TransactionService;

public class TransactionController {
	private TransactionService service = new TransactionService();
	
	public Transaction store(Transaction transaction) {
		return service.store(transaction.getAccount().getAccountHolder().getCpf(), transaction);
	}
}
