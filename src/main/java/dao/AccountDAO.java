package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Account;

public class AccountDAO {
	public static void main(String[] args) { // Just for tests
		Account account = new Account();
		account.setAccountHolderCpf("044");
		account.setAccountHolderName("Luan");
		account.setDescription("Teste");
		account.setTransactionDate(new Date());
		account.setTransactionType("Depósito");
		account.setValue(500.);
		
		new AccountDAO().insert(account);
	}
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("nomePU");
	
	public Account insert(Account account) {
		// Create a new account on database
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(account);
		em.getTransaction().commit();
		em.close();
		
		return null;
	}
	
	public Account update(Account account) {
		return null;
	}
	
	public void delete(Long id) {
		
	}
	
	public List<Account> list() {
		return null;
	}
}
