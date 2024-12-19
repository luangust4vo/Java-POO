package controller;

import java.util.List;

import model.Transaction;
import services.BasicService;
import services.TransactionService;

public class TransactionController implements BasicController<Transaction> {
	private TransactionService service = new TransactionService();

	public Double getBalance(Long id) {
		return service.getBalance(id);
	}

	public List<Transaction> getMonthlyStatement(Long id, int month, int year) {
		return service.getMonthlyStatement(id, month, year);
	}

	public List<Transaction> getPeriodicStatement(Long id, String startDateStr, String endDateStr) {
		return service.getPeriodicStatement(id, startDateStr, endDateStr);
	}

	@Override
	public BasicService<Transaction> getService() {
		return service;
	}
}
