package com.yanimetaxas.realitycheck;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.Test;

/**
 * @author yanimetaxas
 * @since 24-Mar-18
 */
public class ObjectCheckTest {

  @Test
  public void objectIsEqualTo() throws Exception {
    Object object = new Object();
    assertNotNull(new ObjectCheck(object).isEqualTo(object));
  }

  @Test(expected = AssertionError.class)
  public void objectIsEqualTo_WhenIsNot() throws Exception {
    Object object1 = new Object();
    Object object2 = new Object();
    assertNotNull(new ObjectCheck(object1).isEqualTo(object2));
  }

  @Test
  public void customObjectIsEqualTo() throws Exception {
    Money money = new Money(new BigDecimal(10.1), Currency.getInstance("EUR"));
    assertNotNull(new ObjectCheck(money).isEqualTo(money));
  }

  @Test(expected = AssertionError.class)
  public void customObjectIsEqualTo_WhenIsNot() throws Exception {
    Money moneyEUR = new Money(new BigDecimal(10.1), Currency.getInstance("EUR"));
    Money moneySEK = new Money(new BigDecimal(10.1), Currency.getInstance("SEK"));
    assertNotNull(new ObjectCheck(moneyEUR).isEqualTo(moneySEK));
  }
}

class Money implements Serializable, Comparable<Money> {

  private BigDecimal amount;
  private Currency currency;

  Money(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  private BigDecimal getAmount() {
    return amount;
  }

  @Override
  public int compareTo(Money obj) {
    return getAmount().compareTo(obj.getAmount());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money money = (Money) o;

    return amount.equals(money.amount) && currency.equals(money.currency);
  }

  @Override
  public int hashCode() {
    int result = amount.hashCode();
    result = 31 * result + currency.hashCode();
    return result;
  }
}
