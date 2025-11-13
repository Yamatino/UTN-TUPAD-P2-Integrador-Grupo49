-- ================================================================
-- Script de Creación de Base de Datos - Sistema Clínico
-- Base de Datos: clinica_db
-- Motor: MySQL 5.7+
-- ================================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS clinica_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE clinica_db;

-- ================================================================
-- Tabla: paciente
-- Descripción: Almacena información de los pacientes
-- ================================================================
CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT false,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    fecha_nacimiento DATE,
    
    -- Índices para mejorar el rendimiento
    INDEX idx_dni (dni),
    INDEX idx_eliminado (eliminado),
    INDEX idx_nombre_apellido (nombre, apellido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: historia_clinica
-- Descripción: Almacena las historias clínicas de los pacientes
-- Relación: 1-1 con paciente (un paciente tiene una historia clínica)
-- ================================================================
CREATE TABLE IF NOT EXISTS historia_clinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT false,
    nro_historia VARCHAR(20) UNIQUE,
    grupo_sanguineo ENUM('AP', 'AM', 'BP', 'BM', 'ABP', 'ABM', 'OP', 'OM') NOT NULL,
    antecedentes TEXT,
    medicacion_actual TEXT,
    observaciones TEXT,
    paciente_id BIGINT NOT NULL UNIQUE,
    
    -- Clave foránea con restricción de integridad referencial
    CONSTRAINT fk_historia_paciente 
        FOREIGN KEY (paciente_id) 
        REFERENCES paciente(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    
    -- Índices
    INDEX idx_nro_historia (nro_historia),
    INDEX idx_eliminado (eliminado),
    INDEX idx_paciente_id (paciente_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Datos de prueba (OPCIONAL - comentar si no se necesitan)
-- ================================================================

-- Insertar un paciente de ejemplo
-- INSERT INTO paciente (nombre, apellido, dni, fecha_nacimiento, eliminado) 
-- VALUES ('Juan', 'Pérez', '12345678', '1990-05-15', false);

-- Insertar una historia clínica de ejemplo
-- INSERT INTO historia_clinica (nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id, eliminado)
-- VALUES ('HC-001', 'AP', 'Ninguno', 'Ninguna', 'Paciente en buen estado general', 1, false);

-- ================================================================
-- Verificación de las tablas creadas
-- ================================================================
SHOW TABLES;

-- Ver estructura de la tabla paciente
-- DESCRIBE paciente;

-- Ver estructura de la tabla historia_clinica
-- DESCRIBE historia_clinica;

