package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Transaction;

public class TransactionDAO {
	private static TransactionDAO instance;
	protected EntityManager entityManager;
	
	private TransactionDAO() {
		entityManager = getEntityManager();
	}
	
	public static TransactionDAO getInstance() { // Just to don´t need instantiate with 'new ...' 
		if (instance == null) {
			instance = new TransactionDAO();
		}
		
		return instance;
	}
	
	private EntityManager getEntityManager() { // Just to don´t need create an entityManager all time I need use it
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("nomePU");
		
		if (this.entityManager == null) {
			this.entityManager = factory.createEntityManager();
		}
		
		return this.entityManager;
	}
	
	public Transaction insert(Transaction transaction) {
		try {
			// Create a new transaction on database
			this.entityManager.getTransaction().begin();
			this.entityManager.persist(transaction);
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return transaction;
	}
	
	public Transaction update(Transaction transaction) {
		Transaction dbTransaction = null;
		
		try {			
			if (transaction.getId() != null) {
				this.entityManager.getTransaction().begin();
				
				dbTransaction = this.findById(transaction.getId());
				
				if (dbTransaction != null) {
					dbTransaction.setDescription(transaction.getDescription());
					this.entityManager.merge(dbTransaction);
				}
				
				this.entityManager.getTransaction().commit();
			}
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return dbTransaction;
	}
	
	public void delete(Long id) {
		try {
			this.entityManager.getTransaction().begin();
			
			Transaction dbTransaction = this.entityManager.find(Transaction.class, id);
			
			if (dbTransaction != null) {
				this.entityManager.remove(dbTransaction);
			}
			
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> list() {
		List<Transaction> transactions = null;
		
		try {
			this.entityManager.getTransaction().begin();
			transactions = this.entityManager.createQuery("FROM transaction").getResultList();
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return transactions;
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> listCpf(String cpf) {
		List<Transaction> transactions = null;
		
		try {
			this.entityManager.getTransaction().begin();
			transactions = this.entityManager.createQuery("FROM transaction WHERE account_holder_cpf = '" + cpf + "'").getResultList();
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return transactions;
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> listTransaction(String trans_type) {
		List<Transaction> transactions = null;
		
		try {
			this.entityManager.getTransaction().begin();
			transactions = this.entityManager.createQuery("FROM transaction WHERE transaction_type = '" + trans_type + "'").getResultList();
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return transactions;
	}
	
	private Transaction findById(Long id) {
		Transaction transaction = this.entityManager.find(Transaction.class, id);
		this.entityManager.close();
		
		return transaction;
	}
}
