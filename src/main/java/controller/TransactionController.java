package controller;

import java.util.List;

import model.Transaction;
import services.TransactionService;

public class TransactionController {
	private TransactionService service = new TransactionService();
	
	public Transaction store(Transaction transaction) {
		return service.store(transaction.getAccount().getAccountHolder().getCpf(), transaction);
	}

	public Double getBalance(Long id) {
		return service.getBalance(id);
	}

	public List<Transaction> getMonthlyStatement(Long id, int month, int year) {
		return service.getMonthlyStatement(id, month, year);
	}

	public List<Transaction> getPeriodicStatement(Long id, String startDateStr, String endDateStr) {
		return service.getPeriodicStatement(id, startDateStr, endDateStr);
	}
}
