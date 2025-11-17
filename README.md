# Sistema de Gestión Clínica - Proyecto Integrador

## 📋 Descripción

Sistema de gestión de pacientes y historias clínicas desarrollado como Trabajo Final Integrador de Programación 2. Implementa el patrón de diseño **DAO (Data Access Object)** con arquitectura en capas, manejo de transacciones y borrado lógico.

## 👥 Integrantes - Grupo 49

**Comisión 1**

1. Matías Costantini - matias.costantini@tupad.utn.edu.ar
2. Lucas E Amato - lucasezequielamato@gmail.com
3. Ivan Daniliuk - ivan.daniliuk@tupad.utn.edu.ar
4. Augusto Matías Cúneo - augusto_cuneo@hotmail.com

### Link de video 📺 https://youtu.be/3gS84lJrQPk

## 🏗️ Arquitectura del Proyecto

El proyecto implementa una arquitectura en capas siguiendo el patrón **DAO**:

```
┌─────────────────────────────────┐
│   Capa de Presentación          │
│   (Main / UI)                   │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Capa de Servicio              │
│   (Lógica de Negocio)           │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Capa DAO                      │
│   (Acceso a Datos)              │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Base de Datos MySQL           │
│   (Persistencia)                │
└─────────────────────────────────┘
```

## 🗂️ Estructura del Proyecto

```
UTN-TUPAD-P2-Integrador-Grupo49/
│
├── src/
│   ├── Config/
│   │   └── DatabaseConnection.java    # Configuración de conexión MySQL
│   │
│   ├── Models/
│   │   ├── Paciente.java             # Entidad Paciente
│   │   └── HistoriaClinica.java      # Entidad Historia Clínica
│   │
│   ├── Dao/
│   │   ├── GenericDao.java           # Interfaz genérica CRUD
│   │   ├── PacienteDao.java          # DAO de Paciente
│   │   └── HistoriaClinicaDao.java   # DAO de Historia Clínica
│   │
│   ├── Service/
│   │   ├── GenericService.java       # Interfaz genérica de servicio
│   │   └── PacienteService.java      # Servicio con lógica de negocio
│   │
│   └── Main/
│       └── MainApp.java              # Aplicación principal (demo completa)
│
├── database/
│   └── schema.sql                    # Script de creación de BD
│
├── lib/
│   └── mysql-connector-j-8.2.0.jar  # Driver JDBC MySQL
│
├── nbproject/                        # Configuración NetBeans
├── build.xml                         # Script de compilación Ant
└── README.md                         # Este archivo
```

## 🚀 Tecnologías Utilizadas

- **Java 24** - Lenguaje de programación
- **MySQL 5.7+** - Base de datos relacional
- **JDBC** - Conectividad con base de datos
- **Apache Ant** - Gestión de compilación
- **NetBeans IDE** - Entorno de desarrollo

## 💾 Base de Datos

### Modelo de Datos

**Relación 1:1 entre Paciente y Historia Clínica**

```sql
┌─────────────────┐           ┌──────────────────────┐
│    Paciente     │───────────│  Historia Clínica    │
├─────────────────┤    1:1    ├──────────────────────┤
│ id (PK)         │           │ id (PK)              │
│ nombre          │           │ nro_historia (UNIQUE)│
│ apellido        │           │ grupo_sanguineo      │
│ dni (UNIQUE)    │           │ antecedentes         │
│ fecha_nacimiento│           │ medicacion_actual    │
│ eliminado       │           │ observaciones        │
└─────────────────┘           │ paciente_id (FK)     │
                              │ eliminado            │
                              └──────────────────────┘
```

### Características de la BD

- ✅ Restricciones de integridad referencial
- ✅ Claves únicas (DNI, número de historia)
- ✅ Borrado lógico (campo `eliminado`)
- ✅ ENUM para grupo sanguíneo
- ✅ Índices para optimización

## ⚙️ Configuración e Instalación

### 1. Requisitos Previos

- Java JDK 11 o superior
- MySQL 5.7 o superior
- Apache Ant (incluido con NetBeans)
- NetBeans IDE (opcional, recomendado)

### 2. Configurar MySQL

**Crear la base de datos:**

```bash
# Conectar a MySQL en puerto 3307
mysql -u root -p -P 3307 < database/schema.sql
```

**O ejecutar manualmente:**
- Abrir `database/schema.sql` en MySQL Workbench
- Ejecutar el script completo

### 3. Configurar Credenciales

Editar `src/Config/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3307/clinica_db?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASS = "tu_contraseña";  // ← Cambiar aquí
```

### 4. Descargar Driver MySQL

**Opción A: Automática (Windows)**
```bash
./download-mysql-driver.bat
```

**Opción B: Manual**
1. Descargar: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar
2. Guardar en: `lib/mysql-connector-j-8.2.0.jar`

### 5. Compilar y Ejecutar

**Con NetBeans:**
1. Abrir el proyecto en NetBeans
2. Presionar `F11` para compilar
3. Presionar `F6` para ejecutar

**Con línea de comandos:**
```bash
# Compilar
ant clean compile

# Ejecutar test de conexión
ant run -Dmain.class=Main.TestConnection

# Ejecutar aplicación completa
ant run
```

## 🎯 Características Implementadas

### Patrón DAO Completo

#### 1. **Interfaz Genérica**
```java
public interface GenericDao<T> {
    T crear(T entity);
    T crear(T entity, Connection conn); // Para transacciones
    T leer(Long id);
    List<T> leerTodos();
    void actualizar(T entity);
    void eliminar(Long id); // Borrado lógico
}
```

