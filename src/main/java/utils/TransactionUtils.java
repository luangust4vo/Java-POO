package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Transaction;
import model.types.TransactionType;
import services.TransactionService;

public class TransactionUtils {
	private static final TransactionService service = new TransactionService();

	public static Double addTransactionFee(Transaction transaction) {
		Double adjustedValue = transaction.getValue();

		switch (transaction.getTransactionType()) {
			case WITHDRAW:
				adjustedValue += 2;
				break;
			case PAYMENT:
			case PIX:
				adjustedValue += 5;
				break;
			default:
				break;
		}

		return adjustedValue;
	}

	public static boolean isValidPixValue(Transaction transaction) {
		return !transaction.getTransactionType().equals(TransactionType.PIX) || transaction.getValue() <= 300;
	}

	public static boolean isValidPixSchedule(Transaction transaction) {
		if (!transaction.getTransactionType().equals(TransactionType.PIX)) {
			return true;
		}

		LocalTime now = LocalTime.now();
		if (now.isBefore(LocalTime.of(6, 0)) || now.isAfter(LocalTime.of(22, 0))) {
			return false;
		}

		return true;
	}

	public static boolean isValidWithdrawTransaction(Transaction transaction) {
		if (!transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {
			return true;
		}

		List<Transaction> transactions = service.getTransactionsByCpfAndType(
				transaction.getAccount().getAccountHolder().getCpf(),
				TransactionType.WITHDRAW);

		Date today = new Date();
		Double totalWithdraw = transactions.stream()
				.filter(t -> isSameDay(t.getTransactionDate(), today))
				.mapToDouble(Transaction::getValue)
				.sum();

		return !((totalWithdraw + transaction.getValue()) > 5000);
	}

	public static boolean isLowBalance(Double balance) {
		return balance < 100;
	}

	private static boolean isSameDay(Date date1, Date date2) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date1).equals(formatter.format(date2));
	}

	public static boolean isSuspiciousTransaction(Transaction transaction) {
		Double average = getAverageTransactionValue(transaction.getAccount().getAccountHolder().getCpf(), 6);

		return average != null && transaction.getValue() > (average * 2);
	}

	private static Double getAverageTransactionValue(String cpf, int months) {
		Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -months);

		Date startDate = calendar.getTime();

		return service.getAverageTransactionValueByCpfAndPeriod(cpf, startDate, today);
	}

	public static boolean isTransactionDiaryLimitReached(Transaction transaction) {
		Date today = new Date();

		List<Transaction> transactions = service.getTransactionsByCpfAndDate(
				transaction.getAccount().getAccountHolder().getCpf(), today);

		return transactions.size() >= 10;
	}
}