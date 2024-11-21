package dao;

import java.util.Date;
import java.util.List;

import model.Transaction;
import model.types.TransactionType;

public class TransactionDAO extends GenericDAO<Transaction> {
	public TransactionDAO() {
		super(Transaction.class);
	}

	public static TransactionDAO getInstance() {
		return new TransactionDAO();
	}

	public Double getBalance(String cpf) {
		return execute(em -> em.createQuery(
				"SELECT SUM(CASE WHEN t.type = 'DEPOSIT' THEN t.amount ELSE -t.amount END)" +
						"FROM transaction t" +
						"JOIN account a ON t.account_id = a.id" +
						"JOIN account_holder ah ON a.account_holder_id = ah.id" +
						"WHERE ah.cpf = :cpf",
				Double.class).setParameter("cpf", cpf).getSingleResult());
	}

	public List<Transaction> getTransactionsByCpfAndType(String cpf, TransactionType type) {
		return execute(em -> em.createQuery(
				"SELECT t" +
						"FROM transaction t" +
						"JOIN account a ON t.account_id = a.id" +
						"JOIN account_holder ah ON a.account_holder_id = ah.id" +
						"WHERE ah.cpf = :cpf AND t.type = :type",
				Transaction.class).setParameter("cpf", cpf).setParameter("type", type).getResultList());
	}

	public List<Transaction> getTransactionsByCpfAndPeriod(String cpf, Date startDate, Date endDate) {
		return execute(em -> em.createQuery(
				"SELECT t" +
						"FROM transaction t" +
						"JOIN account a ON t.account_id = a.id" +
						"JOIN account_holder ah ON a.account_holder_id = ah.id" +
						"WHERE ah.cpf = :cpf AND t.date BETWEEN :startDate AND :endDate",
				Transaction.class).setParameter("cpf", cpf).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList());
	}

	public Double getAverageTransactionValueByCpfAndPeriod(String cpf, Date startDate, Date endDate) {
		return execute(em -> em.createQuery(
				"SELECT AVG(t.value)" +
						"FROM transaction t" +
						"JOIN account a ON t.account_id = a.id" +
						"JOIN account_holder ah ON a.account_holder_id = ah.id" +
						"WHERE ah.cpf = :cpf AND t.date BETWEEN :startDate AND :endDate" +
						"GROUP BY ah.cpf",
				Double.class).setParameter("cpf", cpf).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getSingleResult());
	}

	public List<Transaction> getTransactionsByCpfAndDate(String cpf, Date date) {
		return execute(em -> em.createQuery(
				"SELECT t" +
						"FROM transaction t" +
						"JOIN account a ON t.account_id = a.id" +
						"JOIN account_holder ah ON a.account_holder_id = ah.id" +
						"WHERE ah.cpf = :cpf AND t.date = :date",
				Transaction.class).setParameter("cpf", cpf).setParameter("date", date).getResultList());
	}
}