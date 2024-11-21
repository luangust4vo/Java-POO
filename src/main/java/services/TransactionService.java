package services;

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

			if (!isSufficientBalance(cpf, transaction.getValue())) {
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
				throw new Exception("Operação bloqueada! Transação suspeita detectada.");
			}

			Transaction transaction2 = dao.store(transaction);

			Double balance = dao.getBalance(cpf);
			if (TransactionUtils.isLowBalance(balance)) {
				throw new Exception(
						"Atenção! Seu saldo atingiu um valor abaixo de R$ 100,00. Seu saldo agora é de R$ " + balance);
			}

			return transaction2;
		});
	}

	private boolean validateCpf(String cpf) {
		return AccountHolderDAO.findByCpf(cpf) != null;
	}

	private boolean isSufficientBalance(String cpf, Double value) {
		Double currentBalance = dao.getBalance(cpf);

		return currentBalance != null && (currentBalance - value) >= 0;
	}

	public List<Transaction> getTransactionsByCpfAndType(String cpf, TransactionType type) {
		return dao.getTransactionsByCpfAndType(cpf, type);
	}

	public List<Transaction> getTransactionsByCpfAndPeriod(String cpf, Date startDate, Date endDate) {
		return dao.getTransactionsByCpfAndPeriod(cpf, startDate, endDate);
	}

	public Double getAverageTransactionValueByCpfAndPeriod(String cpf, Date startDate, Date endDate) {
		return dao.getAverageTransactionValueByCpfAndPeriod(cpf, startDate, endDate);
	}

	public List<Transaction> getTransactionsByCpfAndDate(String cpf, Date date) {
		return dao.getTransactionsByCpfAndDate(cpf, date);
	}
}
