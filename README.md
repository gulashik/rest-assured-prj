## REST Assured — библиотека для тестирования HTTP/REST API на Java.

### Стек технологий
- **Java 11+**
- **REST Assured** — DSL для тестирования REST сервисов.
- **JUnit 5** — фреймворк для написания тестов.
- **Jackson** — библиотека для сериализации/десериализации JSON.
- **Gradle** — система сборки.

## Как запустить тесты
   ```bash
   ./gradlew test
   ```

## Структура проекта
- `SampleTest.java` — пример теста с использованием **Request/Response Specifications**.
- `build.gradle` — конфигурация зависимостей.

## Ключевые концепции

### Request & Response Specifications
Для того чтобы не повторять настройки (Base URI, Headers) в каждом тесте, мы используем:
- **`RequestSpecBuilder`**: Позволяет один раз описать все общие параметры запроса (Base URI, заголовки, авторизацию, параметры запроса).
- **`ResponseSpecBuilder`**: Позволяет собрать повторяющиеся проверки (Status Code, Content-Type).

**Пример из `SampleTest.java`**:
```java
// Настройка спецификаций
requestSpec = new RequestSpecBuilder().setBaseUri("...").build();
responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();

// Использование в тесте
given().spec(requestSpec).when().get("/users/1").then().spec(responseSpec);
```

## Best Practices (Лучшие практики)

1.  **Использование POJO**: Вместо передачи JSON строкой или через `Map`, создавайте Java-классы (Plain Old Java Objects). REST Assured автоматически превратит их в JSON с помощью Jackson.
2.  **Request/Response Specifications**: Выносите повторяющиеся настройки (Base URI, Headers, Auth) в `RequestSpecBuilder`, а общие проверки (StatusCode, Content-Type) в `ResponseSpecBuilder`.
3.  **Логирование при ошибках**: Используйте `.log().ifValidationFails()`, чтобы консоль не засорялась лишней информацией при успешных тестах, но давала полные данные при падениях.
4.  **Разделение логики**: Используйте паттерн **Page Object** (или **Steps/Service Object**) для описания эндпоинтов, отделяя логику запросов от логики самих тестов.
5.  **Hamcrest Matchers**: Активно используйте матчеры (`equalTo`, `hasItem`, `notNullValue`) для гибких проверок тела ответа.

## Подводные камни

1.  **Конфликты версий**: REST Assured тянет свои версии Groovy и Jackson. Если в проекте используются другие версии этих библиотек, могут возникнуть `NoSuchMethodError`. Решается через `exclude` в блоке dependencies или через `Platform/EnforcedPlatform`.
2.  **GPath vs JSON Path**: Синтаксис `body("field.subfield", ...)` использует GPath. Будьте внимательны при работе со списками — GPath позволяет делать мощные выборки (например, `find { it.id == 1 }.name`), но синтаксис отличается от стандартного JSONPath.
3.  **Кодировка**: Иногда REST Assured может некорректно определять кодировку ответа. Если в теле ответа "кракозябры", попробуйте явно указать `.config(RestAssuredConfig.config().encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))`.