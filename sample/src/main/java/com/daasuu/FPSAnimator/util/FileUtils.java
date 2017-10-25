package com.daasuu.FPSAnimator.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by shotasaitou on 2017/10/25.
 */
public class FileUtils {

    public static File externalDir(String dirname, String filename) {
        return new FileUtils(dirname, filename).file();
    }

    private final String dirname, filename;

    private FileUtils(String dirname, String filename) {
        this.dirname = dirname;
        this.filename = filename;
    }

    private String filePath() {
        return file().getAbsolutePath();
    }

    private File file() {
        //noinspection ResultOfMethodCallIgnored
        dir().mkdir();
        return new File(dir(), filename);
    }

    private File dir() {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), dirname);
    }
}
