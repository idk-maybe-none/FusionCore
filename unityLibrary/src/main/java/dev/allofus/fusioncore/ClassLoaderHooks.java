package dev.allofus.fusioncore;

import android.util.Log;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;

public class ClassLoaderHooks {
    public static void installHooks(ClassLoader gameClassLoader) {

        Method loadClassMethod;
        try {
            loadClassMethod = ClassLoader.class.getDeclaredMethod("loadClass", String.class, boolean.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find loadClass method in ClassLoader", e);
        }

        Pine.hook(loadClassMethod, new MethodHook() {
            @Override
            public void afterCall(Pine.CallFrame callFrame) {
                try {
                    // Only attempt fallback if class was not found
                    if (callFrame.getThrowable() instanceof ClassNotFoundException) {
                        String className = (String) callFrame.args[0];
                        Log.d("ClassLoaderHooks", "afterLoadClass: class not found in default loader: " + className);

                        // Skip fallback if this is already the game classloader
                        if (callFrame.thisObject == gameClassLoader) {
                            Log.d("ClassLoaderHooks", "Skipping fallback: loader is already game classloader");
                            return;
                        }

                        // Attempt to load from game classloader, but don't let exceptions propagate to JNI
                        try {
                            Class<?> gameClass = gameClassLoader.loadClass(className);
                            callFrame.setResult(gameClass);
                            Log.d("ClassLoaderHooks", "Successfully loaded from game classloader: " + className);
                        } catch (ClassNotFoundException e) {
                            Log.d("ClassLoaderHooks", "Class not found in game classloader either: " + className);
                        } catch (Exception e) {
                            // Catch any other exceptions to prevent them from propagating to JNI
                            Log.w("ClassLoaderHooks", "Unexpected error loading class from game classloader: " + className, e);
                        }
                    }
                } catch (Exception e) {
                    Log.e("ClassLoaderHooks", "Unexpected error in afterCall (CRITICAL)", e);
                }
            }
        });
    }
}
