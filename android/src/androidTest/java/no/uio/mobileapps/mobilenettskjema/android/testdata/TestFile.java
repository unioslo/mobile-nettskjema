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
package no.uio.mobileapps.mobilenettskjema.android.testdata;

import android.support.test.InstrumentationRegistry;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TestFile {
    private final File file;
    private final int kbSize;

    public TestFile(int kbSize) {
        this.file = InstrumentationRegistry.getContext().getFileStreamPath(kbSize + "kbFile");
        this.kbSize = kbSize;
    }

    public File randomContent() throws IOException {
        RandomAccessFile f = new RandomAccessFile(file, "rw");
        f.setLength(1024 * kbSize);
        f.close();
        return file;
    }

    public void delete() throws IOException {
        if (!file.delete()) throw new IOException("Could not delete file");
    }
}
