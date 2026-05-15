<div align="center">

# 🔐 AIBERT — Authentication Service

### *"Acceso seguro y centralizado para toda la plataforma AIBERT"*

---

### 🛠️ Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge&logo=postgresql&logoColor=white)

### ☁️ Infraestructura & Calidad

![Azure](https://img.shields.io/badge/Azure-Cloud-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

### 🏗️ Arquitectura

![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-009688?style=for-the-badge)

</div>

---

## 📑 Tabla de Contenidos

1. [👤 Integrantes](#1--integrantes)
2. [🎯 Objetivo del Microservicio](#2--objetivo-del-microservicio)
3. [⚡ Funcionalidades Principales](#3--funcionalidades-principales)
4. [📋 Estrategia de Versionamiento y Branches](#4--manejo-de-estrategia-de-versionamiento-y-branches)
5. [⚙️ Tecnologías Utilizadas](#5--tecnologias-utilizadas)
6. [🧩 Funcionalidad](#6--funcionalidad)
7. [📊 Diagramas](#7--diagramas)
8. [⚠️ Manejo de Errores](#8--manejo-de-errores)
9. [🧪 Evidencia de Pruebas y Ejecución](#9--evidencia-de-las-pruebas-y-como-ejecutarlas)
10. [🗂️ Organización del Código](#10--codigo-de-la-implementacion-organizado-en-las-respectivas-carpetas)
11. [🚀 Ejecución del Proyecto](#11--ejecucion-del-proyecto)
12. [☁️ CI/CD y Despliegue en Azure](#12--evidencia-de-cicd-y-despliegue-en-azure)
13. [🤝 Contribuciones](#13--contribuciones)

---

## 1. 👤 Integrantes

- **Equipo:** Grootyology

---

## 2. 🎯 Objetivo del microservicio

El Authentication Service tiene como objetivo centralizar la autenticación de usuarios dentro de la plataforma AIBERT, validando credenciales y generando tokens JWT seguros que permiten a los demás microservicios identificar al usuario sin duplicar lógica de seguridad.

Este microservicio actúa como **la puerta de entrada al sistema**, permitiendo que todos los módulos confíen en un único mecanismo de autenticación y autorización.

---

## 3. ⚡ Funcionalidades principales

<div align="center">

<table>
  <thead>
    <tr>
      <th>🧩 Funcionalidad</th>
      <th>Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Inicio de Sesión</strong></td>
      <td>Valida credenciales de usuario y permite el acceso al sistema.</td>
    </tr>
    <tr>
      <td><strong>Generación de JWT</strong></td>
      <td>Emite tokens JWT firmados que son consumidos por los demás microservicios.</td>
    </tr>
    <tr>
      <td><strong>Seguridad Centralizada</strong></td>
      <td>Evita que cada microservicio maneje su propia lógica de autenticación.</td>
    </tr>
  </tbody>
</table>

</div>

---

## 4. 📋 Manejo de Estrategia de versionamiento y branches

Para el desarrollo del **Authentication Service** se utiliza una estrategia de control de versiones basada en **Git Flow**, la cual permite organizar el trabajo del equipo y separar claramente los cambios en desarrollo de las versiones estables del microservicio.

Esta estrategia ha sido útil para trabajar de forma ordenada, especialmente al implementar nuevas funcionalidades, ajustes de infraestructura y cambios derivados de nuevos requerimientos.

### Estrategia de Ramas (Git Flow)

El repositorio maneja principalmente las siguientes ramas:

- `main`
- `develop`
- `feature/*`

De manera complementaria, el modelo permite el uso de ramas `release/*` y `hotfix/*` en caso de ser necesarias para estabilizar versiones o corregir errores críticos, aunque el desarrollo cotidiano se ha realizado principalmente mediante ramas de tipo `feature/*`.

### Ramas y propósito

#### `main`
- Contiene la versión estable del microservicio.
- Se utiliza como referencia para demostraciones o despliegues.
- No se realizan desarrollos directos sobre esta rama.
- Los cambios llegan a `main` únicamente después de haber sido integrados y probados en `develop`.

#### `develop`
- Rama utilizada para integrar las funcionalidades en desarrollo.
- Sirve como base para crear nuevas ramas `feature/*`.
- Permite validar la correcta integración de los cambios antes de considerarlos estables.

#### `feature/*`
- Ramas utilizadas para desarrollar funcionalidades específicas, mejoras técnicas o tareas relacionadas con infraestructura.
- Se crean a partir de `develop` y se integran nuevamente mediante Pull Requests.
- Ejemplos de ramas utilizadas durante el desarrollo del microservicio:
    - `feature/auth-ci-cd`
    - `feature/dockerizacion`
    - `feature/nuevos-requerimientos`

Este enfoque permitió trabajar cada cambio de forma aislada y reducir conflictos durante la integración.

### Flujo de trabajo general

1. Se crea una rama `feature/*` desde `develop`.
2. Se implementan los cambios correspondientes.
3. Se validan las modificaciones de forma local.
4. Se genera un Pull Request hacia `develop`.
5. Una vez consolidadas las funcionalidades, `develop` se integra a `main` para actualizar la versión estable.

Esta estrategia ha permitido mantener un flujo de desarrollo claro y controlado a lo largo del proyecto.

---

## 5. ⚙️ Tecnologías Utilizadas

| Tecnología | Uso principal |
|------------|---------------|
| **Java 21** | Lenguaje base del microservicio, aprovechando mejoras de rendimiento, compatibilidad moderna y soporte a largo plazo. |
| **Spring Boot** | Framework principal para la creación del microservicio y la exposición de APIs REST relacionadas con el perfil del usuario. |
| **Spring Data JPA** | Abstracción para el acceso a datos y manejo de entidades, permitiendo interactuar con la base de datos de forma sencilla y desacoplada. |
| **PostgreSQL** | Base de datos relacional utilizada para persistir la información personal y académica del usuario de manera estructurada. |
| **Apache Maven** | Gestión de dependencias, construcción y empaquetado del proyecto. |
| **Docker** | Contenerización del microservicio para facilitar su despliegue y ejecución en distintos entornos. |
| **GitHub Actions** | Automatización de procesos de integración continua (CI), validación del código y ejecución de pruebas. |

---

## 6. 🧩 Funcionalidad

### 🔐 Inicio de Sesión

Permite autenticar a un usuario dentro del sistema AIBERT validando sus credenciales y generando un token JWT que será utilizado para autorizar las peticiones a los demás microservicios.

**Endpoint principal:**  
`POST /api/auth/login`

---

### 📦 Estructura de la Solicitud (Request)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricción | 📝 Descripción |
|---------|---------|:-------------:|---------------|
| email | String | Obligatorio | Correo electrónico del usuario |
| password | String | Obligatorio | Contraseña asociada a la cuenta |

</div>

---

### 📦 Estructura de la Respuesta (Response)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|---------|---------|---------------|
| token | String | Token JWT firmado para autenticación |
| expiresAt | LocalDateTime | Fecha y hora de expiración del token |

</div>

---

## 7. 📊 Diagramas


---

### 🧱 Diagrama de Clases — Authentication Service

El siguiente diagrama de clases representa la organización interna del microservicio, siguiendo una arquitectura hexagonal dividida en capas de **Entrypoints**, **Application**, **Domain** e **Infrastructure**.

Se observa cómo el `AuthController` delega la autenticación al `LoginUseCase`, el manejo de DTOs de entrada y salida, las entidades de dominio involucradas y las excepciones asociadas al proceso de autenticación.

<div align="center">

![Diagrama_de_Clases.png](docs/uml/Diagrama_de_Clases.png)

</div>

---

### 🧩 Diagrama de Componentes — Authentication Service

El diagrama de componentes muestra la interacción entre los principales componentes del microservicio durante el proceso de autenticación.

El flujo evidencia la separación de responsabilidades entre controlador, servicio de aplicación, caso de uso y acceso a persistencia mediante puertos y adaptadores, finalizando en la base de datos de usuarios.

<div align="center">

![Diagrama_de_componentes.png](docs/uml/Diagrama_de_componentes.png)

</div>

---

### 🔁 Diagrama de Secuencia — `POST /api/auth/login`

El diagrama de secuencia describe el flujo completo del proceso de inicio de sesión. El usuario envía sus credenciales al `AuthController`, el cual delega la petición al `LoginService` y posteriormente al `LoginUseCase`.

Durante el flujo se realiza la consulta del usuario mediante el `UserRepositoryPort`, su implementación en el adaptador correspondiente y el acceso a la base de datos. En caso de credenciales válidas, se genera el token JWT y se retorna la respuesta al cliente.

<div align="center">

![Diagrama de Secuencia - Login](docs/uml/Diagrama_secuencia_login.png)

</div>

---

## 8. ⚠️ Manejo de Errores

El **Authentication Service** implementa un mecanismo centralizado de manejo de errores con el fin de garantizar respuestas claras, consistentes y seguras ante los distintos escenarios que pueden ocurrir durante el proceso de autenticación.

A través de un **manejador global de excepciones** , se interceptan errores tanto de validación como del dominio de negocio, evitando exponer información sensible y manteniendo un formato de respuesta uniforme para el cliente.

Este enfoque permite que el frontend y los demás microservicios puedan manejar los errores de forma predecible y desacoplada de la implementación interna.

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 Código HTTP | ⚠️ Escenario |
|:-------------:|:------------|
| **400 Bad Request** | Datos inválidos en la petición, campos obligatorios faltantes o formatos incorrectos. |
| **401 Unauthorized** | Credenciales incorrectas durante el inicio de sesión. |
| **403 Forbidden** | Token JWT inválido, expirado o no autorizado para acceder al recurso. |
| **500 Internal Server Error** | Error inesperado en el servidor durante el proceso de autenticación. |

</div>

---

Cuando ocurre un error, el servicio retorna únicamente la información necesaria para que el cliente pueda reaccionar adecuadamente, sin revelar detalles internos del sistema, reforzando así las buenas prácticas de seguridad y manejo de excepciones.

---

## 9. 🧪 Evidencia de Pruebas y Ejecución

El microservicio cuenta con **pruebas unitarias** sobre los casos de uso principales.


### 🚀 Cómo ejecutar las pruebas

#### 1️⃣ Ejecutar todas las pruebas unitarias

El siguiente comando ejecuta todas las pruebas del microservicio:

```bash
mvn clean test
```

---

## 10. 🗂️ Organización del Código (Scaffolding)

El microservicio sigue una arquitectura hexagonal (puertos y adaptadores):

```
auth-service/
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/aibert/dosw/
│   │   │   ├── 📁 application/                     # 🔵 CAPA DE APLICACIÓN
│   │   │   │   ├── 📁 dto/
│   │   │   │   │   ├── 📁 request/                 # DTOs de entrada (LoginRequest)
│   │   │   │   │   └── 📁 response/                # DTOs de salida (LoginResponse)
│   │   │   │   └── 📁 service/                     # Lógica de aplicación (LoginService)
│   │   │   │
│   │   │   ├── 📁 config/                          # ⚙️ CONFIGURACIONES
│   │   │   │                                   # Seguridad, JWT, filtros
│   │   │   │
│   │   │   ├── 📁 domain/                          # 🟢 CAPA DE DOMINIO
│   │   │   │   ├── 📁 exceptions/                  # Excepciones del dominio
│   │   │   │   ├── 📁 model/user/                  # Entidad User
│   │   │   │   └── 📁 ports/                       # Puertos In / Out
│   │   │   │       ├── 📁 in/
│   │   │   │       └── 📁 out/
│   │   │   │
│   │   │   ├── 📁 entrypoints/                     # 🔴 CAPA DE ENTRADA
│   │   │   │   ├── 📁 restcontroller/              # AuthController
│   │   │   │   └── 📁 advice/                      # Manejo global de errores
│   │   │   │
│   │   │   ├── 📁 infrastructure/adapters/         # 🟠 CAPA DE INFRAESTRUCTURA
│   │   │   │   ├── 📁 adapter/                     # Implementación de puertos
│   │   │   │   └── 📁 persistence/
│   │   │   │       ├── 📁 entity/                  # Entidad JPA
│   │   │   │       ├── 📁 mapper/                  # Mapeadores dominio ↔ persistencia
│   │   │   │       └── 📁 repository/              # Repositorios JPA
│   │   │   │
│   │   │   └── AuthServiceApplication            # Punto de arranque Spring Boot
│   │   │
│   │   └── 📁 resources/                           # application.yml
│   │
│   └── 📁 test/                                    # 🧪 PRUEBAS UNITARIAS
│
└── pom.xml         
```

---

## 11. 🚀 Ejecución del Proyecto

### 📋 Prerrequisitos
- **Java 21**
- **Maven 3.8+**
- **Docker** (Opcional)

### 🛠️ Opción 1: Ejecución Local (Maven)

```bash
mvn spring-boot:run
```
📍 **URL Local:** `http://localhost:8080` (o el puerto configurado)  
📚 **Documentación API (Swagger):** `http://localhost:8080/swagger-ui.html`

### 🐳 Opción 2: Ejecución con Docker (Si se incluye Dockerfile)

```bash
docker-compose up --build -d
```

---

## 12. ☁️ CI/CD y Despliegue en Azure

El proyecto tiene capacidad para desplegarse mediante GitHub Actions hacia Azure App Service o un entorno contenedorizado en la nube.
Se definen perfiles como `dev` y `prod` en `application.yml` para gestionar la cadena de conexión de MongoDB y las keys de Gemini/Groq.

---

## 13. 🤝 Contribuciones

### Metodología
Se utiliza **Scrum** con iteraciones cortas, asegurando entregas continuas y mejora de valor. Las ramas principales son protegidas y todos los PRs deben cumplir validación estática (SonarQube) y ejecutar pipelines de CI.

<div align="center">

### 🏆 Proyecto AIBERT

![Course](https://img.shields.io/badge/Course-DOSW-orange?style=for-the-badge)
![Year](https://img.shields.io/badge/Year-2026-blue?style=for-the-badge)

</div>