package dev.allofus.fusioncore;

public class FusionConfig {

    public FusionConfig(
            String gameLibDir,
            String appLibDir,
            String gameDataDir,
            String appDataDir,
            String bepInExDir,
            boolean useOriginalLibUnity
    ) {
        this.gameLibraryDirectory = gameLibDir;
        this.appLibraryDirectory = appLibDir;
        this.gameDataDirectory = gameDataDir;
        this.appDataDirectory = appDataDir;
        this.bepInExDirectory = bepInExDir;
        this.useOriginalLibUnity = useOriginalLibUnity;
    }

    /// The directory where Fusion's native libraries are located.
    public String appLibraryDirectory;

    /// The directory where the game's native libraries are located.
    public String gameLibraryDirectory;

    /// The directory where Fusion's data files are located.
    public String appDataDirectory;

    /// The directory where the game's data files are located.
    public String gameDataDirectory;

    /// The directory where BepInEx should be installed.
    public String bepInExDirectory;

    /// Whether to use the original libunity.so from the game or the one provided by Fusion.
    public boolean useOriginalLibUnity;
}
