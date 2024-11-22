package dao;

import model.AccountHolder;

public class AccountHolderDAO extends GenericDAO<AccountHolder> {
	public AccountHolderDAO() {
		super(AccountHolder.class);
	}

	public static AccountHolderDAO getInstance() {
		return new AccountHolderDAO();
	}

	public AccountHolder findByCpf(String cpf) {
		return execute(em -> em.createQuery(
			"SELECT id FROM account_holder ah WHERE ah.cpf = :cpf", AccountHolder.class)
			.setParameter("cpf", cpf).getSingleResult()
		);
	}
}