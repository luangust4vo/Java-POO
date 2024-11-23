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
                "SELECT a FROM account JOIN account_holder ah ON a.account_holder_id = ah.id WHERE ah.cpf = :cpf",
                Account.class).setParameter("cpf", cpf).getResultList());
    }
}
