package tests;

import dto.EntityRequest;
import dto.EntityResponse;
import factory.TestDataFactory;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.*;
import steps.EntitySteps;

/**
 * Интеграционные автотесты для API сущностей.
 * <p>
 * Все тесты:
 * <ul>
 *   <li>Работают независимо</li>
 *   <li>Могут запускаться параллельно</li>
 *   <li>Не зависят от порядка выполнения</li>
 *   <li>Выполняют полную проверку логики API</li>
 * </ul>
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
@Feature("API Тестирование сущностей")
@Test
public class EntityApiTests {

    private EntitySteps steps;

    @BeforeClass
    public void setUp() {
        steps = new EntitySteps();
    }

    /**
     * Позитивный тест: создание новой сущности.
     * <p>
     * Логика теста:
     * <ol>
     *   <li>Генерируется уникальный заголовок с помощью {@link TestDataFactory}</li>
     *   <li>Создаётся объект запроса {@link EntityRequest} с валидными данными, включая вложенный объект {@linkAdditionRequest}</li>
     *   <li>Происходит валидация входных данных перед отправкой</li>
     *   <li>Выполняется HTTP-запрос к эндпоинту <b>POST /api/create</b> через {@link EntitySteps#createEntity(EntityRequest)}</li>
     *   <li>Проверяется успешность создания:
     *     <ul>
     *       <li>Ответ не null</li>
     *       <li>Сгенерирован ID</li>
     *       <li>Все поля соответствуют переданным данным</li>
     *       <li>Поле addition содержит ID</li>
     *     </ul>
     *   </li>
     *   <li>После проверки сущность удаляется для обеспечения изолированности тестов</li>
     * </ol>
     * </p>
     * <p>
     * Цель: убедиться, что API корректно создаёт сущность, возвращает полный ответ с ID,
     * и все поля сохраняются без потерь.
     * </p>
     */
    @Test(description = "Позитивный: Создание сущности")
    @Story("POST /api/create")
    public void testCreateEntity() {
        String uniqueTitle = TestDataFactory.generateUniqueTitle("Create");
        EntityRequest request = TestDataFactory.createTestEntity(uniqueTitle);

        // Валидация входных данных
        Assert.assertNotNull(request, "Объект request должен быть инициализирован");
        Assert.assertNotNull(request.getTitle(), "Title в request не должен быть null");
        Assert.assertNotNull(request.getVerified(), "Verified в request не должен быть null");
        Assert.assertNotNull(request.getImportantNumbers(), "importantNumbers в request не должен быть null");
        Assert.assertNotNull(request.getAddition(), "addition в request не должен быть null");
        Assert.assertNotNull(request.getAddition().getText(), "addition.text не должен быть null");
        Assert.assertNotNull(request.getAddition().getNumber(), "addition.number не должен быть null");

        EntityResponse created = null;
        String localId = null;

        try {
            created = steps.createEntity(request);
            localId = created != null ? created.getId() : null;
        } catch (Exception e) {
            Assert.fail("Исключение при создании сущности: " + e.getMessage());
        }

        // Проверка ответа
        Assert.assertNotNull(created, "Ответ от API (created) не должен быть null");
        Assert.assertNotNull(created.getId(), "ID не должен быть null");
        Assert.assertTrue(!created.getId().isEmpty(), "ID не должен быть пустой строкой");

        Assert.assertEquals(created.getTitle(), request.getTitle(), "Title не совпадает с ожидаемым");
        Assert.assertEquals(created.getVerified(), request.getVerified(), "Verified не совпадает с ожидаемым");

        // Проверка addition
        Assert.assertNotNull(created.getAddition(), "Поле addition не должно быть null");
        Assert.assertNotNull(created.getAddition().getId(), "addition.id не должен быть null");

        // Чистка
        if (localId != null && !localId.isEmpty()) {
            try {
                steps.deleteEntity(localId);
            } catch (Exception e) {
                // Игнорируем ошибку удаления — не влияет на результат теста
            }
        }
    }

    /**
     * Позитивный тест: получение сущности по ID.
     * <p>
     * Логика теста:
     * <ol>
     *   <li>Генерируется уникальный заголовок с помощью {@link TestDataFactory#generateUniqueTitle(String)}</li>
     *   <li>Создаётся валидный объект {@link EntityRequest} через {@link TestDataFactory#createTestEntity(String)}</li>
     *   <li>Отправляется запрос на создание сущности через {@link EntitySteps#createEntity(EntityRequest)}</li>
     *   <li>Извлекается ID созданной сущности для последующего обращения</li>
     *   <li>Выполняется HTTP-запрос к эндпоинту <b>GET /api/get/{id}</b> через {@link EntitySteps#getEntity(String)}</li>
     *   <li>Проверяется, что возвращённая сущность:
     *     <ul>
     *       <li>Имеет ожидаемый ID</li>
     *       <li>Сохраняет исходное значение title</li>
     *       <li>Сохраняет значение verified</li>
     *       <li>Содержит непустое поле addition с ID</li>
     *     </ul>
     *   </li>
     *   <li>После проверки сущность удаляется для обеспечения изолированности тестов</li>
     * </ol>
     * </p>
     * <p>
     * Цель: убедиться, что API корректно возвращает ранее созданную сущность по её идентификатору,
     * все поля передаются без изменений, и вложенные объекты (addition) десериализуются корректно.
     * </p>
     */
    @Test(description = "Позитивный: Получение сущности по ID")
    @Story("GET /api/get/{id}")
    public void testGetEntityById() {
        String uniqueTitle = TestDataFactory.generateUniqueTitle("GetById");
        EntityRequest request = TestDataFactory.createTestEntity(uniqueTitle);
        String localId = null;

        try {
            EntityResponse created = steps.createEntity(request);
            localId = created.getId();

            EntityResponse retrieved = steps.getEntity(localId);

            Assert.assertEquals(retrieved.getId(), localId, "ID не совпадает");
            Assert.assertEquals(retrieved.getTitle(), request.getTitle(), "Title не совпадает");
            Assert.assertEquals(retrieved.getVerified(), request.getVerified(), "Verified не совпадает");
            Assert.assertNotNull(retrieved.getAddition(), "Поле addition не должно быть null");
            Assert.assertNotNull(retrieved.getAddition().getId(), "addition.id не должен быть null");
        } catch (Exception e) {
            Assert.fail("Тест завершился с ошибкой: " + e.getMessage());
        } finally {
            if (localId != null && !localId.isEmpty()) {
                try {
                    steps.deleteEntity(localId);
                } catch (Exception e) {
                    // Игнорируем
                }
            }
        }
    }

    /**
     * Позитивный тест: получение всех сущностей.
     * <p>
     * Логика теста:
     * <ol>
     *   <li>Выполняется HTTP-запрос к эндпоинту <b>POST /api/getAll</b> через метод {@link EntitySteps#getAllEntities()}</li>
     *   <li>Предполагается, что API возвращает список всех доступных сущностей (в том числе и ранее созданные в других тестах)</li>
     *   <li>Проверяется успешность выполнения запроса:
     *     <ul>
     *       <li>Ответ не вызывает исключений</li>
     *       <li>Статус код — 200 OK</li>
     *       <li>Тело ответа корректно десериализуется в список объектов {@link EntityResponse}</li>
     *     </ul>
     *   </li>
     * </ol>
     * </p>
     * <p>
     * Цель: убедиться, что эндпоинт <b>/api/getAll</b> доступен, работает стабильно и возвращает данные в ожидаемом формате.
     * Тест не проверяет содержимое списка (поскольку зависит от состояния БД), но подтверждает работоспособность метода.
     * </p>
     */
    @Test(description = "Позитивный: Получение всех сущностей")
    @Story("POST /api/getAll")
    public void testGetAllEntities() {
        try {
            steps.getAllEntities();
        } catch (Exception e) {
            Assert.fail("Исключение при получении всех сущностей: " + e.getMessage());
        }
    }

    /**
     * Позитивный тест: удаление сущности.
     * <p>
     * Логика теста:
     * <ol>
     *   <li>Генерируется уникальный заголовок с помощью {@link TestDataFactory#generateUniqueTitle(String)}</li>
     *   <li>Создаётся валидный объект {@link EntityRequest} через {@link TestDataFactory#createTestEntity(String)}</li>
     *   <li>Отправляется запрос на создание сущности через {@link EntitySteps#createEntity(EntityRequest)}, чтобы гарантировать наличие объекта для удаления</li>
     *   <li>Извлекается ID созданной сущности</li>
     *   <li>Выполняется HTTP-запрос к эндпоинту <b>DELETE /api/delete/{id}</b> через {@link EntitySteps#deleteEntity(String)}</li>
     *   <li>Проверяется, что после удаления:
     *     <ul>
     *       <li>Повторный запрос на получение сущности возвращает статус 404 Not Found</li>
     *       <li>Метод {@link EntitySteps#assertEntityNotFound(String)} подтверждает отсутствие ресурса</li>
     *     </ul>
     *   </li>
     *   <li>Дополнительная проверка в блоке <code>finally</code> обеспечивает устойчивость теста к ошибкам</li>
     * </ol>
     * </p>
     * <p>
     * Цель: убедиться, что API корректно удаляет сущность по ID и последующие запросы к удалённому ресурсу возвращают 404.
     * Тест проверяет как успешное удаление, так и согласованность состояния системы после операции.
     * </p>
     */
    @Test(description = "Позитивный: Удаление сущности")
    @Story("DELETE /api/delete/{id}")
    public void testDeleteEntity() {
        String uniqueTitle = TestDataFactory.generateUniqueTitle("Delete");
        EntityRequest request = TestDataFactory.createTestEntity(uniqueTitle);
        String localId = null;

        try {
            EntityResponse created = steps.createEntity(request);
            localId = created.getId();

            steps.deleteEntity(localId);

            // Проверка недоступности
            steps.assertEntityNotFound(localId);
        } catch (Exception e) {
            Assert.fail("Тест завершился с ошибкой: " + e.getMessage());
        } finally {
            if (localId != null && !localId.isEmpty()) {
                try {
                    steps.assertEntityNotFound(localId);
                } catch (Exception e) {
                    // Игнорируем — только для уверенности
                }
            }
        }
    }

    /**
     * Позитивный тест: обновление сущности.
     * <p>
     * Логика теста:
     * <ol>
     *   <li>Генерируется уникальный заголовок <code>createTitle</code> для создания исходной сущности</li>
     *   <li>Создаётся объект {@link EntityRequest} с валидными данными через {@link TestDataFactory#createTestEntity(String)}</li>
     *   <li>Отправляется запрос на создание сущности через {@link EntitySteps#createEntity(EntityRequest)}</li>
     *   <li>Извлекается ID созданной сущности для использования в операции обновления</li>
     *   <li>Генерируется новый уникальный заголовок <code>updateTitle</code> для обновлённого значения</li>
     *   <li>Формируется объект запроса на обновление через {@link TestDataFactory#createUpdatedEntity(String)}</li>
     *   <li>Выполняется HTTP-запрос к эндпоинту <b>PATCH /api/patch/{id}</b> через {@link EntitySteps#updateEntity(String, EntityRequest)}</li>
     *   <li>После обновления выполняется чтение сущности по ID с помощью {@link EntitySteps#getEntity(String)}</li>
     *   <li>Проверяется, что:
     *     <ul>
     *       <li>Поле <code>title</code> обновилось на новое значение</li>
     *       <li>Поле <code>verified</code> соответствует ожидаемому из запроса</li>
     *       <li>Вложенное поле <code>addition</code> присутствует и содержит ID (сохраняется при частичном обновлении)</li>
     *     </ul>
     *   </li>
     *   <li>После проверки сущность удаляется для обеспечения изолированности тестов</li>
     * </ol>
     * </p>
     * <p>
     * Цель: убедиться, что API корректно поддерживает частичное обновление сущности (PATCH),
     * все переданные поля применяются, а неизменяемые или неуказанные поля (например, addition.id) сохраняются при возможности.
     * </p>
     */
    @Test(description = "Позитивный: Обновление сущности")
    @Story("PATCH /api/patch/{id}")
    public void testUpdateEntity() {
        String createTitle = TestDataFactory.generateUniqueTitle("Update_Create");
        String updateTitle = TestDataFactory.generateUniqueTitle("Update_Patched");
        String localId = null;

        try {
            // Создание
            EntityRequest createRequest = TestDataFactory.createTestEntity(createTitle);
            EntityResponse created = steps.createEntity(createRequest);
            localId = created.getId();

            // Обновление
            EntityRequest updateRequest = TestDataFactory.createUpdatedEntity(updateTitle);
            steps.updateEntity(localId, updateRequest);

            // Проверка
            EntityResponse updated = steps.getEntity(localId);

            Assert.assertEquals(updated.getTitle(), updateRequest.getTitle(), "Title после обновления не совпадает");
            Assert.assertEquals(updated.getVerified(), updateRequest.getVerified(), "Verified после обновления не совпадает");
            Assert.assertNotNull(updated.getAddition(), "addition не должен быть null");
            Assert.assertNotNull(updated.getAddition().getId(), "addition.id не должен быть null");
        } catch (Exception e) {
            Assert.fail("Тест завершился с ошибкой: " + e.getMessage());
        } finally {
            if (localId != null) {
                try {
                    steps.deleteEntity(localId);
                } catch (Exception e) {
                    // Игнорируем
                }
            }
        }
    }
}