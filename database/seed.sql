-- SEED DE DATOS DE EJEMPLO
-- Sistema: Pacientes + Historias Clínicas (Grupo 49)

-- 1) Limpieza de tablas (opcional pero recomendado para el TPI)
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE historia_clinica;
TRUNCATE TABLE paciente;

SET FOREIGN_KEY_CHECKS = 1;

-- 2) Inserción de Pacientes
--    Campos: nombre, apellido, dni, fecha_nacimiento, eliminado

INSERT INTO paciente (nombre, apellido, dni, fecha_nacimiento, eliminado) VALUES
  ('Ricardo',  'Darin',       '14000001', '1957-01-16', FALSE),  -- Actor argentino
  ('Natalia',  'Oreiro',      '20000002', '1977-05-19', FALSE),  -- Actriz / cantante uruguaya-argentina
  ('Leonardo', 'DiCaprio',    '30000003', '1974-11-11', FALSE),  -- Actor estadounidense
  ('Scarlett', 'Johansson',   '32000004', '1984-11-22', FALSE),  -- Actriz estadounidense
  ('Robert',   'Downey',      '33000005', '1965-04-04', FALSE);  -- Actor estadounidense

-- 3) Inserción de Historias Clínicas
--    Campos: eliminado, nro_historia, grupo_sanguineo, antecedentes,
--            medicacion_actual, observaciones, paciente_id

INSERT INTO historia_clinica (
    eliminado,
    nro_historia,
    grupo_sanguineo,
    antecedentes,
    medicacion_actual,
    observaciones,
    paciente_id
) VALUES
  (
    FALSE,
    'HC-0001',
    'AP',  -- A positivo
    'Hipertensión arterial leve. Antecedente familiar de cardiopatías.',
    'Enalapril 10 mg/día.',
    'Se sugiere control clínico y cardiológico anual.',
    1      -- Paciente: Ricardo Darin
  ),
  (
    FALSE,
    'HC-0002',
    'OM',  -- 0 negativo
    'Sin antecedentes patológicos relevantes. No refiere alergias conocidas.',
    'No recibe medicación crónica.',
    'Se recomienda control clínico cada 2 años.',
    2      -- Paciente: Natalia Oreiro
  ),
  (
    FALSE,
    'HC-0003',
    'BP',  -- B positivo
    'Hipercolesterolemia diagnosticada hace 5 años. Episodios previos de gastritis.',
    'Atorvastatina 20 mg/día.',
    'Buena adherencia al tratamiento. Se sugiere control de laboratorio cada 6 meses.',
    3      -- Paciente: Leonardo DiCaprio
  ),
  (
    FALSE,
    'HC-0004',
    'ABP', -- AB positivo
    'Rinitis alérgica estacional desde la adolescencia.',
    'Antihistamínicos orales a demanda en épocas de primavera.',
    'Se recomienda evitar exposición a alérgenos frecuentes y evaluar inmunoterapia específica.',
    4      -- Paciente: Scarlett Johansson
  ),
  (
    TRUE,
    'HC-0005',
    'AM',  -- A negativo
    'Antecedente de tabaquismo intenso. Exfumador desde 2010. Controles previos de función pulmonar.',
    'No recibe medicación crónica al momento de la última consulta.',
    'Historia clínica marcada como inactiva a efectos de pruebas de baja lógica y filtrado en listados.',
    5      -- Paciente: Robert Downey (ejemplo con eliminado = TRUE)
  );

-- FIN DE SEED
