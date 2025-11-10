# API Тестирование сущностей

Автоматизированные интеграционные тесты для REST API управления сущностями.

## 🧪 Описание

Проект содержит **позитивные автотесты** для проверки основных операций с сущностями:
- Создание (`POST /api/create`)
- Чтение (`GET /api/get/{id}`)
- Обновление (`PATCH /api/patch/{id}`)
- Удаление (`DELETE /api/delete/{id}`)
- Получение всех (`POST /api/getAll`)

Тесты написаны на:
- **Java 11+**
- **TestNG** — запуск и управление тестами
- **RestAssured** — HTTP-взаимодействие с API
- **Allure** — генерация отчётов
- **Jackson** — сериализация/десериализация JSON

---

## 🛠 Архитектура проекта
# Архитектура проекта

src/
├── main/
│   └── java/
│       └── dto/        # Data Transfer Objects
│           ├── EntityRequest.java
│           ├── EntityResponse.java
│           ├── AdditionRequest.java
│           └── AdditionResponse.java
│   └── test/
│       ├── java/
│       │   ├── factory/    # Фабрика тестовых данных
│       │   │   └── TestDataFactory.java
│       │   │
│       │   ├── steps/     # Шаги для HTTP-запросов
│       │   │   └── EntitySteps.java
│       │   │
│       │   └── tests/     # Основные тесты
│       │       └── EntityApiTests.java
│       └── resources/
│           └── testng.xml # Конфигурация TestNG

---

## ✅ Выполненные дополнительные задачи


В ходе работы были выполнены **важные улучшения**:

### 1. **Полная документация через JavaDoc**
- Каждый класс и метод содержит подробный `@doc` с описанием логики, а не только назначения
- Использованы `@link` для связывания компонентов
- Включены теги `<p>`, `<ul>`, `<li>` для структурирования и читаемости
- Пример: `EntityApiTests.java` — объяснена **логика каждого шага теста**

### 2. **Чистота и профессиональный стиль кода**
- Убраны все отладочные `System.out.println()`
- Удалено логирование, не нужное после финализации тестов
- Код соответствует best practices: изолированность, чистка, уникальные данные
- Все тесты независимы и могут запускаться параллельно

### 3. **Улучшенные DTO с разделением вход/выход**
- `EntityRequest` и `EntityResponse` — разные структуры, как в реальном API
- `AdditionRequest` (text + number) ≠ `AdditionResponse` (только id) — отражает настоящий контракт
- Использование `@JsonProperty` — корректная сериализация

### 4. **Продуманная фабрика тестовых данных**
- `TestDataFactory` — централизованное создание данных
- Гарантирует уникальность `title` через `UUID`
- Упрощает поддержку и расширение

### 5. **Надёжные шаги (EntitySteps)**
- Инкапсуляция логики HTTP-запросов
- Обработка `text/plain` → `JSON` через `registerParser`
- Метод `createEntity()` возвращает **полный объект**, не только ID

### 6. **Готовность к CI/CD**
- Поддержка Allure
- HTML-отчёты TestNG
- Чистые и повторяемые тесты

---

## 🔧 Настройки

### Базовый URL и порт
java RestAssured.baseURI = "http://localhost"; RestAssured.port = 8080;

### Парсинг ответов
API возвращает ID как `text/plain`, поэтому добавлена настройка:
java RestAssured.registerParser("text/plain", Parser.JSON);

---

## ✅ Функциональность тестов

| Тест | Описание |
|------|--------|
| `testCreateEntity()` | Создаёт сущность, проверяет ID и все поля |
| `testGetEntityById()` | Получает сущность по ID, сверяет данные |
| `testGetAllEntities()` | Проверяет доступность списка всех сущностей |
| `testUpdateEntity()` | Обновляет поля сущности, проверяет изменения |
| `testDeleteEntity()` | Удаляет сущность, подтверждает отсутствие |

**Особенности:**
- Все тесты независимы
- Используют уникальные `title` через `TestDataFactory.generateUniqueTitle()`
- Выполняют чистку после себя (`deleteEntity`)
- Не зависят от порядка выполнения
- Могут запускаться параллельно

---

## 🧩 DTO Модели

### `EntityRequest`
| Поле | Тип | Описание |
|------|-----|--------|
| title | `String` | Заголовок (уникальный) |
| verified | `Boolean` | Флаг подтверждения |
| importantNumbers | `List<Integer>` | Список чисел |
| addition | `AdditionRequest` | Вложенная сущность (text + number) |

### `EntityResponse`
| Поле | Тип | Описание |
|------|-----|--------|
| id | `String` | ID, генерируемый сервером |
| title | `String` | Заголовок |
| verified | `Boolean` | Флаг подтверждения |
| importantNumbers | `List<Integer>` | Список чисел |
| addition | `AdditionResponse` | Только ID (сервер не возвращает text/number) |

---

## 🧰 Запуск тестов

### 1. Убедитесь, что API запущено на:
http://localhost:8080

### 2. Запустите тесты через Gradle:
bash ./gradlew test

### 3. Сгенерируйте отчёт Allure:
bash ./gradlew allureReport

### 4. Откройте отчёт:
bash ./gradlew allureServe

---

## 📎 Зависимости (основные)

- `org.testng:testng:7.8.0`
- `io.rest-assured:rest-assured:5.3.0`
- `com.fasterxml.jackson.core:jackson-databind:2.15.2`
- `io.qameta.allure:allure-testng:2.21.0`

---

## 📂 build.gradle (фрагмент)
gradle plugins { id 'java' id 'io.qameta.allure' version '2.12.0' }
repositories { mavenCentral() }
dependencies { testImplementation 'org.testng:testng:7.8.0' testImplementation 'io.rest-assured:rest-assured:5.3.0' testImplementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2' testImplementation 'io.qameta.allure:allure-testng:2.21.0' }
test { useTestNG() testLogging { events "PASSED", "FAILED", "SKIPPED" } }

---

## ✅ Требования к окружению

- Java 11 или выше
- Запущенный API-сервер на `http://localhost:8080`
- Доступ к порту 8080
- Internet (для загрузки зависимостей)

---

## 📄 Лицензия

MIT License — свободное использование и модификация.

---

 
> Все тесты покрывают позитивные сценарии, документированы, изолированы и чистят за собой.