package services;

import dao.AccountHolderDAO;
import model.AccountHolder;
import utils.AccountHolderUtils;
import utils.ValidationUtils;

public class AccountHolderService {
    private final AccountHolderDAO dao;
     
    public AccountHolderService() {
        dao = AccountHolderDAO.getInstance();
    }

    public AccountHolder store (AccountHolder accountHolder) {
        return ValidationUtils.execute(() -> {
            if (!AccountHolderUtils.validateCpf(accountHolder.getCpf())) {
                throw new Exception("CPF inválido. Informe um CPF válido para criar uma conta");
            }

            AccountHolder accountHolder2 = dao.store(accountHolder);

            return accountHolder2;
        });
    }

    public AccountHolder findByCpf(String cpf) {
        return ValidationUtils.execute(() -> {
            if (!AccountHolderUtils.validateCpf(cpf)) {
                throw new Exception("CPF inválido. Informe um CPF válido para acessar a conta");
            }

            AccountHolder accountHolder = dao.findOne(cpf);

            return accountHolder;
        });
    }
}
