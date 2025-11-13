#!/bin/bash
# Script para descargar el driver MySQL JDBC

echo "Descargando MySQL Connector/J..."

# Crear directorio lib si no existe
mkdir -p lib

# Descargar el driver MySQL
curl -L -o lib/mysql-connector-j-8.2.0.jar \
  https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar

if [ $? -eq 0 ]; then
    echo "✓ Driver MySQL descargado exitosamente en: lib/mysql-connector-j-8.2.0.jar"
    echo ""
    echo "Ahora puedes compilar el proyecto con: ant compile"
else
    echo "✗ Error al descargar el driver"
    echo "Por favor descárgalo manualmente desde:"
    echo "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar"
fi

