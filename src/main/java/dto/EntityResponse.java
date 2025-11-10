package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data Transfer Object для ответа API с информацией о сущности.
 * <p>
 * Используется в:
 * <ul>
 *   <li>{@code GET /api/get/{id}}</li>
 *   <li>{@code POST /api/getAll}</li>
 * </ul>
 * </p>
 * <p>
 * Содержит все поля, возвращаемые сервером, включая идентификатор и вложенную сущность.
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
public class EntityResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("verified")
    private Boolean verified;

    @JsonProperty("importantNumbers")
    private List<Integer> importantNumbers;

    @JsonProperty("addition")
    private AdditionResponse addition;

    /**
     * Возвращает идентификатор сущности.
     *
     * @return целое число — ID
     */
    public String getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор сущности.
     *
     * @param id целое число
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Возвращает заголовок сущности.
     *
     * @return строковое значение заголовка
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок сущности.
     *
     * @param title строковое значение заголовка
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает флаг подтверждения.
     *
     * @return {@code true} если подтверждено, {@code false} иначе
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * Устанавливает флаг подтверждения.
     *
     * @param verified булево значение
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * Возвращает список важных чисел.
     *
     * @return список целых чисел
     */
    public List<Integer> getImportantNumbers() {
        return importantNumbers;
    }

    /**
     * Устанавливает список важных чисел.
     *
     * @param importantNumbers список целых чисел
     */
    public void setImportantNumbers(List<Integer> importantNumbers) {
        this.importantNumbers = importantNumbers;
    }

    /**
     * Возвращает вложенную сущность "дополнение".
     *
     * @return объект {@link AdditionResponse}
     */
    public AdditionResponse getAddition() {
        return addition;
    }

    /**
     * Устанавливает дополнение.
     *
     * @param addition объект {@link AdditionResponse}
     */
    public void setAddition(AdditionResponse addition) {
        this.addition = addition;
    }
}