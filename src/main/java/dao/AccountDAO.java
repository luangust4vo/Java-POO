package dao;

import java.util.List;

import model.Account;

public final class AccountDAO extends GenericDAO<Account> {
    public AccountDAO() {
        super(Account.class);
    }

    public static AccountDAO getInstance() {
        return new AccountDAO();
    }

    public List<Account> getAccountsByCpf(String cpf) {
        return execute(em -> em.createQuery(
                "SELECT a FROM Account a WHERE a.accountHolder.cpf = :cpf",
                Account.class).setParameter("cpf", cpf).getResultList());
    }
}