#### 2. **Operaciones CRUD**
- ✅ **Create** - Inserción con transacciones
- ✅ **Read** - Lectura individual y masiva
- ✅ **Update** - Actualización de registros
- ✅ **Delete** - Borrado lógico (no físico)

#### 3. **Características Avanzadas**

**Transacciones:**
```java
// Insertar Paciente + Historia Clínica en una sola transacción
Paciente paciente = pacienteService.insertarPacienteCompleto(nuevoPaciente);
// Si falla algo, hace ROLLBACK automático
```

**Lazy Loading:**
```java
// PacienteDao NO carga automáticamente la HistoriaClinica
Paciente p = pacienteDao.leer(1L);
// p.getHistoriaClinica() == null (debe cargarse explícitamente)
```

**Borrado Lógico:**
```java
// No elimina físicamente, solo marca como eliminado
pacienteDao.eliminar(1L);
// SELECT ... WHERE eliminado = false (no aparece en listados)
```

**Prevención SQL Injection:**
```java
// Uso de PreparedStatement en todos los DAOs
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT * FROM paciente WHERE dni = ?"
);
pstmt.setString(1, dni);
```

### Métodos Específicos por Entidad

**PacienteDao:**
- `buscarPorDni(String dni)` - Búsqueda por documento único

**HistoriaClinicaDao:**
- `buscarPorPacienteId(Long id)` - Búsqueda por paciente asociado

## 📝 Uso del Sistema

### AppMenu (Consola Interactiva)

La aplicación principal `Main.MainApp` ejecuta el **AppMenu**, un menú de consola que permite operar el sistema de forma interactiva.

**Para iniciarlo:**
```bash
ant run
```
o desde NetBeans ejecutar `MainApp`.

**Funciones disponibles:**
- Crear paciente + historia clínica (transacción)
- Listar pacientes (incluye historias asociadas)
- Buscar paciente por ID
- Actualizar datos del paciente y su historia
- Eliminar paciente (borrado lógico)
- Buscar paciente por DNI (búsqueda específica requerida)
- Crear historia clínica para paciente existente
- Listar / buscar / actualizar / eliminar historias clínicas

**Características del menú:**
- Validación de entradas (números, fechas, campos obligatorios)
- IDs inexistentes y errores de BD manejados con mensajes claros
- Conversión de opciones a mayúsculas para evitar confusiones
- Confirmaciones de éxito o error en cada operación

### Ejemplo: Crear Paciente con Historia Clínica

```java
// 1. Crear instancia del servicio
PacienteService service = new PacienteService();

// 2. Crear el paciente
Paciente paciente = new Paciente();
paciente.setNombre("Juan");
paciente.setApellido("Pérez");
paciente.setDni("12345678");
paciente.setFechaNacimiento(LocalDate.of(1990, 5, 15));

// 3. Crear historia clínica
HistoriaClinica hc = new HistoriaClinica();
hc.setNroHistoria("HC-001");
hc.setGrupoSanguineo(GrupoSanguineo.AP);
hc.setAntecedentes("Ninguno");

// 4. Asociar y guardar (transacción automática)
paciente.setHistoriaClinica(hc);
Paciente resultado = service.insertarPacienteCompleto(paciente);

System.out.println("Paciente creado con ID: " + resultado.getId());
```

### Ejecutar Demo Completa

```bash
ant run
```

**El AppMenu permite:**
1. ✅ Test de conexión a MySQL
2. ✅ Creación y actualización de pacientes e historias clínicas
3. ✅ Búsqueda por ID y por DNI
4. ✅ Borrado lógico de ambas entidades
5. ✅ Manejo robusto de errores y validaciones

## 🔒 Validaciones Implementadas

### A Nivel de Base de Datos
- DNI único (restricción UNIQUE)
- Número de historia único
- Relación 1:1 estricta (un paciente = una historia)
- Integridad referencial con ON DELETE RESTRICT

### A Nivel de Servicio
- Validación de campos obligatorios
- Validación de longitud máxima
- Validación de formato de datos
- Manejo de excepciones personalizadas

## 🛠️ Solución de Problemas

### Error: "Duplicate entry for key 'dni'"
**Causa:** DNI ya existe en la base de datos  
**Solución:** El sistema genera DNI únicos automáticamente

### Error: "Cannot connect to database"
**Verificar:**
1. MySQL está corriendo en puerto 3307
2. Base de datos `clinica_db` existe
3. Credenciales en `DatabaseConnection.java` son correctas
4. Driver MySQL está en `lib/`

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Solución:** Descargar driver MySQL con `./download-mysql-driver.bat`

## 📚 Documentación Adicional

- **Diagrama de clases**: Ver archivo PDF del trabajo integrador
- **Script SQL**: `database/schema.sql`
- **Guía de implementación**: `docs/DAO_IMPLEMENTATION.md`

## 🎓 Conceptos Aplicados

- ✅ Patrón de diseño DAO
- ✅ Arquitectura en capas
- ✅ Separación de responsabilidades
- ✅ SOLID principles
- ✅ Manejo de transacciones
- ✅ Gestión de excepciones
- ✅ PreparedStatements
- ✅ Borrado lógico
- ✅ Lazy loading
- ✅ Validaciones de negocio

## 📄 Licencia

Proyecto académico - Universidad Tecnológica Nacional (UTN)  
Programación 2 - 2025

---

**Fecha:** Noviembre 2025  
**Materia:** Programación 2  
**Institución:** UTN - TUPAD

