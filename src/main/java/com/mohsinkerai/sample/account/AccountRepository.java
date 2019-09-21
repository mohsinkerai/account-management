package com.mohsinkerai.sample.account;

import static com.mohsinkerai.sample.helpers.BeanStorage.sql2o;

import java.util.List;
import java.util.Optional;
import org.sql2o.Connection;

public class AccountRepository {

  public Optional<Account> getAccount(int accountId) {
    try (Connection conn = sql2o.beginTransaction()) {
      List<Account> accounts = conn.createQuery("SELECT * from account where id = :accountId")
          .addParameter("accountId", accountId)
          .executeAndFetch(Account.class);
      return accounts.stream().findFirst();
    }
  }

  public void transfer(int fromAccount, int toAccount, long amount) {
    try (Connection conn = sql2o.beginTransaction()) {
      conn.createQuery("UPDATE account set balance = balance-:amount where id = :accountId")
          .addParameter("accountId", fromAccount)
          .addParameter("amount", amount)
          .executeUpdate();
      conn.createQuery("UPDATE account set balance=balance+:amount where id = :accountId")
          .addParameter("accountId", toAccount)
          .addParameter("amount", amount)
          .executeUpdate();
      conn.commit();
    }
  }
}
