package services;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import dao.TransactionDAO;
import model.Transaction;
import utils.TransactionUtils;

public class TransactionService {
	
	public Transaction insert(Transaction transaction) {
		// if (!TransactionUtils.validateCpf(transaction.getAccount().getAccountHolder().getCpf())) {
		// 	JOptionPane.showMessageDialog(null, "O CPF informado é inválido", "Atenção!", JOptionPane.ERROR_MESSAGE);
		// 	return null;
		// }
		
		transaction.setDescription("Operação de " + transaction.getTransactionType());
		transaction.setTransactionDate(new Date());
		return TransactionDAO.getInstance().insert(transaction);
	}

	public List<Transaction> listCpf(String cpf) {
		return TransactionDAO.getInstance().listCpf(cpf);
	}
}
