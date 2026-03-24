// Copyright (c) 2026 XtraCube
#ifndef FUSIONCORE_JAVA_UTILS_H
#define FUSIONCORE_JAVA_UTILS_H

#define GET_JAVA_STRING(javaString, assignment) \
    do { \
        const char *chars = (env)->GetStringUTFChars(javaString, nullptr); \
        assignment = chars; \
        (env)->ReleaseStringUTFChars(javaString, chars); \
    } while(0);

#endif //FUSIONCORE_JAVA_UTILS_H
