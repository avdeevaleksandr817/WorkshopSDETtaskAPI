package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO (Data Transfer Object) для поля "addition" в ответах API.
 * <p>
 * Используется в составе {@link EntityResponse} при получении данных от сервера через эндпоинты:
 * <ul>
 *   <li><b>POST /api/create</b></li>
 *   <li><b>GET /api/get/{id}</b></li>
 *   <li><b>PATCH /api/patch/{id}</b></li>
 * </ul>
 * </p>
 * <p>
 * Особенность контракта API: при создании или обновлении сущности клиент отправляет объект <b>addition</b>
 * с полями <code>text</code> и <code>number</code>, но в ответе сервер возвращает только идентификатор
 * ранее созданного объекта addition:
 * <ul>
 *   <li><b>id</b> — целочисленный идентификатор вложенной сущности (не null после создания)</li>
 * </ul>
 * </p>
 * <p>
 * Таким образом, DTO разделяет:
 * <ul>
 *   <li>входную модель — {@link AdditionRequest} (содержит данные для записи)</li>
 *   <li>выходную модель — {@link AdditionResponse} (содержит только ID созданного объекта)</li>
 * </ul>
 * Это соответствует принципам REST и безопасности: клиент не получает обратно чувствительные или ненужные данные.
 * </p>
 * <p>
 * Аннотация {@link com.fasterxml.jackson.annotation.JsonProperty} обеспечивает корректное
 * сопоставление JSON-поля "id" с полем Java-объекта при десериализации ответа.
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
public class AdditionResponse {
    @JsonProperty("id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdditionResponse{" +
                "id=" + id +
                '}';
    }
}