# FPV Fleet

Система обліку FPV-пілотів, їхніх дронів, обладнання та журналів польотів.
Лабораторна робота №1 — Spring Security (OIDC + JWT).

## Стек

Spring Boot 4, Spring Data JPA + Hibernate, MySQL, Liquibase, MapStruct, Lombok,
Spring Security (OAuth2 Client + Resource Server), Keycloak 24, Thymeleaf.

## Запуск

1. MySQL на `localhost:3306` (користувач `root` / `root`, база `fpv_fleet` створюється автоматично).
2. Keycloak з автоімпортом realm `fpv-fleet`:
   ```
   docker compose up -d
   ```
   Адмінка Keycloak: http://localhost:8081 (`admin` / `admin`).
3. Застосунок (JDK 17+):
   ```
   ./mvnw spring-boot:run
   ```
   UI: http://localhost:8080

Схема БД створюється Liquibase-changelog'ами з `src/main/resources/db/changelog`
(`ddl-auto: validate`, Hibernate схему не змінює).

## Користувачі Keycloak

| Логін    | Пароль   | Ролі         |
|----------|----------|--------------|
| `admin`  | `admin`  | ADMIN, PILOT |
| `pilot`  | `pilot`  | PILOT        |
| `viewer` | `viewer` | —            |

Клієнти: `fpv-ui` (confidential, authorization code — для UI),
`fpv-cli` (public, password grant — для отримання JWT у curl / HTTP-клієнті).
Ролі realm передаються у claim `roles` і мапляться на `ROLE_*`.

## Безпека

Два `SecurityFilterChain` у `SecurityConfig`:

| Ланцюг | Шляхи        | Автентифікація                     | Сесія     |
|--------|--------------|------------------------------------|-----------|
| API    | `/api/**`    | JWT `Authorization: Bearer <token>` | stateless |
| UI     | решта        | OIDC login через Keycloak          | HTTP-сесія |

Обмеження на рівні `SecurityFilterChain` (API):
- `DELETE /api/**` — лише `ADMIN`;
- `POST/PUT /api/v1/pilots/**` — лише `ADMIN`;
- решта — будь-який автентифікований користувач.

Обмеження через `@PreAuthorize` (методи сервісів, діють і для API, і для UI):
- читання та створення/редагування дронів, обладнання, журналів — `ADMIN` або `PILOT`;
- створення/редагування/видалення пілотів і будь-яке видалення — `ADMIN`.

Тому `viewer` проходить фільтр (`authenticated`), але отримує 403 від `@PreAuthorize`.

## REST API

| Ресурс        | Ендпоінти                                                            |
|---------------|----------------------------------------------------------------------|
| Пілоти        | `GET/POST /api/v1/pilots`, `GET/PUT/DELETE /api/v1/pilots/{id}`      |
| Дрони         | `GET/POST /api/v1/drones`, `GET/PUT/DELETE /api/v1/drones/{id}`, `GET /api/v1/drones/pilot/{pilotId}` |
| Обладнання    | `GET/POST /api/v1/equipment`, `GET/PUT/DELETE /api/v1/equipment/{id}`, `GET /api/v1/equipment/pilot/{pilotId}` |
| Журнали       | `GET/POST /api/v1/flight-logs`, `GET/PUT/DELETE /api/v1/flight-logs/{id}` |

## Колекція запитів для захисту

`http/fpv-fleet.http` (IntelliJ HTTP Client, оточення `dev` з `http/http-client.env.json`).
Запустити весь файл: отримує токени трьох користувачів, проходить повний CRUD,
демонструє 401, 403 від `SecurityFilterChain`, 403 від `@PreAuthorize` та 404.
Кожен запит має перевірку очікуваного статусу.

## Демонстрація OIDC

1. Відкрити http://localhost:8080/ui — редирект на сторінку логіну Keycloak.
2. Увійти як `admin` — повернення в застосунок з сесією, у шапці видно ролі.
3. Увійти як `viewer` і відкрити «Дрони» — сторінка 403 від `@PreAuthorize`.
4. «Вийти» — logout із сесії застосунку та Keycloak.
