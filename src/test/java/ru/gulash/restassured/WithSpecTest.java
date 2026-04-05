package ru.gulash.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class WithSpecTest {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    @BeforeAll
    public static void setup() {
        // RequestSpecBuilder позволяет собрать повторяющиеся параметры запроса
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL) // логировать все запросы
                .build();

        // ResponseSpecBuilder позволяет собрать повторяющиеся проверки ответа
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL) // логировать все ответы
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Проверка получения пользователя по ID с использованием спецификаций")
    public void testGetUser() {
        given()
            .spec(requestSpec) // Применяем общие настройки запроса
            .pathParam("id", 1)
        .when()
            .get("/users/{id}")
        .then()
            .spec(responseSpec) // Применяем общие проверки ответа
            .body("username", equalTo("Bret"))
            .body("email", equalTo("Sincere@april.biz"));
    }
}
