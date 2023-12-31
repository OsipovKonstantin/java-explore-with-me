![image](https://github.com/OsipovKonstantin/java-explore-with-me/assets/98541812/ed49ca46-20ba-4a5e-b0e9-7122956c5af8)


# Explore with me - сервис поиска компании для совместного времяпровождения
[![Java](https://img.shields.io/badge/-Java%2011-F29111?style=for-the-badge&logo=java&logoColor=e38873)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/-Spring%20Boot%202.7.5-6AAD3D?style=for-the-badge&logo=spring&logoColor=90fd87)](https://spring.io/projects/spring-framework) 
[![Postgresql 14](https://img.shields.io/badge/-postgresql%2014-31648C?style=for-the-badge&logo=postgresql&logoColor=FFFFFF)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/-Hibernate%205.6-B6A975?style=for-the-badge&logo=hibernate&logoColor=717c88)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/-Maven-7D2675?style=for-the-badge&logo=apache&logoColor=e38873)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)
[![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)](https://editor-next.swagger.io/)

## Описание
Explore with me дословно переводится как исследуй со мной. На платформе пользователи делятся информацией об интересных событиях и находят компанию для участия в них

## Архитектура
Приложение состоит из 2 микросервисов:
- general-service - бизнес-логика приложения
- stats-service - сбор статистики просмотров

Микросервисы и базы данных к ним запускаются в отдельных docker-контейнерах.


## Функциональность
Поведение приложения различается для администраторов (admin), незарегистрированных (public) и зарегистрированных (private) пользователей. Подробное описание эндпоинтов, классов и требований к ним приведены в спецификации Swagger:
- [сервис статистики](https://app.swaggerhub.com/apis/KonstantinOsipov/stat-service_api/v0)
- [основной сервис](https://app.swaggerhub.com/apis/KonstantinOsipov/explore-with_me_api/1.0)
## Диаграммы базы данных для 2 микросервисов
![схемы БД для 2 микросервисов](ewm_schema_DB.png)

## Как запустить и использовать
Для запуска установите и откройте программу [Docker Desktop](https://www.docker.com/products/docker-desktop/). Затем в командной строке cmd выполните следующие команды

   ```
git clone https://github.com/OsipovKonstantin/java-explore-with-me.git
   ```
в командной строке перейдите в корень проекта. Далее
   ```
mvn clean package
   ```
   ```
docker-compose up
   ```
Приложение готово к использованию! Сервис статистики доступен по андресу [http://localhost:9090](http://localhost:9090), а основной сервис - [http://localhost:8080](http://localhost:8080)

Со сценариями работы приложения ознакомьтесь, посмотрев и запустив коллекции Postman-тестов:
- [сервис статистики](postman/stats-service.json)
- [основной сервис](postman/general-service.json)


# Дальнейшие планы - frontend страницы
## UI дизайн главной страницы сервиса
![5](https://github.com/OsipovKonstantin/java-explore-with-me/assets/98541812/266bdbc4-af53-497f-ad20-1685c654c327)

## Состояние кнопок обычное и hover (при наведении)
![image](https://github.com/OsipovKonstantin/java-explore-with-me/assets/98541812/40e183f1-1618-4c03-bd27-206a5754c75b)
