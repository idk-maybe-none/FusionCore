@echo off
:wait
for /f "tokens=2" %%a in ('adb shell ps ^| findstr dev.allofus.fusioncoredev') do (
    cls
    echo Found PID %%a
    adb logcat --pid=%%a
)
goto :wait