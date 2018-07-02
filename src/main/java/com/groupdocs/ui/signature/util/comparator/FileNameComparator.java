package com.groupdocs.ui.signature.util.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * Compare and sort files by name
 */
public class FileNameComparator implements Comparator<File> {

    @Override
    public int compare(File file1, File file2) {

        return String.CASE_INSENSITIVE_ORDER.compare(file1.getName(),
                file2.getName());
    }
}
