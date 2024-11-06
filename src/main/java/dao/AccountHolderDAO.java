package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.AccountHolder;
import model.Transaction;

public class AccountHolderDAO {
	private static AccountHolderDAO instance;
	protected EntityManager entityManager;
	
	private AccountHolderDAO() {
		entityManager = getEntityManager();
	}
	
	public static AccountHolderDAO getInstance() {
		if (instance == null) {
			instance = new AccountHolderDAO();
		}
		
		return instance;
	}
	
	private EntityManager getEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("nomePU");
		
		if (this.entityManager == null) {
			this.entityManager = factory.createEntityManager();
		}
		
		return this.entityManager;
	}
	
	public AccountHolder insert(AccountHolder accountHolder) {
		try {
			this.entityManager.getTransaction().begin();
			this.entityManager.persist(accountHolder);
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return accountHolder;
	}
	
	public AccountHolder update(AccountHolder accountHolder) {
		AccountHolder dbAccountHolder = null;
		
		try {			
			if (accountHolder.getId() != null) {
				this.entityManager.getTransaction().begin();
				
				dbAccountHolder = this.findById(accountHolder.getId());
				
				if (dbAccountHolder != null) {
					dbAccountHolder.setName(accountHolder.getName());
					this.entityManager.merge(dbAccountHolder);
				}
				
				this.entityManager.getTransaction().commit();
			}
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return dbAccountHolder;
	}
	
	private AccountHolder findById(Long id) {
		AccountHolder accountHolder = this.entityManager.find(AccountHolder.class, id);
		this.entityManager.close();
		
		return accountHolder;
	}
}
