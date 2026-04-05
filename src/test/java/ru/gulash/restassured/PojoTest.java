package ru.gulash.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gulash.restassured.models.User;

import static io.restassured.RestAssured.given;

public class PojoTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Десериализация ответа в POJO")
    public void testUserPojo() {
        User user = given()
            .pathParam("id", 1)
        .when()
            .get("/users/{id}")
        .then()
            .statusCode(200)
            .extract()
            .as(User.class);

        Assertions.assertEquals("Leanne Graham", user.getName());
        Assertions.assertEquals("Bret", user.getUsername());
        Assertions.assertEquals("Sincere@april.biz", user.getEmail());
        Assertions.assertNotNull(user.getAddress());
        Assertions.assertEquals("Kulas Light", user.getAddress().getStreet());
        Assertions.assertNotNull(user.getCompany());
        Assertions.assertEquals("Romaguera-Crona", user.getCompany().getName());
    }
}
