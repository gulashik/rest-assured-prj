package ru.gulash.restassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

public class ExtractionTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Извлечение значения по JsonPath выражению")
    public void testExtractJsonPath() {
        // Извлекаем только email конкретного пользователя
        String email = given()
            .pathParam("id", 1)
        .when()
            .get("/users/{id}")
        .then()
            .statusCode(200)
            .extract()
            .path("email");

        Assertions.assertEquals("Sincere@april.biz", email);
        System.out.println("Extracted email: " + email);
    }

    @Test
    @DisplayName("Извлечение всего Response объекта")
    public void testExtractFullResponse() {
        // Извлекаем весь объект ответа для дальнейших манипуляций
        Response response = given()
            .pathParam("id", 1)
        .when()
            .get("/users/{id}")
        .then()
            .statusCode(200)
            .extract()
            .response();

        // Проверяем данные из объекта Response
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("application/json; charset=utf-8", response.getContentType());
        
        String name = response.path("name");
        String city = response.path("address.city");
        
        Assertions.assertEquals("Leanne Graham", name);
        Assertions.assertEquals("Gwenborough", city);
        
        System.out.println("Response body: " + response.asPrettyString());
    }

    @Test
    @DisplayName("Извлечение данных с использованием queryParam")
    public void testExtractWithQueryParam() {
        // Получаем список постов для конкретного пользователя через query parameter
        int postsCount = given()
            .queryParam("userId", 1)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("", hasSize(10)) // Проверяем количество постов для userId=1
            .extract()
            .path("size()"); // Rest Assured JsonPath может извлечь размер коллекции

        System.out.println("Total posts for user 1: " + postsCount);
        Assertions.assertEquals(10, postsCount);
    }
}
