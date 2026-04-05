## REST Assured — библиотека для тестирования HTTP/REST API на Java.

Проект содержит примеры использования библиотеки **REST Assured** для автоматизации тестирования RESTful сервисов на примере API [JSONPlaceholder](https://jsonplaceholder.typicode.com/).

## Как запустить тесты
Для запуска всех тестов выполните команду:
```bash
./gradlew test
```
Отчеты о тестировании будут доступны в папке `build/reports/tests/test/index.html`.

## Структура проекта
Проект организован по пакетам в `src/test/java/ru/gulash/restassured/`:

- `SimpleTest.java` — базовые примеры GET-запросов, использование `pathParam`, `queryParam` и логирования.
- `WithSpecTest.java` — использование **Request/Response Specifications** для переиспользования конфигураций.
- `PojoTest.java` — примеры десериализации (преобразования) JSON-ответа в Java-объекты (POJO).
- `ExtractionTest.java` — способы извлечения данных из тела ответа (JsonPath, Response object).
- `models/` — пакет с POJO-классами (`User`, `Address`, `Company`, `Geo`), описывающими структуру данных API.

## Ключевые концепции

### 1. Request & Response Specifications
Для исключения дублирования настроек (Base URI, Headers) используются спецификации:
- **`RequestSpecification`**: общие параметры запроса (Base URI, Content-Type, Auth).
- **`ResponseSpecification`**: общие проверки ответа (Status Code, Content-Type).

**Пример (`WithSpecTest.java`)**:
```java
requestSpec = new RequestSpecBuilder()
    .setBaseUri("https://jsonplaceholder.typicode.com")
    .setContentType(ContentType.JSON)
    .build();

given().spec(requestSpec).when().get("/users/1").then().statusCode(200);
```

### 2. Работа с POJO (Jackson)
REST Assured автоматически преобразует JSON в Java-объекты и обратно, если в проекте есть Jackson/Gson.

**Пример (`PojoTest.java`)**:
```java
User user = given()
    .when().get("/users/1")
    .then().extract().as(User.class);

System.out.println(user.getName()); // Leanne Graham
```

### 3. Извлечение данных (Extraction)
Если нужно получить конкретное значение из ответа для использования в следующих тестах:

**Пример (`ExtractionTest.java`)**:
```java
String email = given()
    .when().get("/users/1")
    .then().extract().path("email");
```

## Best Practices (Лучшие практики)

1.  **Использование POJO**: Создавайте Java-классы для сущностей. Это делает тесты более читаемыми и устойчивыми к изменениям в JSON.
2.  **Спецификации (Specifications)**: Выносите общие настройки в `BeforeAll` или отдельные классы конфигурации.
3.  **Логирование при ошибках**: Используйте `.log().ifValidationFails()` в `BeforeAll` или в цепочке вызовов, чтобы видеть детали запроса/ответа только при падении теста.
4.  **Hamcrest Matchers**: Используйте матчеры (`equalTo`, `hasItem`, `hasSize`, `everyItem`) для декларативных проверок.
5.  **Разделение логики**: В больших проектах используйте паттерн **Steps/Service Object** для описания эндпоинтов отдельно от тестовых сценариев.

## Полезные ссылки
- [Официальная документация REST Assured](https://github.com/rest-assured/rest-assured/wiki/Usage)
- [Hamcrest Matchers Guide](http://hamcrest.org/JavaHamcrest/tutorial)
- [JSONPlaceholder API](https://jsonplaceholder.typicode.com/)