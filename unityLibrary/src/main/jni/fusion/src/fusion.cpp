// Copyright (c) 2026 XtraCube
#include <android/log.h>
#include <jni.h>
#include <filesystem>

#include <libmain.h>
#include <fusion_config.h>

extern "C" JNIEXPORT void JNICALL loadFusion(
        JNIEnv *env,
        jclass thisObject,
        jobject nativeConfig
)
{
    __android_log_print(ANDROID_LOG_INFO, "Fusion", "loadFusion called");

    FusionConfig config(env, nativeConfig);

    std::filesystem::path gameLibsPath(config.getGameLibraryDirectory());
    std::filesystem::path appLibsPath(config.getAppLibraryDirectory());

    std::filesystem::path libil2cpp = gameLibsPath / "libil2cpp.so";
    std::filesystem::path libunity = gameLibsPath / "libunity.so";

    set_override_il2cpp_path(libil2cpp.c_str());
    set_override_unity_path(libunity.c_str());

    __android_log_print(ANDROID_LOG_INFO, "Fusion", "Game library directory: %s", config.getGameLibraryDirectory().c_str());
    __android_log_print(ANDROID_LOG_INFO, "Fusion", "App library directory: %s", config.getAppLibraryDirectory().c_str());
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *globalEnv;
    if (vm->GetEnv(reinterpret_cast<void**>(&globalEnv), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // Failed to obtain JNIEnv
    }

    jclass clazz = globalEnv->FindClass("dev/allofus/fusioncore/ActivityBridge");
    if (!clazz) {
        return JNI_ERR; // Class not found
    }

    static const JNINativeMethod methods[] = {
            {"loadFusion", "(Ldev/allofus/fusioncore/FusionConfig;)V",
                    reinterpret_cast<void *>(loadFusion)}
    };

    jint ret = globalEnv->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(JNINativeMethod));
    if (ret != JNI_OK) {
        return ret; // Failed to register natives
    }

    return JNI_VERSION_1_6; // Successful initialization
}
