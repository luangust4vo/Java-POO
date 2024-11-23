package services;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;
import model.types.AccountType;
import utils.AccountUtils;
import utils.ValidationUtils;

public class AccountService {
    private final AccountDAO dao;
    private final static Double COMPOUND_INTEREST_RATE = 0.03;

    public AccountService() {
		dao = AccountDAO.getInstance();
	}

    public Account store(Account account) {
        return ValidationUtils.execute(() -> {
            if (AccountUtils.isMaximumAccountNumberReached(account.getAccountHolder().getCpf())) {
                throw new Exception("Número máximo de 3 no mesmo CPF contas atingido!");
            }

            Account account2 = dao.store(account);
            return account2;
        });
    }

    public List<Account> getAccountsByCpf(String cpf) {
        return dao.getAccountsByCpf(cpf);
    }

    public Double getPreApprovedCreditLimit(Long id, Account account) {
        if (account.getType().equals(AccountType.CURRENT_ACCOUNT)) {
            Date startDate = new Date();
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);
            calendar.add(Calendar.MONTH, -3);
            Date endDate = calendar.getTime();

            List<Transaction> transactions = new TransactionDAO().getTransactionsByPeriod(id, startDate, endDate);

            Double average = transactions.stream().mapToDouble(Transaction::getValue).average().orElse(0.0);

            return average * 0.5;
        } else return 0.0;
    }

    public Double getMonthlyIncome(Account account, int months) {
        if (account.getType().equals(AccountType.SAVINGS_ACCOUNT)) {
           return new TransactionService().getBalance(account.getId()) * Math.pow((1 + COMPOUND_INTEREST_RATE), months);
        } else return 0.0;
    }
}
