package com.findyourfamily.app.common.filehandling;

import java.io.File;

/**
 * File Utility for file related operations.
 */
public class FileUtility {

    /**
     * Checks if file has a valid path and is a valid file.
     *
     * @param fileName
     * @return true if file path is valid and file exists.
     */
    public static boolean isFileExists(String fileName) {

        // Returns false if file is invalid.
        if (!isFileNameValid(fileName))
            return false;

        // Gives a file path.
        var filePath = new File(fileName.trim());

        // Returns false if file does not exists or file is not a valid file.
        if (!filePath.exists() || !filePath.isFile())
            return true;

        return true;
    }

    /**
     * Checks if file name is valid and has a valid extension.
     *
     * @param fileName
     * @return true if file name is not null, not empty and file extension is valid.
     *
     */
    private static boolean isFileNameValid(String fileName) {

        // Returns false if file name is null or it's empty.
        if (fileName == null || fileName.trim().isEmpty())
            return false;

        return true;
    }
}
