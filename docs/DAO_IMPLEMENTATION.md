# Implementación Capa DAO - Sistema Clínico

## Resumen de Implementación

Se ha completado la implementación del patrón DAO (Data Access Object) para el proyecto integrador de Programación 2.

## Archivos Creados

### 1. Capa DAO (`src/Dao/`)
- **GenericDao.java**: Interfaz genérica con operaciones CRUD estándar
- **PacienteDao.java**: Implementación del DAO para entidad Paciente
  - Incluye método adicional `buscarPorDni(String dni)`
  - Implementa lazy loading (no carga automáticamente la historia clínica)
- **HistoriaClinicaDao.java**: Implementación del DAO para entidad HistoriaClinica
  - Incluye método adicional `buscarPorPacienteId(Long pacienteId)`
  - Manejo correcto de ENUM para MySQL

### 2. Base de Datos (`database/`)
- **schema.sql**: Script SQL para crear la base de datos y tablas en MySQL
  - Tabla `paciente`
  - Tabla `historia_clinica` con relación 1:1

## Archivos Modificados

### 1. Modelos (`src/Models/`)
- **Paciente.java**: 
  - Corregido package duplicado
  - Agregados todos los getters y setters
  - Agregado constructor con parámetros
  
- **HistoriaClinica.java**:
  - Corregido package duplicado
  - Agregado atributo `paciente` para la relación
  - Agregados todos los getters y setters
  - Agregado constructor completo

### 2. Configuración (`src/Config/`)
- **DatabaseConnection.java**:
  - Corregido package duplicado
  - Configurada URL de conexión MySQL
  - Agregado método `testConnection()` para verificar conectividad

### 3. Servicios (`src/Service/`)
- **PacienteService.java**:
  - Actualizados imports para usar las nuevas clases DAO
  - Corregidos packages (Config, Dao, Models)
  
- **GenericService.java**:
  - Corregido package

## Configuración Necesaria

### 1. Base de Datos MySQL

Ejecutar el script SQL para crear la base de datos:

```bash
mysql -u root -p < database/schema.sql
```

O ejecutar manualmente los comandos desde MySQL Workbench o phpMyAdmin.

### 2. Credenciales de Base de Datos

Editar el archivo `src/Config/DatabaseConnection.java` y configurar:

```java
private static final String URL = "jdbc:mysql://localhost:3306/clinica_db?useSSL=false&serverTimezone=UTC";
private static final String USER = "root"; // Tu usuario MySQL
private static final String PASS = "tu_contraseña"; // Tu contraseña MySQL
```

### 3. Driver MySQL

Asegurarse de tener el driver JDBC de MySQL en el proyecto:

**Para Maven:**
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

**Para NetBeans (sin Maven):**
- Descargar `mysql-connector-java-8.0.33.jar`
- Click derecho en el proyecto → Properties → Libraries → Add JAR/Folder
- Seleccionar el archivo descargado

## Características Implementadas

### Patrón DAO
- ✅ Interfaz genérica `GenericDao<T>` con operaciones CRUD
- ✅ Implementaciones concretas para Paciente e HistoriaClinica
- ✅ Separación de responsabilidades (DAO vs Service)
- ✅ Uso de PreparedStatement (prevención de SQL Injection)

### Operaciones CRUD
- ✅ Crear (con y sin conexión compartida para transacciones)
- ✅ Leer por ID
- ✅ Leer todos (solo registros no eliminados)
- ✅ Actualizar
- ✅ Eliminar (borrado lógico)

### Características Adicionales
- ✅ Lazy loading en PacienteDao (no carga HistoriaClinica automáticamente)
- ✅ Manejo correcto de ENUM (GrupoSanguineo) con MySQL
- ✅ Soporte para transacciones (conexión compartida)
- ✅ Métodos de búsqueda específicos:
  - `buscarPorDni()` en PacienteDao
  - `buscarPorPacienteId()` en HistoriaClinicaDao

### Borrado Lógico
- ✅ Campo `eliminado` en todas las tablas
- ✅ Método `eliminar()` actualiza el flag en lugar de borrar físicamente
- ✅ Método `leerTodos()` filtra registros eliminados

## Estructura del Proyecto

```
UTN-TUPAD-P2-Integrador-Grupo49/
├── database/
│   └── schema.sql              # Script de creación de base de datos
├── src/
│   ├── Config/
│   │   └── DatabaseConnection.java
│   ├── Dao/
│   │   ├── GenericDao.java           # Interfaz genérica
│   │   ├── PacienteDao.java          # DAO Paciente
│   │   └── HistoriaClinicaDao.java   # DAO HistoriaClinica
│   ├── Models/
│   │   ├── Paciente.java
│   │   └── HistoriaClinica.java
│   └── Service/
│       ├── GenericService.java
│       └── PacienteService.java
├── DAO_IMPLEMENTATION.md       # Este archivo
└── README.md
```

## Próximos Pasos

1. **Configurar las credenciales de MySQL** en `DatabaseConnection.java`
2. **Ejecutar el script SQL** para crear la base de datos y tablas
3. **Agregar el driver MySQL** al proyecto
4. **Crear clases de prueba** para verificar el funcionamiento
5. **Implementar la capa de presentación** (UI o API REST)

## Notas Importantes

- El proyecto usa **borrado lógico**: los registros no se eliminan físicamente
- La relación Paciente-HistoriaClinica es **1:1**
- Se implementa **lazy loading**: la historia clínica no se carga automáticamente al leer un paciente
- Las transacciones están soportadas mediante el método `crear(T entity, Connection conn)`
- El Service ya está preparado para usar estos DAOs (ver `PacienteService.java`)

## Autores

Grupo 49 - Comisión 1
- Matías Costantini
- Lucas E Amato
- Ivan Daniliuk
- Augusto Matías Cúneo

---

**Fecha de implementación:** Noviembre 2025
**Tecnologías:** Java, MySQL, JDBC, Patrón DAO

