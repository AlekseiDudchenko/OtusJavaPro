package otus.study.cashmachine.bank.service.impl;

import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.IAccountService;

import java.math.BigDecimal;

public class AccountService implements IAccountService {
    AccountDao accountDao;

    public AccountService(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account createAccount(BigDecimal amount) {
        Account newAccount = new Account(0, amount);
        return accountDao.saveAccount(newAccount);
    }

    @Override
    public Account getAccount(Long id) {
        return accountDao.getAccount(id);
    }

    @Override
    public BigDecimal getMoney(Long id, BigDecimal amount) {
        Account account = accountDao.getAccount(id);
        if (account.getBalance().subtract(amount).doubleValue() < 0) {
            throw new IllegalArgumentException("Not enough money");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return account.getBalance();
    }

    @Override
    public BigDecimal putMoney(Long id, BigDecimal amount) {
        Account account = accountDao.getAccount(id);
        account.setBalance(account.getBalance().add(amount));
        return account.getBalance();
    }

    @Override
    public BigDecimal checkBalance(Long id) {
        Account account = accountDao.getAccount(id);
        return account.getBalance();
    }
}