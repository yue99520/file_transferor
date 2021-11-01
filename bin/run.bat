@echo off
TITLE FileTransferor Service
setlocal enabledelayedexpansion

if "%OS%"=="Windows_NT" goto nt
echo This script only works with NT-based versions of Windows.
goto :eof

:nt

IF DEFINED JAVA11_HOME (
  SET JAVA_HOME=%JAVA11_HOME%
)

if NOT DEFINED JAVA_HOME (
    echo JAVA_HOME environment variable must be set!
    goto :eof
)

set FILE_TRANSFEROR_HOME=%CD%
set CLASS_PATH="%FILE_TRANSFEROR_HOME%/config";"%FILE_TRANSFEROR_HOME%/lib/*"
set BOOT_JAR="%FILE_TRANSFEROR_HOME%\boot\file-transferor-boot.jar"
set JVM_OPT=-server -Xms256m -Xmx256m -Xmn512m^
    -XX:MetaspaceSize=48m^
    -XX:MaxMetaspaceSize=256m^
    -XX:-OmitStackTraceInFastThrow


"%JAVA_HOME%\bin\java" -cp %CLASS_PATH% -jar %BOOT_JAR%