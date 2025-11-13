@echo off
echo ========================================
echo Compilando Proyecto Clinica
echo ========================================
echo.

REM Limpiar compilación anterior
if exist build rmdir /S /Q build
mkdir build\classes

echo [1/3] Compilando archivos Java...
javac -d build\classes -cp "lib\*" src\Models\*.java src\Config\*.java src\Dao\*.java src\Service\*.java src\Main\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] La compilacion fallo
    pause
    exit /b 1
)

echo [OK] Compilacion exitosa
echo.

echo [2/3] Ejecutando TestConnection...
java -cp "build\classes;lib\*" Main.TestConnection

echo.
echo ========================================
pause

