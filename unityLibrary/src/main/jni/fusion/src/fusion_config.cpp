// Copyright (c) 2026 XtraCube
#include <fusion_config.h>
#include <java_utils.h>

FusionConfig::FusionConfig(JNIEnv *env, jobject jFusionConfig)
{
    jclass configClass = env->GetObjectClass(jFusionConfig);

    jstring gameLibsJString = (jstring) env->GetObjectField(jFusionConfig,
                                                            env->GetFieldID(configClass,
                                                                            "gameLibraryDirectory",
                                                                            "Ljava/lang/String;"));
    jstring appLibsJString = (jstring) env->GetObjectField(jFusionConfig,
                                                           env->GetFieldID(configClass,
                                                                           "appLibraryDirectory",
                                                                           "Ljava/lang/String;"));

    GET_JAVA_STRING(gameLibsJString, gameLibraryDirectory)
    GET_JAVA_STRING(appLibsJString, appLibraryDirectory)
}