package utils;

import java.util.ArrayList;
import java.util.List;

import model.Transaction;

public class TransactionUtils {
	
	public static boolean validateCpf(String cpf) {	
		cpf = cpf.replace(".", "").replace("-", "");
		
		if (cpf.length() != 11) return false;
		
		int first = cpf.charAt(0);
		for (int i = 0; i < cpf.length(); i++) {
			if (cpf.charAt(i) != first) break;
			else if (i == cpf.length() - 1) return false;
		}

		int digit;
		
		ArrayList<Integer> cpfNumbers = new ArrayList<Integer>();
		
		for (int i = 0; i < 9; i++) {
			cpfNumbers.add(Character.getNumericValue(cpf.charAt(i)));
		}
		
		digit = calculateVerificationDigit(cpfNumbers, 10);
		
		if (digit != Character.getNumericValue(cpf.charAt(9))) return false;
		
		cpfNumbers.add(digit);
		digit = calculateVerificationDigit(cpfNumbers, 11);
		
		if (digit != Character.getNumericValue(cpf.charAt(10))) return false;
		
		return true;
	}
	
	private static int calculateVerificationDigit(ArrayList<Integer> cpfNumbers, int weight) {
		int digit = 0;
		for(int j = 0; j < cpfNumbers.size(); j++) {
			digit += cpfNumbers.get(j) * weight--;
		}

		digit = digit % 11;
		digit = digit < 2 ? 0 : (11 - digit);
		
		return digit;
	}
	
	private static double calcBalance(List<Transaction> transactions) {
		double balance = 0;
		
		for (Transaction transaction : transactions) {
			if (transaction.getTransactionType().equals("deposit")) {
				balance += transaction.getValue();
			} else {
				balance -= transaction.getValue();
			}
		}
		
		return balance;
	}
	
	private static boolean validBalance(Transaction transaction, List<Transaction> transactions) {
		double balance = calcBalance(transactions);
		
		if (transaction.getTransactionType().equals("deposit") || balance - transaction.getValue() < 0) return false;
		
		return true;
	}
}
