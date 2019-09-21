package com.mohsinkerai.sample.account;

import java.math.BigDecimal;

public class Account {

  private final int id;
  private final long balance; // Upto 2 Decimal places

  public Account(int id, long balance) {
    this.id = id;
    this.balance = balance;
  }

  public int getId() {
    return id;
  }

  public long getBalance() {
    return balance;
  }
}
