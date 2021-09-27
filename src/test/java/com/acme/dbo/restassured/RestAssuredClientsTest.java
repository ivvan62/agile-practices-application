package com.acme.dbo.restassured;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;


public class RestAssuredClientsTest {
    private RequestSpecification request;
    private final String BASE_URI = "http://localhost";
    private final int PORT = 8080;
    private final String BASE_PATH = "/dbo/api/";


    @BeforeEach
    public void init() {
        request = given()
                .baseUri(BASE_URI)
                .port(PORT)
                .basePath(BASE_PATH)
                .header("X-API-VERSION", 1)
                .contentType(ContentType.JSON)
                .filters(new RequestLoggingFilter(), new RequestLoggingFilter());
    }

    @Test
    public void shouldGetClientById() {
        request
            .when()
                .get("/client/{id}", 2)
            .then()
                .statusCode(SC_OK)
                .body("id", is (2),
                        "login", is ("account@acme.com"),
                        "secret", is ("5aba80f0c9f7cfb0c7e8d5767aad85e8b384872e070c13a8fe6d11f58327934b"),
                        "salt", is ("somesalt"),
                        "created", is ("2021-09-24T09:26:06.117Z"),
                        "enabled", is (true));
    }

    @Test
    public void shouldGetClientIdIsNotFound() {
        request
            .when()
                .get("/client/{id}", 666)
            .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void shouldDeleteClientByLogin () {
        request
            .when()
                .body("{ \n" +
                        " \"login\" : \"testtesttest@acme.com\", \n" +
                        " \"secret\" : \"c99ef573720e30031034d24e82721350dfa6af9957d267c2acc0be98813bb3e5\", \n" +
                        " \"salt\" : \"somesalt\"" +
                        "}")
                .post("client")
            .then().statusCode(SC_CREATED);
        request
            .when()
                .delete("/client/login/{login}", "testtesttest@acme.com")
            .then()
                .statusCode(SC_OK);
    }

    @Test
    public void shouldDeleteClientIsNotFoundId () {
        request
            .when()
                .delete("/client/login/{id}", 777)
            .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void shouldDeleteClientIsNotFoundLogin () {
        request
            .when()
                .delete("/client/login/{login}", "777@tinkoff.ru")
            .then()
                .statusCode(SC_NOT_FOUND);
    }


}
