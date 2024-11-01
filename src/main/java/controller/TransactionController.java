package controller;

import model.Transaction;
import services.TransactionService;

public class TransactionController {
	TransactionService service = new TransactionService();
	
	public Transaction insert(Transaction transaction) {
		return service.insert(transaction);
	}
}
