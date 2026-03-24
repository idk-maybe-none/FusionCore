# Fusion Core

Unity modding framework for Android IL2CPP games.

## Configuration

1. Replace `unity-classes.jar` in the unityLibrary/libs folder with the `classes.jar` from the Unity version used by the target game.
You can find it in the `{Unity Install Path}\Editor\Data\PlaybackEngines\AndroidPlayer\Variations\il2cpp\Release\Classes` folder.
2. Replace the `TARGET_GAME` String in `UnityPlayerActivity` with the package name of the target game.
3. Add/replace a package query in `AndroidManifest.xml` with the package name of the target game.
(This is required since new Android versions require explicit package queries to access other apps' data.)
4. Fix any compiling issues due to Unity version differences.
5. Attempt to run the app and fix runtime issues.