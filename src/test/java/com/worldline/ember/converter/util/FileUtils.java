package com.worldline.ember.converter.util;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Some methods are from FileUtils (apache-commons 2.4 library)
 */
public class FileUtils {

    public static boolean areIdentical(File file1, File file2) {

        checkArgument(file1 != null && file2 != null);
        checkArgument(file1.exists() && file2.exists());
        checkArgument(file1.canRead() && file2.canRead());
        checkArgument(file1.isFile() && file2.isFile());

        Boolean areIdentical = true;
        Scanner scanFile1 = null;
        Scanner scanFile2 = null;
        try {
            scanFile1 = new Scanner(file1);
            scanFile2 = new Scanner(file2);
        } catch (IOException e) {
            Throwables.propagate(e);
        }

        while (scanFile1.hasNext() && scanFile2.hasNext()) {
            String s1 = scanFile1.nextLine();
            String s2 = scanFile2.nextLine();
            if (!s1.equals(s2)) {
                areIdentical = false;
            }
        }

        if (areIdentical) {
            areIdentical = scanFile1.hasNext() == scanFile2.hasNext();
        }

        scanFile1.close();
        scanFile2.close();

        return areIdentical;
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (File.separatorChar == '\\') {
            return false;
        }
        File fileInCanonicalDir;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

}
