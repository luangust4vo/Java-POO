package dao;

import model.AccountHolder;

public final class AccountHolderDAO extends GenericDAO<AccountHolder> {
	public AccountHolderDAO() {
		super(AccountHolder.class);
	}

	public static AccountHolderDAO getInstance() {
		return new AccountHolderDAO();
	}

	public AccountHolder findOne(String cpf) {
		return execute(em -> em.createQuery(
			"SELECT ah FROM AccountHolder ah WHERE ah.cpf = :cpf", AccountHolder.class)
			.setParameter("cpf", cpf).getSingleResult()
		);
	}
}