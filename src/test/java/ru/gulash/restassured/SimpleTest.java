package ru.gulash.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SimpleTest {

    @BeforeAll
    public static void setup() {
        // Можно использовать общие настройки для всех тестов в этом классе

        // Установка базового URI для всех запросов в этом классе или явно в методе
        //RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Проверка получения пользователя по ID")
    public void testGetUser() {
        // можно использовать многострочные строки для запроса
        String requestBody = """
        {
            "name": "Ivan Petrov",
            "email": "ivan@example.com"
        }
        """;
        System.out.println("=====================/n");
        //Given  — предусловия (базовый URL, заголовки, тело запроса, авторизация)
        given() // Секция подготовки (заголовки, параметры, тело запроса)
            .baseUri("https://jsonplaceholder.typicode.com")
            //.contentType(ContentType.JSON)
            //.body(requestBody)
            .pathParam("id", 1)
        //When   — само HTTP-действие (GET, POST, PUT, DELETE)
        .when() // Секция действия (HTTP метод и эндпоинт)
            .log().all() // Логируем все детали исходящего запроса (URI, метод, заголовки, тело)
            .get("/users/{id}")
        //Then   — проверки ответа (статус, тело, заголовки)
        .then() // Секция проверок (валидация ответа)
            //.log().all() // Логируем весь ответ полностью
            .log().body() // Выводим в консоль только тело ответа
            //.log().headers() // Логируем только заголовки ответа
            //.log().status() // Логируем только строку статуса (HTTP 200 OK)
            //.log().cookies() // Логируем только куки
            //.log().ifValidationFails() // Логирует детали только в случае провала теста
            //.log().ifError() // Логирует только если код ответа >= 400
            .statusCode(200) // Проверка, что сервер вернул код 200 OK
            .body("username", equalTo("Bret")) // Валидация JSON-поля username через Hamcrest-матчер
            .body("email", equalTo("Sincere@april.biz")); // Валидация JSON-поля email
        System.out.println("=====================/n");

    }
}

