@echo off
echo ========================================
echo Compilation de Pac-Max
echo ========================================

REM Créer le dossier de sortie
if not exist "bin" mkdir bin

echo.
echo Téléchargement des dépendances JavaFX...
echo.

REM Note: Ce script nécessite que JavaFX soit installé
REM Vous pouvez télécharger JavaFX depuis: https://openjfx.io/

echo ATTENTION: Ce projet nécessite Maven pour gérer les dépendances.
echo.
echo Pour installer Maven:
echo 1. Téléchargez Maven depuis https://maven.apache.org/download.cgi
echo 2. Extrayez l'archive
echo 3. Ajoutez le dossier bin de Maven au PATH
echo.
echo Puis lancez: mvn clean compile
echo             mvn javafx:run
echo.
pause
