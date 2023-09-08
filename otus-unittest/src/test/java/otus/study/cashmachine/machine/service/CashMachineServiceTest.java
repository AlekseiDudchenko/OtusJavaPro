package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.impl.CardServiceImpl;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Spy
    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardsDao cardsDao;

    @Mock
    private AccountService accountService;

    @Mock
    private MoneyBoxService moneyBoxService;

    private CashMachineServiceImpl cashMachineService;

    private CashMachine cashMachine = new CashMachine(new MoneyBox());

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineServiceImpl(cardService, accountService, moneyBoxService);
    }

    @Test
    void getMoney() {
// @TODO create get money test using spy as mock
// Arrange
        CashMachine machine = mock(CashMachine.class);
        MoneyBox moneyBox = mock(MoneyBox.class);
        when(machine.getMoneyBox()).thenReturn(moneyBox);

        String cardNum = "12345";
        String pin = "6789";
        BigDecimal amount = new BigDecimal("100");

        when(cardService.getMoney(cardNum, pin, amount)).thenReturn(amount);
        when(moneyBoxService.getMoney(moneyBox, amount.intValue())).thenReturn(Arrays.asList(50, 50));

        // Act
        List<Integer> result = cashMachineService.getMoney(machine, cardNum, pin, amount);

        // Assert
        verify(cardService).getMoney(cardNum, pin, amount);
        verify(moneyBoxService).getMoney(moneyBox, amount.intValue());
        assertEquals(Arrays.asList(50, 50), result);

//        getMoney(CashMachine machine, String cardNum, String pin, BigDecimal amount)
        cashMachineService.getMoney(cashMachine, "1111", "1111", BigDecimal.valueOf(1000));

    }

    @Test
    void putMoney() {
    }

    @Test
    void checkBalance() {

    }

    @Test
    void changePin() {
// @TODO create change pin test using spy as implementation and ArgumentCaptor and thenReturn
    }

    @Test
    void changePinWithAnswer() {
// @TODO create change pin test using spy as implementation and mock an thenAnswer
    }
}