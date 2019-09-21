package com.mohsinkerai.sample.helpers;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.mohsinkerai.sample.account.AccountRepository;
import com.mohsinkerai.sample.account.TransferRequestDto;
import org.sql2o.Sql2o;
import spark.Spark;

public final class Initializer {

  public static final void initializeAll() {
    initializeRestEndpoints();
    initializeDatabaseConnection();
  }

  public static final void initializeRestEndpoints() {
    initializeNotFound();
    initializeInternalServerErrorHandler();
    initializeCustomExceptionHandler();
    applicationRoutes();
  }



  public static final void initializeNotFound() {
    // Using Route
    notFound((req, res) -> {
      res.type("application/json");
      return "{\"message\":\"Custom 404\"}";
    });
  }

  public static final void initializeInternalServerErrorHandler() {
    // Using Route
    internalServerError((req, res) -> {
      res.type("application/json");
      return "{\"message\":\"Something is Wrong\"}";
    });
  }

  public static final void initializeCustomExceptionHandler() {
    exception(CustomException.class, (exception, request, response) -> {
      String body = new Gson().toJson(exception);
      response.body(body);
    });
  }

  public static final void initializeDatabaseConnection() {
    BeanStorage.sql2o = new Sql2o("jdbc:mysql://localhost:3306/spark", "root", "");
  }

  public static final void applicationRoutes() {
    AccountRepository accountRepository = new AccountRepository();
    Gson gson = new Gson();

    Spark.path("/account", () -> {
      get("/:accountId", (request, response) -> {
        String accountId = request.params(":accountId");
        Integer accountIdInteger = Integer.valueOf(accountId);
        return accountRepository.getAccount(accountIdInteger).orElseThrow(() -> new CustomException(2, String.format("Account %d Does't exists", accountIdInteger)));
      }, gson::toJson);
      post("/transfer", (request, response) -> {
        TransferRequestDto transferRequestDto = gson
            .fromJson(request.body(), TransferRequestDto.class);
        accountRepository.transfer(transferRequestDto.getFromAccount(), transferRequestDto.getToAccount(), transferRequestDto.getAmount());
        return "{}";
      });
    });
  }
}
