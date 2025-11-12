package tests;

import dto.EntityRequest;
import dto.EntityResponse;
import factory.TestDataFactory;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.*;
import steps.EntitySteps;

/**
 * Тестовый класс для проверки функциональности получения сущности по ID через эндпоинт {@code GET /api/get/{id}}.
 *
 * <p>Цель тестов:
 * <ul>
 *   <li>Проверить, что API возвращает корректную сущность по её идентификатору.</li>
 *   <li>Убедиться, что возвращаемые данные совпадают с ранее созданными.</li>
 *   <li>Проверить, что поле addition также возвращается с корректным ID.</li>
 * </ul>
 *
 * <p>После проверки сущность удаляется.
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.1
 * @since 2025
 */
@Feature("GET /api/get/{id}")
@Story("Получение сущности по ID")
@Test
public class GetEntityByIdTest {

    private EntitySteps steps;

    @BeforeClass
    public void setUp() {
        steps = new EntitySteps();
    }

    /**
     * Позитивный тест: проверка получения сущности по ID.
     *
     * <p><b>Сценарий:</b>
     * <ol>
     *   <li>Создаётся новая сущность через {@code POST /api/create}.</li>
     *   <li>Выполняется запрос на получение сущности по её ID.</li>
     *   <li>Проверяются все поля ответа: ID, Title, Verified, Addition.</li>
     *   <li>Выполняется очистка — удаление сущности.</li>
     * </ol>
     */
    @Test(description = "Позитивный: Получение сущности по ID")
    public void testGetEntityById() {
        String uniqueTitle = TestDataFactory.generateUniqueTitle("GetById");
        EntityRequest request = TestDataFactory.createTestEntity(uniqueTitle);

        EntityResponse created = steps.createEntity(request);
        String localId = created.getId();

        EntityResponse retrieved = steps.getEntity(localId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(retrieved.getId(), localId, "ID не совпадает");
        softAssert.assertEquals(retrieved.getTitle(), request.getTitle(), "Title не совпадает");
        softAssert.assertEquals(retrieved.getVerified(), request.getVerified(), "Verified не совпадает");

        softAssert.assertNotNull(retrieved.getAddition(), "Поле addition не должно быть null");
        softAssert.assertNotNull(retrieved.getAddition().getId(), "addition.id не должен быть null");

        softAssert.assertAll();

        steps.deleteEntity(localId);
    }
}
