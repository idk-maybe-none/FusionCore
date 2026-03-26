package dev.allofus.fusioncore;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;

public class UnityPlayerHooks {

    public static final String[] UnityPlayerClassNames = new String[] {
            "com.unity3d.player.UnityPlayer",
            "com.unity3d.player.UnityPlayerForActivityOrService"
    };

    public static void installHooks(Context gameContext) {
        var classLoader = BootstrapActivity.class.getClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException("ClassLoader is null");
        }

        // get constructor
        Constructor<?> constructor = null;
        Class<?> unityPlayerClass = null;
        for (String className : UnityPlayerClassNames) {
            try {
                unityPlayerClass = classLoader.loadClass(className);
                for (Constructor<?> ctor : unityPlayerClass.getDeclaredConstructors()) {
                    if (ctor.getParameterTypes().length == 2 &&
                            Context.class.isAssignableFrom(ctor.getParameterTypes()[0])) {
                        constructor = ctor;
                        break;
                    }
                }

                constructor.setAccessible(true);
                break;
            } catch (ClassNotFoundException e) {
                // Try next class name
            }
        }

        if (constructor == null) {
            throw new IllegalStateException("Failed to find UnityPlayer constructor");
        }

        ArrayList<Field> activityFields = new ArrayList<>();
        for (Field field : unityPlayerClass.getDeclaredFields()) {
            if (Activity.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                activityFields.add(field);
                break;
            }
        }

        Pine.hook(constructor, new MethodHook() {
            Activity activity = null;

            @Override
            public void beforeCall(Pine.CallFrame callFrame) {
                activity = (Activity) callFrame.args[1];
                callFrame.args[0] = new CustomContextWrapper(gameContext, activity, activity);
            }

            @Override
            public void afterCall(Pine.CallFrame callFrame) {
                for (Field field : activityFields) {
                    try {
                        field.set(callFrame.thisObject, activity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to set activity field: " + field.getName(), e);
                    }
                }
            }
        });
    }
}
