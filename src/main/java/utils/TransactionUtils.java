package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import model.Transaction;
import model.enums.TransactionType;
import services.TransactionService;

public class TransactionUtils {
	private static final TransactionService service = new TransactionService();

	public static boolean validateTransaction(Transaction transaction) {
		if (transaction.getTransactionType().equals(TransactionType.WITHDRAW) && isWithdrawLimitExceeded(transaction))
			return false;
			
		if (!isValidBalance(transaction, addTransactionTax(transaction))) {
			JOptionPane.showMessageDialog(null, "Saldo insuficiente para realizar a operação", "Atenção!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!isValidPixTransaction(transaction))
			return false;

		return true;
	}

	private static double calcBalance(List<Transaction> transactions) {
		double balance = 0;

		for (Transaction transaction : transactions) {
			if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
				balance += transaction.getValue();
			} else {
				balance -= transaction.getValue();
			}
		}

		return balance;
	}

	private static boolean isValidBalance(Transaction transaction, Double adjustedValue) {
		if (transaction.getTransactionType().equals(TransactionType.DEPOSIT))
			return true;

		double balance = calcBalance(service.listCpf(transaction.getAccount().getAccountHolder().getCpf()));
		double remainingBalance = balance - adjustedValue;

		if (remainingBalance < 0)
			return false;
		else if (remainingBalance < 100) {
			JOptionPane.showMessageDialog(null, "Saldo abaixo de R$ 100,00", "Atenção!", JOptionPane.WARNING_MESSAGE);
		}

		return true;
	}

	private static Double addTransactionTax(Transaction transaction) {
		Double adjustedValue = transaction.getValue();

		if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {
			adjustedValue += 2;
		} else if (transaction.getTransactionType().equals(TransactionType.PAYMENT)
				|| transaction.getTransactionType().equals(TransactionType.PIX)) {
			adjustedValue += 5;
		}

		return adjustedValue;
	}

	private static boolean isValidPixTransaction(Transaction transaction) {
		if (!transaction.getTransactionType().equals(TransactionType.PIX)) {
			return true;
		}

		if (transaction.getValue() > 300) {
			JOptionPane.showMessageDialog(null, "Valor máximo para transações PIX é de R$ 300,00", "Atenção!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		LocalTime now = LocalTime.now();
		if (now.isBefore(LocalTime.of(6, 0)) || now.isAfter(LocalTime.of(22, 0))) {
			JOptionPane.showMessageDialog(null, "Transações PIX só podem ser realizadas entre 6h e 22h", "Atenção!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private static boolean isWithdrawLimitExceeded(Transaction transaction) {
		List<Transaction> transactions = service.listCpf(transaction.getAccount().getAccountHolder().getCpf());

		Double totalWithdraw = 0.0;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(new Date());
		for (Transaction trans : transactions) {
			try {
				String transactionDate = formatter.format(trans.getTransactionDate());
				if (trans.getTransactionType().equals(TransactionType.WITHDRAW) && transactionDate.equals(today)) {
					totalWithdraw += trans.getValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ((totalWithdraw + transaction.getValue()) > 5000) {
			JOptionPane.showMessageDialog(null, "Limite diário de saque excedido", "Atenção!",
					JOptionPane.ERROR_MESSAGE);
			return true;
		}

		return false;
	}
}