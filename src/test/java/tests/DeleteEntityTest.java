package tests;

import dto.EntityRequest;
import dto.EntityResponse;
import factory.TestDataFactory;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.*;
import steps.EntitySteps;

/**
 * Тестовый класс для проверки функциональности удаления сущности через эндпоинт {@code DELETE /api/delete/{id}}.
 *
 * <p>Цель тестов:
 * <ul>
 *   <li>Проверить, что API корректно удаляет сущность по ID.</li>
 *   <li>Убедиться, что после удаления сущность недоступна (возвращает 404).</li>
 * </ul>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.1
 * @since 2025
 */
@Feature("DELETE /api/delete/{id}")
@Story("Удаление сущности")
@Test
public class DeleteEntityTest {

    private EntitySteps steps;

    @BeforeClass
    public void setUp() {
        steps = new EntitySteps();
    }

    /**
     * Позитивный тест: проверка удаления сущности.
     *
     * <p><b>Сценарий:</b>
     * <ol>
     *   <li>Создаётся сущность.</li>
     *   <li>Выполняется DELETE-запрос.</li>
     *   <li>Проверяется, что сущность больше не доступна.</li>
     * </ol>
     */
    @Test(description = "Позитивный: Удаление сущности")
    public void testDeleteEntity() {
        String uniqueTitle = TestDataFactory.generateUniqueTitle("Delete");
        EntityRequest request = TestDataFactory.createTestEntity(uniqueTitle);

        EntityResponse created = steps.createEntity(request);
        String localId = created.getId();

        steps.deleteEntity(localId);

        steps.assertEntityNotFound(localId);
    }
}