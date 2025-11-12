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
 * Тестовый класс для проверки функциональности создания сущности через эндпоинт {@code POST /api/create}.
 *
 * <p>Цель тестов:
 * <ul>
 *   <li>Проверить, что API корректно создаёт новую сущность на основе переданных данных.</li>
 *   <li>Убедиться, что возвращаемый объект содержит ожидаемые поля и соответствует отправленным данным.</li>
 *   <li>Проверить, что у созданной сущности генерируется уникальный идентификатор.</li>
 *   <li>Обеспечить чистку данных после выполнения теста (удаление созданной сущности).</li>
 * </ul>
 *
 * <p>Используемые компоненты:
 * <ul>
 *   <li>{@link EntityRequest} — DTO с входными данными для создания сущности.</li>
 *   <li>{@link EntityResponse} — DTO с ответом от API после создания/получения сущности.</li>
 *   <li>{@link TestDataFactory} — фабрика для генерации тестовых данных с уникальными значениями.</li>
 *   <li>{@link EntitySteps} — шаги для выполнения HTTP-запросов к API с аннотациями Allure для отчётов.</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Используются {@link SoftAssert} для проверки всех условий, даже если одно из них падает.</li>
 *   <li>Перед каждым тестом инициализируется экземпляр {@link EntitySteps}.</li>
 *   <li>После успешного создания сущности она удаляется для поддержания чистоты среды.</li>
 * </ul>
 *
 * <p>Аннотации:
 * <ul>
 *   <li>{@link Feature} — указывает, что тест относится к эндпоинту {@code POST /api/create}.</li>
 *   <li>{@link Story} — краткое описание сценария тестирования.</li>
 *   <li>{@link Test} — все методы в классе являются тестами TestNG.</li>
 * </ul>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.1
 * @since 2025
 */
@Feature("POST /api/create")
@Story("Создание новой сущности")
@Test
public class CreateEntityTest {

    private EntitySteps steps;

    /**
     * Инициализация шагов перед выполнением тестов.
     * <p>Выполняется один раз перед всеми тестами в этом классе.</p>
     */
    @BeforeClass
    public void setUp() {
        steps = new EntitySteps();
    }

    /**
     * Позитивный тест: проверка успешного создания сущности.
     *
     * <p><b>Сценарий:</b>
     * <ol>
     *   <li>Генерируется уникальное имя для сущности с помощью {@link TestDataFactory#generateUniqueTitle(String)}.</li>
     *   <li>Создаётся объект {@link EntityRequest} с тестовыми данными через {@link TestDataFactory#createTestEntity(String)}.</li>
     *   <li>Отправляется запрос на создание сущности через {@link EntitySteps#createEntity(EntityRequest)}.</li>
     *   <li>Проверяется, что:
     *     <ul>
     *       <li>API вернул не-null ответ.</li>
     *       <li>ID не null и не пустой.</li>
     *       <li>Title и Verified совпадают с отправленными.</li>
     *       <li>Поле addition присутствует и содержит идентификатор.</li>
     *     </ul>
     *   </li>
     *   <li>После проверок сущность удаляется через {@link EntitySteps#deleteEntity(String)}.</li>
     * </ol>
     *
     * <p><b>Ожидаемый результат:</b>
     * API возвращает 200 OK, объект с заполненными полями и сгенерированным ID.
     *
     * <p><b>Используется:</b>
     * <ul>
     *   <li>SoftAssert — чтобы все проверки выполнились, даже если одна упадёт.</li>
     *   <li>Allure @Step — для детализации шагов в отчёте.</li>
     * </ul>
     */
    @Test(description = "Позитивный: Создание сущности")
    public void testCreateEntity() {
        // Генерация уникального заголовка для избежания конфликтов
        String uniqueTitle = TestDataFactory.generateUniqueTitle("Create");

        // Создание тестового объекта с данными
        EntityRequest request = TestDataFactory.createTestEntity(uniqueTitle);

        // Выполнение шага: создание сущности → получение полного объекта
        EntityResponse created = steps.createEntity(request);
        String localId = created.getId(); // Извлечение ID для последующих операций

        // Группировка всех проверок в один блок
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(created, "Ответ от API (created) не должен быть null");
        softAssert.assertNotNull(localId, "ID не должен быть null");
        softAssert.assertTrue(!localId.isEmpty(), "ID не должен быть пустой строкой");

        softAssert.assertEquals(created.getTitle(), request.getTitle(), "Title не совпадает с ожидаемым");
        softAssert.assertEquals(created.getVerified(), request.getVerified(), "Verified не совпадает с ожидаемым");

        softAssert.assertNotNull(created.getAddition(), "Поле addition не должно быть null");
        softAssert.assertNotNull(created.getAddition().getId(), "addition.id не должен быть null");

        // Выполнение всех ассертов сразу
        softAssert.assertAll();

        // Очистка: удаление созданной сущности
        steps.deleteEntity(localId);
    }
}
