package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.*;
import steps.EntitySteps;

/**
 * Тестовый класс для проверки получения всех сущностей через эндпоинт {@code POST /api/getAll}.
 *
 * <p>Цель тестов:
 * <ul>
 *   <li>Проверить, что API возвращает список всех созданных сущностей.</li>
 *   <li>Убедиться, что эндпоинт доступен и возвращает 200 OK.</li>
 * </ul>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.1
 * @since 2025
 */
@Feature("POST /api/getAll")
@Story("Получение всех сущностей")
@Test
public class GetAllEntitiesTest {

    private EntitySteps steps;

    @BeforeClass
    public void setUp() {
        steps = new EntitySteps();
    }

    /**
     * Позитивный тест: проверка получения всех сущностей.
     *
     * <p><b>Сценарий:</b>
     * <ol>
     *   <li>Выполняется запрос к {@code /api/getAll}.</li>
     *   <li>Проверяется, что ответ приходит с кодом 200.</li>
     * </ol>
     */
    @Test(description = "Позитивный: Получение всех сущностей")
    public void testGetAllEntities() {
        steps.getAllEntities();
    }
}
