package com.mohsinkerai.sample.helpers;

import static spark.Spark.exception;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

import com.google.gson.Gson;
import org.sql2o.Sql2o;

public final class Initializer {

  private static final String ACCOUNT_TABLE = "create table ACCOUNT(\n"
      + "\tID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_38AF3FE1_9336_432F_87EB_69ADE6C49E7F)primary key,\n"
      + "\tBALANCE BIGINT);";

  public static final void initializeAll() {
    initializeNotFound();
    initializeInternalServerErrorHandler();
    initializeCustomExceptionHandler();
    initializeH2Database();
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

  public static final void initializeH2Database() {
    BeanStorage.sql2o = new Sql2o("jdbc:mysql://localhost:3306/spark", "root", "");
  }
}
