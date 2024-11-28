package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.AccountHolderDAO;
import dao.TransactionDAO;
import model.Transaction;
import model.types.TransactionType;
import utils.TransactionUtils;
import utils.ValidationUtils;

public class TransactionService {
	private final TransactionDAO dao;

	public TransactionService() {
		dao = TransactionDAO.getInstance();
	}

	public Transaction store(String cpf, Transaction transaction) {
		return ValidationUtils.execute(() -> {
			if (!validateCpf(cpf)) {
				throw new Exception("Não existe nenhum cliente com o CPF informado.");
			}

			if (TransactionUtils.isTransactionDiaryLimitReached(transaction)) {
				throw new Exception("Operação bloqueada! Limite diário de 10 transações atingido.");
			}

			Double adjustedValue = TransactionUtils.addTransactionFee(transaction);
			transaction.setValue(adjustedValue);

			if (!transaction.getType().equals(TransactionType.DEPOSIT) && !isSufficientBalance(transaction.getAccount().getId(), transaction.getValue())) {
				throw new Exception("Operação bloqueada! Seu saldo é insuficiente para realizar a transação");
			}

			if (TransactionUtils.isValidPixSchedule(transaction)) {
				if (!TransactionUtils.isValidPixValue(transaction)) {
					throw new Exception("O valor máximo para transações PIX é de R$ 300,00.");
				}
			} else {
				throw new Exception("Operação bloqueada! Transações PIX só podem ser realizadas entre 6h e 22h.");
			}

			if (!TransactionUtils.isValidWithdrawTransaction(transaction)) {
				throw new Exception(
						"Operação bloqueada! Saques só podem ser realizados até o valor máximo de R$ 5.000,00.");
			}

			if (TransactionUtils.isSuspiciousTransaction(transaction)) {
				System.out.println("chegou aqui");
				throw new Exception("Operação bloqueada! Transação suspeita detectada.");
			}

			Transaction transaction2 = dao.store(transaction);

			Double balance = dao.getBalance(transaction.getAccount().getId());
			if (TransactionUtils.isLowBalance(balance)) {
				throw new Exception(
						"Atenção! Seu saldo atingiu um valor abaixo de R$ 100,00. Seu saldo agora é de R$ " + balance);
			}

			return transaction2;
		});
	}

	private boolean validateCpf(String cpf) {
		return new AccountHolderDAO().findOne(cpf) != null;
	}

	private boolean isSufficientBalance(Long id, Double value) {
		Double currentBalance = dao.getBalance(id);

		return currentBalance != null && (currentBalance - value) >= 0;
	}

	public List<Transaction> getTransactionsByType(Long id, TransactionType type) {
		return dao.getTransactionsByType(id, type);
	}

	public List<Transaction> getTransactionsByPeriod(Long id, Date startDate, Date endDate) {
		return dao.getTransactionsByPeriod(id, startDate, endDate);
	}

	public Double getAverageTransactionValueByPeriod(Long id, Date startDate, Date endDate) {
		return dao.getAverageTransactionValueByPeriod(id, startDate, endDate);
	}

	public List<Transaction> getTransactionsByDate(Long id, Date date) {
		return dao.getTransactionsByDate(id, date);
	}

	public List<Transaction> getMonthlyStatement(Long id, int month, int year) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Mês inválido.");
		} else if (year < 1900 && year > Calendar.getInstance().get(Calendar.YEAR)) {
			throw new IllegalArgumentException("Ano inválido.");
		}

		Date startDate = TransactionUtils.getStartOfMonth(month, year);
		Date endDate = TransactionUtils.getEndOfMonth(month, year);

		return dao.getTransactionsByPeriod(id, startDate, endDate);
	}

	public List<Transaction> getPeriodicStatement(Long id, String startDateStr, String endDateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setLenient(false);

		try {
			Date startDate = formatter.parse(startDateStr);
			Date endDate = formatter.parse(endDateStr);

			if (startDate.after(endDate)) {
				throw new Exception();
			}

			return dao.getTransactionsByPeriod(id, startDate, endDate);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Double getBalance(Long id) {
		Double balance = dao.getBalance(id);
		return balance != null ? balance : 0.0;
	}
}
