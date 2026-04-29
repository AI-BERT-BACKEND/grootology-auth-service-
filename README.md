# AI.BERT - Auth

> Microservicio de autenticación  para A.IBERT — maneja registro, login con JWT.

---

# Tabla de Contenido

- Descripción General
- Equipo
- Objetivos
- Planteamiento del Problema
- Requerimientos
- Arquitectura
- Stack Tecnológico
- Diagramas
- Gestión del Proyecto
- Pruebas y Calidad
- Demo
- Instalación
- Referencias

---

# Descripción General

## Resumen Ejecutivo

* Problema: Los estudiantes no tienen una plataforma unificada que gestione su identidad académica y les permita acceder de forma segura a sus datos de planificación.
  
* Solución: Microservicio independiente que maneja registro, autenticación con JWT, siendo la puerta de entrada a todos los demás módulos de A.IBERT.

* Usuarios objetivo: Estudiantes universitarios de la ECI.
  
* Impacto esperado: Acceso seguro y centralizado para los 6 módulos del sistema, con sesiones protegidas.

## Alcance

### Incluye
- Inicio de sesión

---

#  Equipo

**Nombre del equipo**

| Integrante | Rol | Responsabilidades |
|-----------|------|------------------|
| Mariana Parra | Lider |  Revisión, coordinación con otros equipos|
| Dana Leal | Backend | UI/UX |
| Andres Sabogal | DevOps | Infraestructura |
| Nicolas Parrado| Arquitectura | Diseño del sistema |

---

# Objetivos

## Objetivo General

Construir un microservicio de autenticación seguro que provea tokens JWT válidos para que los demás módulos de A.IBERT puedan autenticar peticiones.

## Objetivos Específicos

- Generar y validar tokens JWT consumibles por los otros 5 microservicios

---

# Planteamiento del Problema

## Contexto

A.IBERT es una plataforma de planificación académica compuesta por microservicios independientes. Para que funcionen de forma coordinada, todos necesitan identificar al usuario que hace cada petición de forma segura y sin acoplar su lógica de autenticación.

## Problema

No existe un mecanismo centralizado que gestione la identidad del estudiante y emita credenciales reconocidas por todos los módulos del sistema.

## Dificultades Actuales

- Sin autenticación centralizada, cada microservicio tendría que manejar su propia lógica de login

## Solución Propuesta

Un microservicio dedicado (auth-service) que emite JWT firmados con una SECRET_KEY compartida. Cualquier microservicio puede verificar el token localmente sin consultar la base de datos de auth en cada petición.

---

#  Requerimientos

---

## Requerimientos Funcionales

| ID | Requerimiento | Módulo |
|----|---------------|--------|
| R01 | Login de usuarios | Autenticación |


---

##  Análisis de Requerimientos

- [Documento de análisis](docs/requisitos.md)

---

#  Arquitectura

## Arquitectura General

El auth-service forma parte de una arquitectura de microservicios. Se comunica con los demás módulos a través del API Gateway (puerto 8000), que valida el JWT antes de redirigir cada petición.

Frontend (React) → API Gateway :8000 → auth-service :8001 → PostgreSQL.

Patrón: MVC por capas (Controller → Service → Repository).

El token JWT es verificable por cualquier microservicio usando la SECRET_KEY compartida.

No hay comunicación directa entre auth-service y otros microservicios — todo pasa por el Gateway.

---

#  Stack Tecnológico

| Área | Tecnologías |
|------|-------------|
| Backend | Java 21, Spring Boot |
| Frontend | React / Angular |
| API | REST, OpenAPI |
| Seguridad | Spring Security, JWT |
| SQL | PostgreSQL |
| NoSQL | MongoDB |
| Testing | JUnit, Mockito |
| DevOps | Docker, GitHub Actions |
| Calidad | SonarCloud, JaCoCo |

---

#  Diagramas

## Contexto
- [Diagrama de contexto](docs/diagramas/contexto.png)

## Casos de Uso
- [Casos de uso](docs/diagramas/casos-uso.png)

## Diagrama de Clases
- [Diagrama de clases](docs/diagramas/clases.png)

## Componentes
- [Diagrama de componentes](docs/diagramas/componentes.png)

## Entidad Relación
- [ER Diagram](docs/diagramas/er.png)

## Secuencia

- [01 Registro usuario](docs/secuencia/registro.md)
- [02 Login](docs/secuencia/login.md)
- [03 Gestión principal](docs/secuencia/modulo.md)

---

#  Gestión del Proyecto

## Metodología

- Scrum

## Sprints

| Sprint | Objetivo | Estado |
|-------|----------|--------|
| Sprint 1 | Setup proyecto | ✅ |


---

#  Pruebas

## Estrategia

- Unitarias
- Integración


## Reporte

[Ver reporte pruebas](docs/testing/pruebas.md)

---

# Cobertura

Reporte generado con **JaCoCo** y analizado con **SonarQube**

| Métrica | Cubierto | Total | Cobertura |
|---|---|---|---|
| Líneas | 1964 | 2188 | 90% |
| Ramas | 509 | 745 | 68% |
| Métodos | 744 | 794 | 94% |

## Calidad

- Bugs: 0 críticos
- Code Smells: X
- Deuda técnica: Baja
- Quality Gate: ✅ Passed

---

#  Demo

## Video Demo
- [Demo módulo](link-demo)

## Capturas

Agregar screenshots aquí.

---

# Instalación

## Requisitos

- Java 21
- Docker
- PostgreSQL

## Clonar repositorio

```bash
git clone https://github.com/usuario/proyecto.git
cd proyecto
```

## Backend

```bash
./mvnw spring-boot:run
```

## Frontend

```bash
npm install
npm run dev
```

---

# Estructura del Proyecto

```bash
src/
docs/
tests/
docker/
```

---

#  Referencias

- Documentación oficial Spring
- PostgreSQL docs
- OpenAPI
- Papers / fuentes usadas

---

