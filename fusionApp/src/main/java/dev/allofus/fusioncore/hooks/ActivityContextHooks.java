package dev.allofus.fusioncore.hooks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dev.allofus.fusioncore.tools.CustomContextWrapper;
import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;

public class ActivityContextHooks {

    private static final String TAG = "ActivityContextHooks";

    public static boolean installAttachBaseContextHook(ClassLoader gameClassLoader, String launcherClassName,
            Context gameContext, Context fusionContext, String targetPackage, int themeId) {
        try {
            Class<?> launcherClass = Class.forName(launcherClassName, false, gameClassLoader);
            Method method = null;
            Class<?> clazz = launcherClass;
            while (clazz != null && method == null) {
                try {
                    method = clazz.getDeclaredMethod("attachBaseContext", Context.class);
                } catch (NoSuchMethodException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (method == null) {
                Log.w(TAG, "attachBaseContext not found in " + launcherClassName);
                return false;
            }
            method.setAccessible(true);

            final Class<?> targetClass = launcherClass;

            Pine.hook(method, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    if (!targetClass.isInstance(callFrame.thisObject)) {
                        return;
                    }
                    Context base = (Context) callFrame.args[0];
                    CustomContextWrapper wrapper = new CustomContextWrapper(
                            gameContext, fusionContext, base, targetPackage);
                    callFrame.args[0] = wrapper;
                }

                @Override
                public void afterCall(Pine.CallFrame callFrame) {
                    if (!targetClass.isInstance(callFrame.thisObject)) {
                        return;
                    }

                    try {
                        Activity activity = (Activity) callFrame.thisObject;
                        if (themeId != 0) {
                            activity.setTheme(themeId);
                            Log.i(TAG, "Applied game launcher theme (0x" +
                                    Integer.toHexString(themeId) + ") for " + targetPackage);
                        } else {
                            Log.w(TAG, "Could not resolve a theme for " + targetPackage);
                        }

                        // Also patch the Activity's mApplication field to point to the game's Application
                        // This prevents Firebase and other SDKs from getting the wrong Application instance
                        // try {
                        //     Field mApplicationField = Activity.class.getDeclaredField("mApplication");
                        //     mApplicationField.setAccessible(true);
                        //     Object currentApp = mApplicationField.get(activity);
                        //     if (currentApp != null) {
                        //         Context gameAppContext = gameContext.getApplicationContext();
                        //         if (gameAppContext instanceof android.app.Application) {
                        //             mApplicationField.set(activity, gameAppContext);
                        //             Log.i(TAG, "Patched Activity mApplication to game's Application");
                        //         }
                        //     }
                        // } catch (Exception e) {
                        //     Log.w(TAG, "Could not patch mApplication", e);
                        // }

                    } catch (Exception e) {
                        Log.w(TAG, "Could not set game theme dynamically", e);
                    }
                }
            });

            Log.i(TAG, "Installed attachBaseContext hook for " + launcherClassName);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to install attachBaseContext hook", e);
            return false;
        }
    }
}
