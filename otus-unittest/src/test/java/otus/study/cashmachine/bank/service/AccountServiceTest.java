package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Spy
    AccountDao accountDao;

    @InjectMocks
    AccountService accountService;

    @AfterEach
    void tearDown() {
        reset(accountDao);
    }

    @Test
    void testCreateAccountCaptor1() {
        BigDecimal newBalance = new BigDecimal(1111);
        doReturn(new Account(1L, newBalance)).when(accountDao).saveAccount(any());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        accountService.createAccount(newBalance);
        verify(accountDao).saveAccount(captor.capture());
        assertEquals(0, captor.getValue().getId());
    }

    @Test
    void testCreateAccountCaptor2() {
        BigDecimal newBalance = new BigDecimal(1111);
        doReturn(new Account(1L, newBalance)).when(accountDao).saveAccount(any());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        accountService.createAccount(newBalance);
        verify(accountDao).saveAccount(captor.capture());
        assertEquals(newBalance, captor.getValue().getBalance());
    }

    @Test
    void testCreateAccountArgumentMatcher() {
        BigDecimal newBalance = new BigDecimal("1000");
        doReturn(new Account(1L, newBalance)).when(accountDao).saveAccount(any(Account.class));
        Account newAccount = accountService.createAccount(newBalance);
        assertNotNull(newAccount);
        Account matchAccount = new Account(0, newBalance);
        verify(accountDao, times(1)).saveAccount(argThat(new AccountMatcher(matchAccount)));
    }

    @Test
    void testPutMoney() {
        Long accId = 1L;
        BigDecimal balance = new BigDecimal(1000);
        BigDecimal addedAmount = new BigDecimal(500);
        Account account = new Account(accId, balance);
        when(accountDao.getAccount(accId)).thenReturn(account);
        BigDecimal result = accountService.putMoney(accId, addedAmount);
        assertEquals(balance.add(addedAmount), result);
    }

    @Test
    void testGetMoneyPositive() {
        Long accId = 1L;
        BigDecimal balance = new BigDecimal(1000);
        BigDecimal subAmount = new BigDecimal(500);
        Account account = new Account(accId, balance);
        when(accountDao.getAccount(accId)).thenReturn(account);

        BigDecimal result = accountService.getMoney(accId, subAmount);

        assertEquals(balance.subtract(subAmount), result);
    }

    @Test
    void testGetMoneyNegative() {
        Long accId = 1L;
        BigDecimal balance = new BigDecimal(1000);
        BigDecimal subAmount = new BigDecimal(1500);
        Account account = new Account(accId, balance);
        when(accountDao.getAccount(accId)).thenReturn(account);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.getMoney(accId, subAmount)
        );

        assertEquals("Not enough money", thrown.getMessage());
    }

    @Test
    void testGetAccount() {
        Long accId = 1L;
        accountService.getAccount(accId);
        verify(accountDao).getAccount(accId);
    }

    @Test
    void testCheckBalance() {
        Long accId = 1L;
        BigDecimal balance = new BigDecimal(1000);
        Account account = new Account(accId, balance);
        when(accountDao.getAccount(accId)).thenReturn(account);
        BigDecimal result = accountService.checkBalance(accId);
        verify(accountDao).getAccount(accId);
        assertEquals(balance, result);
    }

    static class AccountMatcher implements ArgumentMatcher<Account> {

        private final Account mathcAccount;

        AccountMatcher(Account mathcAccount) {
            this.mathcAccount = mathcAccount;
        }

        @Override
        public boolean matches(Account account) {
            return mathcAccount.getId() == account.getId() &&
                    mathcAccount.getBalance().equals(account.getBalance());
        }
    }
}
