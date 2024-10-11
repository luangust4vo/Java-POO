package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Account;

public class AccountDAO {
	private static AccountDAO instance;
	protected EntityManager entityManager;
	
	private AccountDAO() {
		entityManager = getEntityManager();
	}
	
	public static AccountDAO getInstance() { // Just to don´t need instantiate with 'new ...' 
		if (instance == null) {
			instance = new AccountDAO();
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
	
	public void insert(Account account) {
		try {
			// Create a new account on database
			this.entityManager.getTransaction().begin();
			this.entityManager.persist(account);
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
	}
	
	public Account update(Account account) {
		Account dbAccount = null;
		
		try {			
			if (account.getId() != null) {
				this.entityManager.getTransaction().begin();
				
				dbAccount = this.findById(account.getId());
				
				if (dbAccount != null) {
					dbAccount.setDescription(account.getDescription());
					this.entityManager.merge(dbAccount);
				}
				
				this.entityManager.getTransaction().commit();
			}
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return dbAccount;
	}
	
	public void delete(Long id) {
		try {
			this.entityManager.getTransaction().begin();
			
			Account dbAccount = this.entityManager.find(Account.class, id);
			
			if (dbAccount != null) {
				this.entityManager.remove(dbAccount);
			}
			
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> list() {
		List<Account> accounts = null;
		
		try {
			this.entityManager.getTransaction().begin();
			accounts = this.entityManager.createQuery("FROM account").getResultList();
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return accounts;
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> listCpf(String cpf) {
		List<Account> accounts = null;
		
		try {
			this.entityManager.getTransaction().begin();
			accounts = this.entityManager.createQuery("FROM account WHERE account_holder_cpf = '" + cpf + "'").getResultList();
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return accounts;
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> listTransaction(String trans_type) {
		List<Account> accounts = null;
		
		try {
			this.entityManager.getTransaction().begin();
			accounts = this.entityManager.createQuery("FROM account WHERE transaction_type = '" + trans_type + "'").getResultList();
			this.entityManager.getTransaction().commit();
		} catch (Exception exc) {
			this.entityManager.getTransaction().rollback();
			exc.printStackTrace();
		}
		
		this.entityManager.close();
		return accounts;
	}
	
	private Account findById(Long id) {
		Account account = this.entityManager.find(Account.class, id);
		this.entityManager.close();
		
		return account;
	}
}
