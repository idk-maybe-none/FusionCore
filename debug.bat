@echo off
setlocal EnableDelayedExpansion

if not exist "logs" mkdir "logs"

set "PIDS="

:wait
for /f "tokens=2" %%a in ('adb shell ps ^| findstr "dev.allofus.fusioncoredev"') do (
    set "PID=%%a"
    set "SEEN=0"

    for %%b in (!PIDS!) do (
        if "%%b"=="%%a" set "SEEN=1"
    )

    if !SEEN! == 0 (
        set "PIDS=!PIDS! %%a"
        for /f %%I in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set "TIMESTAMP=%%I"
        set "LOGFILE=%~dp0logs\%%a_!TIMESTAMP!.log"
        echo New process detected: PID %%a - logging to !LOGFILE!
        start "Logcat PID %%a" powershell -NoProfile -Command "adb logcat --pid=%%a 2>&1 | Tee-Object -FilePath '!LOGFILE!'"
    )
)
timeout /t 2 >nul
goto :wait