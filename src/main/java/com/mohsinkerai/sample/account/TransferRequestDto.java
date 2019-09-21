package com.mohsinkerai.sample.account;

public class TransferRequestDto {

  private int fromAccount;
  private int toAccount;
  private long amount;

  public TransferRequestDto(int fromAccount, int toAccount, long amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

  public int getFromAccount() {
    return fromAccount;
  }

  public void setFromAccount(int fromAccount) {
    this.fromAccount = fromAccount;
  }

  public int getToAccount() {
    return toAccount;
  }

  public void setToAccount(int toAccount) {
    this.toAccount = toAccount;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }
}
