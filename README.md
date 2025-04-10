# ClubBook

## Descripción

**Clubbook** es una aplicación móvil desarrollada como **Trabajo de Fin de Grado (TFG)** con el objetivo de ofrecer una solución tecnológica accesible para la gestión de clubes deportivos, especialmente aquellos con recursos limitados que no pertenecen a disciplinas mayoritarias como el fútbol o el baloncesto.

### ¿Qué es Clubbook?

Clubbook permite digitalizar y simplificar las tareas administrativas y organizativas de un club deportivo mediante una interfaz moderna y accesible. La app contempla tres tipos de usuarios, cada uno con funcionalidades específicas:

- **Administradores**: gestión de usuarios, clases, temporadas y eventos.
- **Profesores**: control de asistencia, agenda de clases y acceso a información de eventos.
- **Alumnos o tutores legales**: inscripción en eventos, recepción de notificaciones y alertas (como ausencias).

### Enfoque del proyecto

El desarrollo se ha llevado a cabo aplicando **metodologías ágiles**, con especial atención a la **usabilidad**, la **accesibilidad** y un enfoque **centrado en el usuario**. Además, se han realizado pruebas con usuarios reales para validar el funcionamiento de la app y detectar mejoras de cara a futuras versiones.

### Objetivos

El objetivo principal de **Clubbook** es desarrollar un sistema que facilite la gestión de miembros en clubes deportivos y mejore la comunicación entre clubes, profesores y deportistas (o sus tutores legales).

Para alcanzar este objetivo general, se han planteado los siguientes objetivos específicos:

- **Desarrollar un servicio RESTful** que implemente toda la lógica del sistema, incluyendo la capa de persistencia de datos.
- **Desarrollar una aplicación móvil multiplataforma** que consuma dicho servicio RESTful, prestando especial atención a la **usabilidad**, **accesibilidad**, **rendimiento** y **seguridad**.
- **Aplicar una metodología de desarrollo iterativa e incremental**, lo que permite mantener la flexibilidad del proyecto, adaptarse a cambios y entregar valor de manera continua.

Estos objetivos han guiado el diseño y la implementación de Clubbook, orientando el desarrollo hacia una solución robusta, funcional y centrada en las necesidades reales de los clubes deportivos.


## Tecnologías

El desarrollo de Clubbook se ha basado en un stack tecnológico moderno que permite construir una solución robusta, escalable y multiplataforma. A continuación se detallan las tecnologías principales empleadas:

### React Native + Expo
Se ha utilizado **React Native** junto con **Expo** para el desarrollo de la aplicación móvil. Esta combinación permite:

- Desarrollar una **app multiplataforma** (Android e iOS) desde una única base de código.
- Acelerar el desarrollo gracias a herramientas como recarga en tiempo real y una comunidad muy activa.
- Acceder a componentes nativos de forma sencilla a través del ecosistema de Expo.

### Java con Spring Boot
El backend del sistema está desarrollado en **Java** utilizando el framework **Spring Boot**, que permite:

- Construir una **API RESTful** de forma sencilla, estructurada y segura.
- Integrar fácilmente servicios, controladores, y mecanismos de seguridad.
- Escalar la aplicación en entornos productivos de manera eficiente.

### PostgreSQL
La persistencia de datos se ha gestionado con **PostgreSQL**, un sistema de gestión de bases de datos relacional de código abierto que ofrece:

- Alto rendimiento y fiabilidad.
- Soporte completo para operaciones complejas y relaciones entre entidades.
- Facilidad de integración con Spring Boot mediante JPA/Hibernate.

Este conjunto de tecnologías permite ofrecer una experiencia de usuario fluida, mantener una arquitectura limpia y preparar el sistema para futuras mejoras o ampliaciones.

## Repositorio del proyecto

Este repositorio contiene todo el código fuente y documentación del proyecto **Clubbook**. En él se incluye:

- El frontend móvil, desarrollado con **React Native** y gestionado mediante **Expo**.
- El backend, implementado con **Java** utilizando el framework **Spring Boot**.
- La configuración de la base de datos relacional **PostgreSQL**.
- Scripts, documentación y recursos necesarios para la instalación y despliegue del sistema.

El repositorio está estructurado para facilitar la navegación entre los distintos componentes del sistema y permitir una rápida puesta en marcha del proyecto por parte de otros desarrolladores o interesados en su evolución.

## Instalación y ejecución del proyecto

Este proyecto está compuesto por una aplicación móvil desarrollada con **React Native** y **Expo**, y un backend en **Java con Spring Boot**. A continuación se detallan los pasos necesarios para instalar y ejecutar ambos componentes.

### Requisitos previos

Antes de comenzar, asegúrate de tener instalados los siguientes elementos:

- Node.js
- React Native
- https://git-scm.com/
- Una cuenta gratuita en Expo
- Java 17 o superior
- PostgreSQL
- Expo Go

### Backend
1. Instalar Java 17 o superior y Git.
2. Instalar PostgreSQL y crear una base de datos para el proyecto.
3. Configurar en application.properties la base de datos.
```bash
spring.datasource.url=<ruta a la base de datos>
spring.datasource.username=<usuario>
spring.datasource.password=<contraseña>
```
5. Ejecutar el proyecto Spring Boot.

### Frontend
1. Instalar Node.js.
2. Clonar repositorio.
3. Abre PowerShell o CMD y ejecuta el siguiente comando:
```bash
npm install -g react-native-cli
npm install -g expo-cli
```
3. Una vez instalado Expo CLI y React Native, navega al directorio del proyecto (ClubBook) y ejecuta el siguiente comando para instalar las dependencias del proyecto:
```bash
npm install
```
4. Para iniciar el servidor de desarrollo de Expo, ejecuta:
```bash
expo start
```
5. Escanea el QR que aparece en la terminal para ejecutarlo en la aplicación Expo CLI.



