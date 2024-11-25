# TaskManager
Приложение "Менеджер задач"


## Особенности

- CRUD операции для задач
- Аутентификация и авторизация с использованием JWT
- Управление ролями пользователей
- API документация через Swagger
- Поддержка работы с базой данных PostgreSQL

## Технологии

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- Docker
- Maven

## Установка и запуск

### Предварительные требования

- [JDK 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [PostgreSQL](https://www.postgresql.org/)

### Шаги для запуска

1. **Клонирование репозитория**

   ```bash
   git clone https://github.com/Alexandr893/TaskManager.git
   cd TaskManager

2. Убедитесь, что у вас установлен PostgreSQL и доступны настройки подключения


3. Используйте Maven для сборки и запуска приложения

   - mvn clean install
   - mvn spring-boot:run

### Docker

4. Чтобы контейнеризировать и запустить приложение с помощью Docker

  Запуск Docker контейнера:

   - docker-compose up --build
