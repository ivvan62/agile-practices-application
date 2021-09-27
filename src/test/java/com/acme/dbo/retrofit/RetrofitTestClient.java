package com.acme.dbo.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RetrofitTestClient {
    ClientService service;

    @BeforeEach
    public void init() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("http://localhost:8080/dbo/api/")
                .client(httpClient.build())
                .build();
        service = retrofit.create(ClientService.class);
    }

    @Test
    public void shouldGetClient() throws IOException {
        service.getClients().execute().body().forEach(System.out::println);
    }

    @Test
    public void shouldPostClient() throws IOException {
        String loginClient = service.createClient(new Client("2@2.ru", "same-salt", "2131231ffd43400000"))
                .execute().body().getLogin();
        assertEquals("2@2.ru", loginClient);
    }
}