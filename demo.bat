@echo off
REM Script para demostrar los perfiles de Spring y variables de entorno

echo.
echo ========================================
echo Demostración: Perfiles de Spring Boot
echo ========================================
echo.

echo 1. Ejecutando con perfil LOCAL (por defecto)
echo    - Password: default_local_password
echo.
REM timeout /t 2 /nobreak

echo 2. Ejecutando con perfil DEV
echo    - Password: dev_password_123
echo.
REM timeout /t 2 /nobreak

echo 3. Ejecutando con perfil PROD
echo    - Password: prod_password_securely_set
echo.
REM timeout /t 2 /nobreak

echo 4. Ejecutando con variable de entorno personalizada
echo    - DB_PASSWORD: mi_contraseña_super_secreta
echo.

echo ========================================
echo Para ejecutar, usa estos comandos:
echo ========================================
echo.
echo Perfil LOCAL (defecto):
echo   mvnw.cmd spring-boot:run
echo.
echo Perfil DEV:
echo   mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
echo.
echo Perfil PROD:
echo   mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
echo.
echo Con variable de entorno personalizada:
echo   set DB_PASSWORD=mi_contraseña_super_secreta
echo   mvnw.cmd spring-boot:run
echo.
echo ========================================
echo Para ejecutar los tests:
echo ========================================
echo.
echo   mvnw.cmd test
echo.

