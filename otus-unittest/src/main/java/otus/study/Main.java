package otus.study;

import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.service.IAccountService;
import otus.study.cashmachine.bank.service.ICardService;
import otus.study.cashmachine.bank.service.impl.AccountService;
import otus.study.cashmachine.bank.service.impl.CardService;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.ICashMachineService;
import otus.study.cashmachine.machine.service.IMoneyBoxService;
import otus.study.cashmachine.machine.service.impl.CashMachineService;
import otus.study.cashmachine.machine.service.impl.MoneyBoxService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class Main {
    static AccountDao accountDao = new AccountDao();

    static IAccountService accountService;
    static CardsDao cardsDao;
    static ICardService cardService;
    static IMoneyBoxService moneyBoxService;
    static ICashMachineService cashMachineService;


    static {
        accountService = new AccountService(accountDao);
        cardsDao = new CardsDao();
        cardService = new CardService(accountService, cardsDao);
        moneyBoxService = new MoneyBoxService();
        cashMachineService = new CashMachineService(cardService, accountService, moneyBoxService);
    }

    public static void main(String[] args) {
        MoneyBox moneyBox = new MoneyBox();
        CashMachine cashMachine = new CashMachine(moneyBox);

        BigDecimal initialSum = cashMachineService.checkBalance(cashMachine, "1111", "0000");
        System.out.println("Initial sum " + initialSum);

        List<Integer> takenAmount = cashMachineService.getMoney(cashMachine, "1111", "0000", BigDecimal.valueOf(4000));
        System.out.println("Taken notes " + takenAmount);

        initialSum = cashMachineService.checkBalance(cashMachine, "1111", "0000");
        System.out.println("New sum " + initialSum);

        cashMachineService.putMoney(cashMachine, "1111", "0000", Arrays.asList(0, 0, 0, 1));
        initialSum = cashMachineService.checkBalance(cashMachine, "1111", "0000");
        System.out.println("New sum " + initialSum);

        cashMachineService.changePin("1111", "0000", "0001");

        System.out.println("");
    }
}