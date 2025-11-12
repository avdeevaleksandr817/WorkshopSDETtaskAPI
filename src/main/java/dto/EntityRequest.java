package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data Transfer Object для создания и обновления сущности.
 * <p>
 * Логика использования:
 * <ul>
 *   <li>При создании новой сущности используется в {@code POST /api/create} — все поля могут быть заданы клиентом</li>
 *   <li>При частичном обновлении — в {@code PATCH /api/patch/{id}} — передаются только изменяемые поля</li>
 * </ul>
 * </p>
 * <p>
 * Структура отражает контракт API:
 * <ul>
 *   <li><b>title</b> — обязательное строковое поле, идентифицирующее сущность</li>
 *   <li><b>verified</b> — обязательный булев флаг, указывает на статус проверки</li>
 *   <li><b>importantNumbers</b> — опциональный список целых чисел, может быть null или пустым</li>
 *   <li><b>addition</b> — вложенный объект типа {@link AdditionRequest}, содержащий текст и число;
 *       при создании — инициализируется полностью, при обновлении — заменяется целиком (если передан)</li>
 * </ul>
 * </p>
 * <p>
 * Особенности:
 * <ul>
 *   <li>DTO предназначен для отправки данных в API, поэтому содержит только сеттеры и геттеры</li>
 *   <li>Аннотации {@link com.fasterxml.jackson.annotation.JsonProperty} обеспечивают корректное
 *       сопоставление полей Java-объекта с JSON-ключами при сериализации запроса</li>
 *   <li>Наличие пустого конструктора обязательно для работы Jackson при десериализации</li>
 * </ul>
 * </p>
 * <p>
 * Цель класса — инкапсулировать входные данные, обеспечив типобезопасность и читаемость кода тестов.
 * Разделение на {@link EntityRequest} (вход) и {@link EntityResponse} (выход) позволяет точно следовать
 * спецификации API, где запрос и ответ могут отличаться по структуре.
 * </p>
 *
 * @author SDET Avdeev Aleksandr
 * @version 1.0
 * @since 2025
 */
public class EntityRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("verified")
    private Boolean verified;

    @JsonProperty("importantNumbers")
    private List<Integer> importantNumbers;

    @JsonProperty("addition")
    private AdditionRequest addition;

    /**
     * Конструктор по умолчанию. Требуется для десериализации JSON через Jackson.
     */
    public EntityRequest() {}

    /**
     * Конструктор с обязательными полями.
     *
     * @param title    заголовок сущности (обязательное поле)
     * @param verified флаг подтверждения (обязательное поле)
     */
    public EntityRequest(String title, Boolean verified) {
        this.title = title;
        this.verified = verified;
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
     * @return список целых чисел, может быть null
     */
    public List<Integer> getImportantNumbers() {
        return importantNumbers;
    }

    /**
     * Устанавливает список важных чисел.
     *
     * @param importantNumbers список целых чисел, может быть null
     */
    public void setImportantNumbers(List<Integer> importantNumbers) {
        this.importantNumbers = importantNumbers;
    }

    /**
     * Возвращает вложенную сущность "дополнение".
     *
     * @return объект {@link AdditionRequest}, может быть null
     */
    public AdditionRequest getAddition() {
        return addition;
    }

    /**
     * Устанавливает дополнение.
     *
     * @param addition объект {@link AdditionRequest}, если null — поле не будет включено в JSON
     */
    public void setAddition(AdditionRequest addition) {
        this.addition = addition;
    }
}