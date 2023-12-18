![image](https://github.com/OsipovKonstantin/java-explore-with-me/assets/98541812/f57fcda2-23ce-4c70-9c97-348d22ffc02f)
# Explore with me - сервис поиска компании для совместного времяпровождения
[![Java](https://img.shields.io/badge/-Java%2011-F29111?style=for-the-badge&logo=java&logoColor=e38873)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/-Spring%202.7.5-6AAD3D?style=for-the-badge&logo=spring&logoColor=90fd87)](https://spring.io/projects/spring-framework) 
[![Postgresql](https://img.shields.io/badge/-postgresql-31648C?style=for-the-badge&logo=postgresql&logoColor=FFFFFF)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/-Hibernate-B6A975?style=for-the-badge&logo=hibernate&logoColor=717c88)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/-Maven-7D2675?style=for-the-badge&logo=apache&logoColor=e38873)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)
[![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)](https://editor-next.swagger.io/)

## Описание
Explore with me дословно переводится как исследуй со мной. Приложение предоставляет возможность делиться информацией об интересных событиях и помогать найти компанию для участия в них

## Архитектура
Приложение состоит из 2 микросервисов:
2 микросервиса:
- general-service бизнес-логика приложения
- stats-service - сбор статистики просмотров

Микросервисы и базы данных к ним запускаются в отдельных docker-контейнерах.


## Функциональность
Поведение приложения различается для администраторов (admin), незарегистрированных (public) и зарегистрированных (private) пользователей. Подробное описание эндпоинтов, классов и требований к ним приведены в спецификации [Swagger](https://app.swaggerhub.com/apis/KonstantinOsipov/explore-with_me_api/1.0)
## Диаграмма базы данных
## Как использовать
