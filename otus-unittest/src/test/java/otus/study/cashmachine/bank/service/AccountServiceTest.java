package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AccountServiceTest {

    @Spy
    AccountDao accountDao;

    AccountServiceImpl accountServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        accountServiceImpl = new AccountServiceImpl(accountDao);
    }

    @AfterEach
    void tearDown() {
        reset(accountDao);
    }

    @Test
    void testCreateAccountCaptor() {
        BigDecimal newBalance = new BigDecimal(1111);

        doReturn(new Account(1L, newBalance)).when(accountDao).saveAccount(any());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        // Act
        accountServiceImpl.createAccount(newBalance);

        verify(accountDao).saveAccount(captor.capture());
        assertEquals(0, captor.getValue().getId());
        assertEquals(newBalance, captor.getValue().getBalance());
    }

    @Test
    void testCreateAccountArgumentMatcher() {
        BigDecimal newBalance = new BigDecimal("1000");

        doReturn(new Account(1L, newBalance)).when(accountDao).saveAccount(any(Account.class));

        // Act
        Account newAccount = accountServiceImpl.createAccount(newBalance);

        assertNotNull(newAccount);
        Account matchAccount = new Account(0, newBalance);
        verify(accountDao, times(1)).saveAccount(argThat(new AccountMatcher(matchAccount)));
    }

    @Test
    void addSum() {
    }

    @Test
    void getSum() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void checkBalance() {
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
