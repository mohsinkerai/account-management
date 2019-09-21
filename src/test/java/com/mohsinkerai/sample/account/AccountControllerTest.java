package com.mohsinkerai.sample.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static spark.Spark.init;
import static spark.Spark.threadPool;

import com.google.gson.Gson;
import com.mohsinkerai.sample.helpers.BeanStorage;
import com.mohsinkerai.sample.helpers.Initializer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Sql2o;

public class AccountControllerTest {

  @BeforeClass
  public static void init() throws Exception {
    Class.forName("org.hsqldb.jdbc.JDBCDriver");
    initDatabase();
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
      statement.execute("CREATE TABLE IF NOT EXISTS account (id INT NOT NULL, balance BIGINT NOT NULL)");
      statement.execute("TRUNCATE TABLE account");
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
  public void testTransferOfMoney() throws Exception {
    threadPool(10);
    init();
    Initializer.initializeRestEndpoints();

    // Given
    HttpResponse<String> response = Unirest.get("http://127.0.0.1:4567/account/1")
        .asString();
    Account account = new Gson().fromJson(response.getBody(), Account.class);
    assertEquals(2000, account.getBalance());
    assertEquals(1, account.getId());

    response = Unirest.get("http://127.0.0.1:4567/account/2")
        .asString();
    account = new Gson().fromJson(response.getBody(), Account.class);
    assertEquals(3000, account.getBalance());
    assertEquals(2, account.getId());

    // When
    Unirest.post("http://127.0.0.1:4567/account/transfer")
        .body(new Gson().toJson(new TransferRequestDto(1,2,500))).asEmpty();

    response = Unirest.get("http://127.0.0.1:4567/account/1")
        .asString();
    account = new Gson().fromJson(response.getBody(), Account.class);
    assertEquals(1500, account.getBalance());
    assertEquals(1, account.getId());

    response = Unirest.get("http://127.0.0.1:4567/account/2")
        .asString();
    account = new Gson().fromJson(response.getBody(), Account.class);
    assertEquals(3500, account.getBalance());
    assertEquals(2, account.getId());
  }
}