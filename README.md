# 👨‍🍳 API de Gestión de Servicios Culinarios y Chefs

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue) ![JWT](https://img.shields.io/badge/Security-JWT-red)

## 📄 Descripción

Este proyecto es una API REST desarrollada con **Spring Boot** que implementa un sistema para conectar a **Chefs profesionales** con **Clientes** que desean contratar servicios de comida. 

El proyecto se centra en la seguridad en aplicaciones distribuidas (UD2), implementando autenticación mediante **JWT (JSON Web Tokens)**, gestión de roles, validaciones y requisitos de protección de datos como la **anonimización** de usuarios al eliminar cuentas.

## 🚀 Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot (Web, Data JPA, Security, Validation)
* **Base de Datos:** PostgreSQL
* **Seguridad:** Spring Security + JWT (jjwt)
* **Documentación:** OpenAPI / Swagger UI
* **Build Tool:** Maven

## ⚙️ Funcionalidades Principales

### 🔐 Seguridad y Roles
El sistema cuenta con tres roles diferenciados (`ADMIN`, `CHEF`, `CLIENTE`) gestionados en `SecurityConfiguration.java`:

* **Rutas Públicas:** Login, Registro de Chefs/Clientes, Listado de Chefs activos, Noticias.
* **Rutas Privadas:** Requieren Token Bearer JWT.

### 👤 Actores
1.  **Administrador:**
    * Gestión completa de usuarios.
    * **Banear/Desbanear:** Puede bloquear el acceso a Chefs o Clientes.
    * Crear otros administradores.
2.  **Chef:**
    * Gestionar su perfil y disponibilidad (Activo/Inactivo).
    * Ver servicios recibidos.
    * Establecer precio y ver su valoración media.
3.  **Cliente:**
    * Contratar servicios culinarios.
    * Puntuar servicios finalizados (0-5 estrellas).
    * Gestionar datos médicos (alergias) y dirección.

### 🛡️ Protección de Datos (GDPR)
* **Anonimización:** Al eliminar una cuenta (Cliente, Chef o Admin), los datos no se borran físicamente para mantener la integridad referencial, pero se ofuscan (e.g., nombre pasa a "ANONIMO", email a "anonimoID@deleteUser.com") y se bloquea el acceso.

## 🛠️ Configuración e Instalación

### Prerrequisitos
* Java JDK 17 o superior.
* Maven.
* PostgreSQL instalado y ejecutándose.

### Configuración de Base de Datos
Crea una base de datos en PostgreSQL llamada `simulacro_db`.
El proyecto está configurado en `application.properties` con:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/simulacro_db
spring.datasource.username=postgres
spring.datasource.password=password
