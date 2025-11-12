package factory;

import dto.AdditionRequest;
import dto.EntityRequest;

import java.util.Arrays;
import java.util.UUID;

/**
 * Фабрика для создания тестовых данных.
 * <p>
 * Централизованное место формирования объектов {@link EntityRequest} с валидными,
 * но уникальными полями, что необходимо для:
 * <ul>
 *   <li>Предотвращения конфликтов при создании сущностей (уникальный {@code title})</li>
 *   <li>Обеспечения независимости тестов</li>
 *   <li>Упрощения поддержки и модификации тестовых данных</li>
 * </ul>
 * </p>
 * <p>
 * Используется в тестах для:
 * <ul>
 *   <li>Создания новых сущностей через {@link #createTestEntity(String)}</li>
 *   <li>Формирования данных для обновления через {@link #createUpdatedEntity(String)}</li>
 *   <li>Генерации уникальных идентифицируемых заголовков через {@link #generateUniqueTitle(String)}</li>
 * </ul>
 * </p>
 * <p>
 * Преимущества использования фабрики:
 * <ul>
 *   <li>Изоляция логики генерации данных от тестов</li>
 *   <li>Единый источник правды для структуры тестовых объектов</li>
 *   <li>Лёгкое изменение формата данных — в одном месте</li>
 * </ul>
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
public class TestDataFactory {

    /**
     * Создаёт валидный объект запроса для создания новой сущности.
     * <p>
     * Логика:
     * <ul>
     *   <li>Принимает уникальный заголовок, сгенерированный через {@link #generateUniqueTitle(String)}</li>
     *   <li>Формирует вложенный объект {@link AdditionRequest} с фиксированными значениями</li>
     *   <li>Инициализирует все обязательные поля в соответствии с контрактом API</li>
     * </ul>
     * </p>
     * <p>
     * Пример использования:
     * <pre>
     * String title = generateUniqueTitle("Create");
     * EntityRequest request = createTestEntity(title);
     * </pre>
     * </p>
     * <p>
     * Цель: обеспечить тест единым, предсказуемым, но уникальным набором данных.
     * </p>
     *
     * @param title уникальный заголовок сущности, должен быть не null
     * @return готовый объект {@link EntityRequest} для отправки в API
     */
    public static EntityRequest createTestEntity(String title) {
        AdditionRequest addition = new AdditionRequest();
        addition.setText("Text_" + title);
        addition.setNumber(100);

        EntityRequest request = new EntityRequest();
        request.setTitle(title);
        request.setVerified(true);
        request.setImportantNumbers(Arrays.asList(1, 2, 3));
        request.setAddition(addition);

        return request;
    }

    /**
     * Создаёт объект запроса с обновлёнными данными для частичного обновления (PATCH).
     * <p>
     * Логика:
     * <ul>
     *   <li>Используется при тестировании эндпоинта {@code PATCH /api/patch/{id}}</li>
     *   <li>Формирует отличающиеся от начальных значения полей</li>
     *   <li>Позволяет проверить, что API корректно применяет изменения</li>
     * </ul>
     * </p>
     * <p>
     * Отличия от {@link #createTestEntity(String)}:
     * <ul>
     *   <li>Заголовок текста: "Updated_Text_" + title</li>
     *   <li>Число: 999 (вместо 100)</li>
     *   <li>verified: false (вместо true)</li>
     *   <li>importantNumbers: [7,8,9] (вместо [1,2,3])</li>
     * </ul>
     * </p>
     *
     * @param title уникальный заголовок обновлённой сущности, должен быть не null
     * @return объект {@link EntityRequest} с обновлёнными полями
     */
    public static EntityRequest createUpdatedEntity(String title) {
        AdditionRequest addition = new AdditionRequest();
        addition.setText("Updated_Text_" + title);
        addition.setNumber(999);

        EntityRequest request = new EntityRequest();
        request.setTitle(title);
        request.setVerified(false);
        request.setImportantNumbers(Arrays.asList(7, 8, 9));
        request.setAddition(addition);

        return request;
    }

    /**
     * Генерирует уникальный заголовок для сущности.
     * <p>
     * Логика:
     * <ul>
     *   <li>Принимает префикс (например, "Create", "GetById")</li>
     *   <li>Добавляет к нему случайный UUID (первых 8 символов)</li>
     *   <li>Формирует строку вида: "Create_7f3a4b1e"</li>
     * </ul>
     * </p>
     * <p>
     * Цель: гарантировать уникальность поля {@code title}, чтобы:
     * <ul>
     *   <li>Избежать конфликтов при создании</li>
     *   <li>Обеспечить повторяемость и независимость тестов</li>
     * </ul>
     * </p>
     *
     * @param prefix префикс, описывающий назначение сущности
     * @return уникальная строка в формате "prefix_randomSuffix"
     */
    public static String generateUniqueTitle(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}