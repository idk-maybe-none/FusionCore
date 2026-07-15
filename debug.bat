@echo off
setlocal EnableDelayedExpansion

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
        start "Logcat PID %%a" cmd /c "adb logcat --pid=%%a ^& echo. ^& echo Logcat for PID %%a ended. ^& pause"
    )
)
timeout /t 2 >nul
goto :wait