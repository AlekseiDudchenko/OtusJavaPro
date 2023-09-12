package otus.study.cashmachine.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import otus.study.cashmachine.TestUtil;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.impl.CardService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    private final static String NO_CARD_EXCEPTION_MESSAGE = "No card found";
    private final static String PIN_EXCEPTION_MESSAGE = "Pin code is incorrect";
    IAccountService accountService;

    CardsDao cardsDao;

    ICardService cardService;

    @BeforeEach
    void init() {
        cardsDao = mock(CardsDao.class);
        accountService = mock(IAccountService.class);
        cardService = new CardService(accountService, cardsDao);
    }

    @Test
    void testCreateCard() {
        when(cardsDao.createCard("5555", 1L, "0123")).thenReturn(
                new Card(1L, "5555", 1L, "0123"));

        Card newCard = cardService.createCard("5555", 1L, "0123");
        assertNotEquals(0, newCard.getId());
        assertEquals("5555", newCard.getNumber());
        assertEquals(1L, newCard.getAccountId());
        assertEquals("0123", newCard.getPinCode());
    }

    @Test
    void testGetBalance() {
        Card card = new Card(1L, "1234", 1L, "0000");
        when(cardsDao.getCardByNumber(anyString())).thenReturn(card);
        when(accountService.checkBalance(1L)).thenReturn(new BigDecimal(1000));

        BigDecimal sum = cardService.getBalance("1234", "0000");
        assertEquals(0, sum.compareTo(new BigDecimal(1000)));
    }

    @Test
    void testGetBalanceException() {
        when(cardsDao.getCardByNumber(anyString())).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cardService.getBalance("1111", "0000"));
        assertEquals(NO_CARD_EXCEPTION_MESSAGE, ex.getMessage());
    }

    @Test
    void getMoney() {
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        when(cardsDao.getCardByNumber("1111"))
                .thenReturn(new Card(1L, "1111", 100L, "0000"));

        when(accountService.getMoney(idCaptor.capture(), amountCaptor.capture()))
                .thenReturn(BigDecimal.TEN);

        cardService.getMoney("1111", "0000", BigDecimal.ONE);

        verify(accountService, only()).getMoney(anyLong(), any());
        assertEquals(BigDecimal.ONE, amountCaptor.getValue());
        assertEquals(100L, idCaptor.getValue().longValue());
    }

    @Test
    void getMoneyException() {
        when(cardsDao.getCardByNumber(anyString())).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cardService.getMoney("1111", "0000", new BigDecimal(10)));
        assertEquals(NO_CARD_EXCEPTION_MESSAGE, ex.getMessage());
    }

    @Test
    void testPutMoneyNoCardException() {
        String cardNum = "1111";
        String pin = "0000";
        BigDecimal sum = new BigDecimal(5000);
        when(cardsDao.getCardByNumber(cardNum)).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                                () -> cardService.putMoney(cardNum, pin, sum));
        assertEquals(NO_CARD_EXCEPTION_MESSAGE, ex.getMessage());
    }

    @Test
    void testPutMoneyPinException() {
        long cardId = 1L;
        long accId = 2L;
        String cardNum = "1111";
        String pin = "0000";
        BigDecimal sum = new BigDecimal(5000);
        Card card = new Card(cardId, cardNum, accId, pin);
        when(cardsDao.getCardByNumber(cardNum)).thenReturn(card);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cardService.putMoney(cardNum, "wrongPin", sum));
        assertEquals(PIN_EXCEPTION_MESSAGE, ex.getMessage());
    }

    @Test
    void testPutMoney() {
        long cardId = 1L;
        long accId = 2L;
        String cardNum = "1111";
        String pin = "0000";
        BigDecimal sum = new BigDecimal(5000);
        BigDecimal newAmount = new BigDecimal(6000);
        Card card = new Card(cardId, cardNum, accId, pin);
        when(cardsDao.getCardByNumber(cardNum)).thenReturn(card);
        when(accountService.putMoney(accId, sum)).thenReturn(newAmount);
        BigDecimal result = cardService.putMoney(cardNum, pin, sum);
        verify(cardsDao).getCardByNumber(cardNum);
        verify(accountService).putMoney(card.getAccountId(), sum);
        assertEquals(newAmount, result);
    }

    @Test
    void checkIncorrectPin() {
        Card card = new Card(1L, "1234", 1L, "0000");
        when(cardsDao.getCardByNumber(eq("1234"))).thenReturn(card);

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            cardService.getBalance("1234", "0012");
        });
        assertEquals(PIN_EXCEPTION_MESSAGE, thrown.getMessage());
    }

    @Test
    void testChangePinException() {
        String cardNumber = "1111";
        String oldPin = "0000";
        String newPin = "0001";
        when(cardsDao.getCardByNumber(anyString())).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cardService.cnangePin(cardNumber, oldPin, newPin));
        assertEquals(NO_CARD_EXCEPTION_MESSAGE, ex.getMessage());
    }

    @Test
    void testChangePin() {
        long cardId = 1L;
        long accId = 2L;
        String cardNumber = "1111";
        String oldPin = "0000";
        String newPin = "0001";
        Card card = new Card(cardId, cardNumber, accId, oldPin);
        when(cardsDao.getCardByNumber(cardNumber)).thenReturn(card);
        assertTrue(cardService.cnangePin(cardNumber, oldPin, newPin));
        verify(cardsDao).saveCard(card);
        assertEquals(newPin, card.getPinCode());
    }

    @Test
    void testChangePinWrongPin() {
        long cardId = 1L;
        long accId = 2L;
        String cardNumber = "1111";
        String oldPin = "0000";
        String newPin = "0001";
        Card card = new Card(cardId, cardNumber, accId, oldPin);
        when(cardsDao.getCardByNumber(cardNumber)).thenReturn(card);
        assertFalse(cardService.cnangePin(cardNumber, "wrongPin", newPin));
        assertEquals(oldPin, card.getPinCode());
    }
}