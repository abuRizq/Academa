@echo off
@setlocal

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "WRAPPER_JAR=%~dp0.mvn\wrapper\maven-wrapper.jar"

@REM Find java.exe
set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: JAVA_HOME is not set and no 'java' command could be found in your PATH.
    exit /b 1
)

"%JAVA_EXE%" -D"maven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%." -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
