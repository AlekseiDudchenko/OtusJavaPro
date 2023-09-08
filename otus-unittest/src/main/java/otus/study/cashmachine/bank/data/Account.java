package otus.study.cashmachine.bank.data;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private BigDecimal balance;
    private long id;

    public Account(final long id, final BigDecimal balance) {
        this.balance = balance;
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal amount) {
        this.balance = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Account account = (Account) o;
        return id == account.id && balance.compareTo(account.balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, id);
    }
}
