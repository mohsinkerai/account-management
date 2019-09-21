package com.mohsinkerai.sample;

import static com.mohsinkerai.sample.helpers.Initializer.initializeAll;
import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.threadPool;

import com.google.gson.Gson;
import com.mohsinkerai.sample.account.AccountRepository;
import com.mohsinkerai.sample.account.TransferRequestDto;
import com.mohsinkerai.sample.helpers.CustomException;
import spark.Spark;

public class Main {

  public static void main(String[] args) {
    port(8081);
    threadPool(10);
    init();
    initializeAll();

    AccountRepository accountRepository = new AccountRepository();
    Gson gson = new Gson();

    Spark.path("/account", () -> {
      get("/:accountId", (request, response) -> {
        String accountId = request.params(":accountId");
        Integer accountIdInteger = Integer.valueOf(accountId);
        return accountRepository.getAccount(accountIdInteger);
      }, gson::toJson);
      post("/transfer", (request, response) -> {
        TransferRequestDto transferRequestDto = gson
            .fromJson(request.body(), TransferRequestDto.class);
        accountRepository.transfer(transferRequestDto.getFromAccount(), transferRequestDto.getToAccount(), transferRequestDto.getAmount());
        return "{}";
      });
    });

    get("/exception", (request, response) -> {
      throw new CustomException(1, "Something Bad Happened");
    });
  }
}
