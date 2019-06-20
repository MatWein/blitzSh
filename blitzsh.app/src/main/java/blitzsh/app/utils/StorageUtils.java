package blitzsh.app.utils;

import blitzsh.app.common.model.enums.WorkDir;

import java.io.File;

public class StorageUtils {
    public static File getOrCreateWorkDir(WorkDir workDir) {
        File currentDir = getCurrentDir();
        File subDir = new File(currentDir, workDir.getDirName());

        if (subDir.isDirectory()) {
            return subDir;
        } else {
            if (subDir.mkdirs()) {
                return subDir;
            } else {
                return getTempDir();
            }
        }
    }

    public static File getCurrentDir() {
        return new File(System.getProperty("user.dir"));
    }

    public static File getTempDir() {
        return new File(System.getProperty("java.io.tmpdir"));
    }
}
