package services;

import java.util.Date;

import javax.swing.JOptionPane;

import dao.TransactionDAO;
import model.Transaction;
import utils.TransactionUtils;

public class TransactionService {
	
	public Transaction insert(Transaction transaction) {
		if (!TransactionUtils.validateCpf(transaction.getAccountHolderCpf())) {
			JOptionPane.showMessageDialog(null, "O CPF informado é inválido", "Atenção!", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		transaction.setDescription("Operação de " + transaction.getTransactionType());
		transaction.setTransactionDate(new Date());
		return TransactionDAO.getInstance().insert(transaction);
	}
}
