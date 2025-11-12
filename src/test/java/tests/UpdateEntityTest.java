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
 * Тестовый класс для проверки функциональности обновления сущности через эндпоинт {@code PATCH /api/patch/{id}}.
 *
 * <p>Цель тестов:
 * <ul>
 *   <li>Проверить, что API корректно обновляет поля существующей сущности.</li>
 *   <li>Убедиться, что после обновления все поля соответствуют новым значениям.</li>
 * </ul>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.1
 * @since 2025
 */
@Feature("PATCH /api/patch/{id}")
@Story("Обновление сущности")
@Test
public class UpdateEntityTest {

    private EntitySteps steps;

    @BeforeClass
    public void setUp() {
        steps = new EntitySteps();
    }

    /**
     * Позитивный тест: проверка обновления сущности.
     *
     * <p><b>Сценарий:</b>
     * <ol>
     *   <li>Создаётся сущность с начальными данными.</li>
     *   <li>Выполняется PATCH-запрос с обновлёнными данными.</li>
     *   <li>Получается обновлённая сущность и проверяются все поля.</li>
     * </ol>
     */
    @Test(description = "Позитивный: Обновление сущности")
    public void testUpdateEntity() {
        String createTitle = TestDataFactory.generateUniqueTitle("Update_Create");
        String updateTitle = TestDataFactory.generateUniqueTitle("Update_Patched");

        EntityRequest createRequest = TestDataFactory.createTestEntity(createTitle);
        EntityResponse created = steps.createEntity(createRequest);
        String localId = created.getId();

        EntityRequest updateRequest = TestDataFactory.createUpdatedEntity(updateTitle);
        steps.updateEntity(localId, updateRequest);

        EntityResponse updated = steps.getEntity(localId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(updated.getTitle(), updateRequest.getTitle(), "Title после обновления не совпадает");
        softAssert.assertEquals(updated.getVerified(), updateRequest.getVerified(), "Verified после обновления не совпадает");

        softAssert.assertNotNull(updated.getAddition(), "addition не должен быть null");
        softAssert.assertNotNull(updated.getAddition().getId(), "addition.id не должен быть null");

        softAssert.assertAll();

        steps.deleteEntity(localId);
    }
}
