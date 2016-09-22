/*
 * Copyright (c) 2016, University of Oslo, Norway All rights reserved.
 * 
 * This file is part of "UiO Software Information Inventory".
 * 
 * "UiO Software Information Inventory" is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * "UiO Software Information Inventory" is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with "UiO Software Information Inventory". If
 * not, see <http://www.gnu.org/licenses/>
 */
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.filemanagement;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;

public class TemporaryFile {
    private static final String extension = "queueTemp";
    private final String originalPath;
    private final File file;

    public static boolean isTemporary(File aFile) {
        return FilenameUtils.getExtension(aFile.getName()).equals(extension);
    }

    TemporaryFile(File originalFile) {
        this.originalPath = originalFile.getAbsolutePath();
        this.file = new File(originalPath + "." + extension);
    }

    void replaceOriginalFile() throws MobileNettskjemaException {
        if (!file.renameTo(new File(originalPath))) throw new MobileNettskjemaException("Renaming file " + file.getAbsolutePath() + " to " + originalPath + " failed");
    }

    File file() {
        return file;
    }

}
