package dev.allofus.fusioncore;

public class FusionConfig {

    public FusionConfig(String gameLibDir, String appLibDir) {
        this.gameLibraryDirectory = gameLibDir;
        this.appLibraryDirectory = appLibDir;
    }

    public String appLibraryDirectory;
    public String gameLibraryDirectory;
}
