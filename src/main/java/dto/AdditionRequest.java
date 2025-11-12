package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO (Data Transfer Object) для вложенного поля "addition" в запросах создания и обновления сущности.
 * <p>
 * Используется в составе {@link EntityRequest} при отправке POST-запросов на <b>/api/create</b>
 * и PATCH-запросов на <b>/api/patch/{id}</b>.
 * </p>
 * <p>
 * Структура соответствует контракту API:
 * <ul>
 *   <li><b>text</b> — строковое поле, может быть пустым или содержать текст</li>
 *   <li><b>number</b> — целочисленное поле, допускает null (опциональное)</li>
 * </ul>
 * </p>
 * <p>
 * Аннотации {@link com.fasterxml.jackson.annotation.JsonProperty} обеспечивают корректную
 * сериализацию и десериализацию полей при обмене данными с API в формате JSON.
 * </p>
 * <p>
 * Цель класса — типизировать и инкапсулировать данные для вложенного объекта,
 * обеспечивая безопасность типов и удобство использования в тестах.
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
public class AdditionRequest {
    @JsonProperty("text")
    private String text;

    @JsonProperty("number")
    private Integer number;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
}