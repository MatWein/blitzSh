package blitzsh.app.common.model.enums;

public enum WorkDir {
    HELP("help"),
    SETTINGS("settings");

    private final String dirName;

    WorkDir(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }
}
