package steps;

import dto.EntityRequest;
import dto.EntityResponse;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import static org.hamcrest.Matchers.*;

/**
 * Шаги (Steps/Helpers) для выполнения HTTP-запросов к API сущностей.
 * <p>
 * Инкапсулирует логику взаимодействия с API, обеспечивая:
 * <ul>
 *   <li>Высокий уровень абстракции в тестах</li>
 *   <li>Повторное использование кода</li>
 *   <li>Централизованное управление запросами и ожиданиями</li>
 * </ul>
 * </p>
 * <p>
 * Все методы используют {@link io.restassured.RestAssured} для выполнения HTTP-запросов
 * и валидации статус-кодов. Ответы автоматически десериализуются в DTO.
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
public class EntitySteps {

    static {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.registerParser("text/plain", Parser.JSON);
    }

    /**
     * Создаёт новую сущность через POST-запрос.
     * <p>
     * Логика:
     * <ul>
     *   <li>Отправляется POST-запрос на {@code /api/create} с телом {@link EntityRequest}</li>
     *   <li>Ожидается статус 200 OK и строковый ответ — ID новой сущности</li>
     *   <li>Так как API возвращает только ID, сразу выполняется GET-запрос для получения полного объекта</li>
     * </ul>
     * </p>
     * <p>
     * Цель: скрыть двухэтапную логику получения полного объекта от тестов.
     * Тест получает готовый {@link EntityResponse} с заполненными полями.
     * </p>
     *
     * @param request объект с данными для создания сущности
     * @return полный объект {@link EntityResponse} из GET-запроса
     */
    public EntityResponse createEntity(EntityRequest request) {
        // Сначала создаём — получаем id
        String id = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/api/create")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        // Затем получаем полный объект
        return getEntity(id);
    }

    /**
     * Получает сущность по её идентификатору.
     * <p>
     * Выполняет GET-запрос к {@code /api/get/{id}} и ожидает:
     * <ul>
     *   <li>Статус 200 OK</li>
     *   <li>Тело ответа в формате JSON, соответствующем {@link EntityResponse}</li>
     * </ul>
     * </p>
     * <p>
     * Используется как самостоятельный шаг и как часть {@link #createEntity(EntityRequest)}.
     * </p>
     *
     * @param id идентификатор сущности (строка)
     * @return объект {@link EntityResponse} с заполненными полями
     */
    public EntityResponse getEntity(String id) {
        return RestAssured.given()
                .get("/api/get/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(EntityResponse.class);
    }

    /**
     * Получает список всех сущностей.
     * <p>
     * Выполняет POST-запрос к {@code /api/getAll}.
     * <ul>
     *   <li>Тело запроса может быть пустым или содержать параметры фильтрации (не используется в тестах)</li>
     *   <li>Ожидается статус 200 OK</li>
     * </ul>
     * </p>
     * <p>
     * Цель: проверить доступность эндпоинта и его работоспособность.
     * Ответ не извлекается — достаточно подтверждения успешного статуса.
     * </p>
     */
    public void getAllEntities() {
        RestAssured.given()
                .contentType("application/json")
                .post("/api/getAll")
                .then()
                .statusCode(200);
    }

    /**
     * Обновляет существующую сущность по ID.
     * <p>
     * Выполняет PATCH-запрос к {@code /api/patch/{id}} с телом {@link EntityRequest}.
     * <ul>
     *   <li>Передаётся обновлённая версия объекта</li>
     *   <li>Ожидается статус 204 No Content (успешно, без тела)</li>
     * </ul>
     * </p>
     * <p>
     * Цель: изменить поля указанной сущности в соответствии с переданными данными.
     * Метод не возвращает тело — подтверждение через статус.
     * </p>
     *
     * @param id      идентификатор сущности для обновления
     * @param request объект с новыми значениями полей
     */
    public void updateEntity(String id, EntityRequest request) {
        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .patch("/api/patch/" + id)
                .then()
                .statusCode(204);
    }

    /**
     * Удаляет сущность по ID.
     * <p>
     * Выполняет DELETE-запрос к {@code /api/delete/{id}}.
     * <ul>
     *   <li>Ожидается статус 204 No Content (успешное удаление)</li>
     *   <li>Допускается 404 Not Found — если сущность уже удалена (например, из-за параллельного теста)</li>
     * </ul>
     * </p>
     * <p>
     * Цель: безопасно удалить сущность без падения теста, если она отсутствует.
     * Используется в блоке {@code finally} для чистки.
     * </p>
     *
     * @param id идентификатор сущности
     */
    public void deleteEntity(String id) {
        RestAssured.given()
                .when()
                .delete("/api/delete/" + id)
                .then()
                .statusCode(anyOf(
                        equalTo(204),
                        equalTo(404)
                ));
    }

    /**
     * Проверяет, что сущность с указанным ID не существует.
     * <p>
     * Выполняет GET-запрос к {@code /api/get/{id}}.
     * <ul>
     *   <li>Ожидается статус 404 Not Found</li>
     *   <li>Допускается 500 Internal Server Error (в некоторых сценариях API так реагирует на несуществующий ID)</li>
     * </ul>
     * </p>
     * <p>
     * Цель: подтвердить, что сущность действительно удалена или не была создана.
     * Используется после удаления и в финальной проверке.
     * </p>
     *
     * @param id идентификатор сущности
     */
    public void assertEntityNotFound(String id) {
        RestAssured.given()
                .when()
                .get("/api/get/" + id)
                .then()
                .statusCode(anyOf(
                        equalTo(404),
                        equalTo(500)
                ));
    }
}