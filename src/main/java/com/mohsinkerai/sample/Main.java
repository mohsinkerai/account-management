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
  }
}
