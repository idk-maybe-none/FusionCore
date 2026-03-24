// Copyright (c) 2026 XtraCube
#ifndef FUSION_FUSION_CONFIG_H
#define FUSION_FUSION_CONFIG_H
#include <string>
#include <jni.h>

class FusionConfig {
private:
    std::string gameLibraryDirectory;
    std::string appLibraryDirectory;

public:
    FusionConfig(JNIEnv *env, jobject jFusionConfig);

    [[nodiscard]] const std::string &getGameLibraryDirectory() const {
        return const_cast<std::string&>(gameLibraryDirectory);
    }

    [[nodiscard]] const std::string &getAppLibraryDirectory() const {
        return const_cast<std::string&>(appLibraryDirectory);
    }
};

#endif //FUSION_FUSION_CONFIG_H
