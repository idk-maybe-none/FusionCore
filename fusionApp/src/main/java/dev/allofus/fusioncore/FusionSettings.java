package dev.allofus.fusioncore;

import android.content.Context;
import android.content.SharedPreferences;

public final class FusionSettings {
    private static final String PREFS_NAME = "fusion_settings";
    private static final String KEY_DOWNLOAD_UNSTRIPPED_LIBUNITY = "download_unstripped_libunity";
    private static final String KEY_USE_EXPERIMENTAL_DOWNLOADER = "use_experimental_libunity_downloader";

    private FusionSettings() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // use when non-f1 unity build
    public static boolean isDownloadUnstrippedLibUnity(Context context) {
        return prefs(context).getBoolean(KEY_DOWNLOAD_UNSTRIPPED_LIBUNITY, true);
    }

    public static void setDownloadUnstrippedLibUnity(Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_DOWNLOAD_UNSTRIPPED_LIBUNITY, enabled).apply();
    }

    public static boolean isUseExperimentalDownloader(Context context) {
        return prefs(context).getBoolean(KEY_USE_EXPERIMENTAL_DOWNLOADER, false);
    }

    public static void setUseExperimentalDownloader(Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_USE_EXPERIMENTAL_DOWNLOADER, enabled).apply();
    }
}
