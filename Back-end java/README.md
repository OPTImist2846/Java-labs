# FPV Fleet

Back-end розробка на Java. Система обліку FPV-пілотів, їхніх дронів,
обладнання та журналів польотів.

## Запуск

Потрібні MySQL на `localhost:3306` (`root` / `root`) і Docker.

```
docker compose up -d
./mvnw spring-boot:run
```

Застосунок: http://localhost:8080, Keycloak: http://localhost:8081.
Користувачі Keycloak: `admin/admin` (ADMIN, PILOT), `pilot/pilot` (PILOT), `viewer/viewer` (без ролей).

## Лабораторна робота №1. Spring Security

**Завдання:** закласти фундамент системи: предметна область, базовий CRUD, база даних під контролем
міграцій, простий UI та подвійна автентифікація (OIDC для UI і JWT для API).

**Предметна область.** Облік FPV-пілотів (`Pilot`), їхніх дронів (`Drone`), обладнання (`Equipment`)
і журналів польотів (`FlightLog`). Область легко розширюється надалі: обслуговування дронів,
статистика польотів, навчання пілотів.

### Виконання критеріїв

**AC1. CRUD.** Для всіх сутностей зроблено Controller, Service, Repository, Entity і DTO, мапінг
через MapStruct. REST API: `/api/v1/pilots`, `/api/v1/drones`, `/api/v1/equipment`,
`/api/v1/flight-logs` з операціями GET, POST, PUT, DELETE.

**AC2. Міграції.** Схему створює Liquibase (`src/main/resources/db/changelog`), Hibernate працює
в режимі `validate`.

**AC3. UI.** Сторінки на Thymeleaf за адресою `/ui` дозволяють переглядати, створювати,
редагувати й видаляти записи.

**AC4. OIDC.** При відкритті `/ui` користувача перенаправляє на логін Keycloak, після входу він
повертається в застосунок із сесією. Realm імпортується автоматично з `keycloak/fpv-fleet-realm.json`.

**AC5. JWT.** Запити до `/api/**` обробляє окремий `SecurityFilterChain` без сесії, автентифікація
через заголовок `Authorization: Bearer <token>`.

**AC6. SecurityFilterChain.** DELETE-запити до API і зміна пілотів дозволені лише ролі `ADMIN`,
інакше 403.

**AC7. @PreAuthorize.** Методи сервісів перевіряють ролі незалежно від фільтрів. Наприклад,
`viewer` проходить фільтр як автентифікований, але при читанні дронів отримує 403.

**AC8. Колекція запитів.** Файл `http/fpv-fleet.http` для IntelliJ HTTP Client отримує токени,
проходить повний CRUD і перевіряє відповіді 401, 403 та 404.

### Висновок

Створено основу проєкту на Spring Boot з CRUD, міграціями Liquibase та простим UI. Налаштовано
автентифікацію через Keycloak двома способами (OIDC і JWT) та обмеження доступу на рівні
`SecurityFilterChain` і `@PreAuthorize`.
