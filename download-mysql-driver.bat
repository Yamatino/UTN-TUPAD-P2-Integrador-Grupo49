@echo off
REM Script para descargar el driver MySQL JDBC en Windows

echo Descargando MySQL Connector/J...

REM Crear directorio lib si no existe
if not exist lib mkdir lib

REM Descargar usando PowerShell
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar' -OutFile 'lib/mysql-connector-j-8.2.0.jar'}"

if %ERRORLEVEL% EQU 0 (
    echo [OK] Driver MySQL descargado exitosamente en: lib\mysql-connector-j-8.2.0.jar
    echo.
    echo Ahora puedes compilar el proyecto con: ant compile
) else (
    echo [ERROR] No se pudo descargar el driver
    echo Por favor descargalo manualmente desde:
    echo https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar
    echo Y colocalo en la carpeta lib\
)

pause

