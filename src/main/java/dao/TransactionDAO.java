package dao;

import java.util.Date;
import java.util.List;

import model.Transaction;
import model.types.TransactionType;

public final class TransactionDAO extends GenericDAO<Transaction> {
	public TransactionDAO() {
		super(Transaction.class);
	}

	public static TransactionDAO getInstance() {
		return new TransactionDAO();
	}

	public Double getBalance(Long id) {
		return execute(em -> em.createQuery(
<<<<<<< HEAD
				"SELECT SUM(CASE WHEN t.type = 'DEPOSIT' THEN t.amount ELSE -t.amount END)" +
=======
				"SELECT SUM(CASE WHEN t.type = 'DEPOSIT' THEN t.value ELSE -t.value END)" +
>>>>>>> a774996dcae14786e906a58a9d490d676ee999a2
						"FROM Transaction t WHERE t.account.id = :id",
				Double.class).setParameter("id", id).getSingleResult());
	}

	public List<Transaction> getTransactionsByType(Long id, TransactionType type) {
		return execute(em -> em.createQuery(
				"SELECT t FROM Transaction t WHERE t.account.id = :id AND t.type = :type",
				Transaction.class).setParameter("id", id).setParameter("type", type).getResultList());
	}

	public List<Transaction> getTransactionsByPeriod(Long id, Date startDate, Date endDate) {
		return execute(em -> em.createQuery(
				"SELECT t FROM Transaction t WHERE t.account.id = :id AND t.date BETWEEN :startDate AND :endDate",
				Transaction.class).setParameter("id", id).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList());
	}

	public Double getAverageTransactionValueByPeriod(Long id, Date startDate, Date endDate) {
		return execute(em -> em.createQuery(
				"SELECT AVG(t.value) FROM Transaction t WHERE t.account.id = :id" +
<<<<<<< HEAD
						"AND t.date BETWEEN :startDate AND :endDate GROUP BY t.account.id",
=======
						" AND t.date BETWEEN :startDate AND :endDate GROUP BY t.account.id",
>>>>>>> a774996dcae14786e906a58a9d490d676ee999a2
				Double.class).setParameter("id", id).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getSingleResult());
	}

	public List<Transaction> getTransactionsByDate(Long id, Date date) {
		return execute(em -> em.createQuery(
				"SELECT t FROM Transaction t WHERE t.account.id = :id AND t.date = :date",
				Transaction.class).setParameter("id", id).setParameter("date", date).getResultList());
	}
}