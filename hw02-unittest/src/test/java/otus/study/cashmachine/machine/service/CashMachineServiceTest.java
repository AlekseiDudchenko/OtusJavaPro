package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.service.IAccountService;
import otus.study.cashmachine.bank.service.impl.CardService;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Spy
    @InjectMocks
    private CardService cardService;

    @Mock
    private CardsDao cardsDao;

    @Mock
    private IAccountService accountService;

    @Mock
    private IMoneyBoxService moneyBoxService;

    private CashMachineService cashMachineService;

    private CashMachine cashMachine = new CashMachine(new MoneyBox());

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineService(cardService, accountService, moneyBoxService);
    }

    @Test
    void testGetMoney() {
        String cardNum = "123456";
        String pin = "1234";
        BigDecimal amount = new BigDecimal(100);

        doReturn(new BigDecimal(100)).when(cardService).getMoney(eq(cardNum), eq(pin), eq(amount));
        when(moneyBoxService.getMoney(any(), anyInt())).thenReturn(Arrays.asList(20, 20, 20, 20, 20));

        // Act
        List<Integer> result = cashMachineService.getMoney(cashMachine, cardNum, pin, amount);

        verify(cardService).getMoney(eq(cardNum), eq(pin), eq(amount));
        verify(moneyBoxService).getMoney(any(), eq(amount.intValue()));
        assertEquals(Arrays.asList(20, 20, 20, 20, 20), result);
    }

    @Test
    void testGetMoneyException() {
        // Arrange
        String cardNum = "123456";
        String pin = "1234";
        BigDecimal amount = new BigDecimal(100);

        doThrow(new IllegalArgumentException("No card found")).when(cardService).getMoney(cardNum, pin, amount);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cashMachineService.getMoney(cashMachine, cardNum, pin, amount));

        verify(moneyBoxService, never()).getMoney(any(), anyInt());
        verify(cardService).putMoney(cardNum, pin, amount);
    }

    @Test
    public void testPutMoney() {
        List<Integer> notes = Arrays.asList(1, 2, 3);
        String cardNum = "12345";
        String pin = "1111";
        BigDecimal oldBalance = new BigDecimal("1000");
        BigDecimal newBalance = new BigDecimal("9000");
        doReturn(oldBalance).when(cardService).getBalance(anyString(), anyString());
        doReturn(newBalance).when(cardService).putMoney(anyString(), anyString(), any(BigDecimal.class));
        BigDecimal result = cashMachineService.putMoney(cashMachine, cardNum, pin, notes);
        verify(cardService).getBalance(cardNum, pin);
        verify(cardService).putMoney(eq(cardNum), eq(pin), any(BigDecimal.class));
        verify(moneyBoxService).putMoney(any(MoneyBox.class), eq(0), eq(3), eq(2), eq(1));
        assertEquals(newBalance, result);
    }

    @Test
    void checkBalance() {

    }

    @Test
    void changePin() {
        String cardNum = "1111";
        String oldPin = "0000";
        String newPin = "2222";

        doReturn(true).when(cardService).cnangePin(anyString(), anyString(), anyString());
        ArgumentCaptor<String> cardNumCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> oldPinCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> newPinCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        assertTrue(cashMachineService.changePin(cardNum, oldPin, newPin));

        verify(cardService).cnangePin(cardNumCaptor.capture(), oldPinCaptor.capture(), newPinCaptor.capture());
        assertEquals(cardNum, cardNumCaptor.getValue());
        assertEquals(oldPin, oldPinCaptor.getValue());
        assertEquals(newPin, newPinCaptor.getValue());
    }

    @Test
    void changePinWithAnswer() {
        // Arrange
        String cardNum = "12345";
        String oldPin = "1111";
        String newPin = "2222";


        // Use thenAnswer to define custom behavior for the spy
        doAnswer(invocation -> {
            String oldPinArg = invocation.getArgument(1);
            String newPinArg = invocation.getArgument(2);

            return oldPinArg.equals("1111") && newPinArg.equals("2222");
        }).when(cardService).cnangePin(eq(cardNum), anyString(), anyString());


        // Act
        assertTrue(cardService.cnangePin(cardNum, oldPin, newPin));

        // Assert
        // Use ArgumentCaptor to capture the arguments and verify them, if necessary.
        ArgumentCaptor<String> cardNumCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> oldPinCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> newPinCaptor = ArgumentCaptor.forClass(String.class);

        verify(cardService).cnangePin(cardNumCaptor.capture(), oldPinCaptor.capture(), newPinCaptor.capture());
        assertEquals(cardNum, cardNumCaptor.getValue());
        assertEquals(oldPin, oldPinCaptor.getValue());
        assertEquals(newPin, newPinCaptor.getValue());
    }
}