package com.mohsinkerai.sample.account;

import static org.junit.Assert.*;

import com.mohsinkerai.sample.helpers.BeanStorage;
import java.util.Optional;
import org.junit.AfterClass;
import org.junit.Before;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Sql2o;

public class AccountRepositoryTest {

  @BeforeClass
  public static void init() throws Exception {
    Class.forName("org.hsqldb.jdbc.JDBCDriver");
    initDatabase();
  }

  @Before
  public void setUp() throws Exception {
    BeanStorage.sql2o = new Sql2o("jdbc:hsqldb:mem:account", "sa", "sa");
  }

  @AfterClass
  public static void destroy() throws Exception {
    try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
      statement.executeUpdate("DROP TABLE account");
      connection.commit();
    }
  }

  private static void initDatabase() throws SQLException {
    try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
      statement.execute("CREATE TABLE account (id INT NOT NULL, balance BIGINT NOT NULL)");
      connection.commit();
      statement.executeUpdate("INSERT INTO account VALUES (1,2000)");
      statement.executeUpdate("INSERT INTO account VALUES (2,3000)");
      connection.commit();
    }
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:hsqldb:mem:account", "sa", "sa");
  }

  @Test
  public void testTransferOfMoney() {
    AccountRepository accountRepository = new AccountRepository();

    // Given
    /**
     * Verifying Balance of Both Accounts before Transfer
     */
    Optional<Account> optionalAccount = accountRepository.getAccount(1);
    assertTrue(optionalAccount.isPresent());
    Account account = optionalAccount.get();
    assertEquals(account.getBalance(), 2000);

    optionalAccount = accountRepository.getAccount(2);
    assertTrue(optionalAccount.isPresent());
    account = optionalAccount.get();
    assertEquals(account.getBalance(), 3000);

    // When
    accountRepository.transfer(1, 2, 500);

    // Then
    /**
     * Verifying Balance of Accounts after Transfer
     */
    optionalAccount = accountRepository.getAccount(1);
    assertTrue(optionalAccount.isPresent());
    account = optionalAccount.get();
    assertEquals(account.getBalance(), 1500);

    optionalAccount = accountRepository.getAccount(2);
    assertTrue(optionalAccount.isPresent());
    account = optionalAccount.get();
    assertEquals(account.getBalance(), 3500);
  }
}