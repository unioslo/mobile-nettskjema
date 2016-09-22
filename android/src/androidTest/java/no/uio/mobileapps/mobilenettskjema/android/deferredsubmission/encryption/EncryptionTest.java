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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.encryption;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class EncryptionTest {

    private static final File TEST_DATA_FOLDER = context().getFileStreamPath("testdata");
    private static final File ORIGINAL_FILE = new File(TEST_DATA_FOLDER.getAbsolutePath() + "/plaintext");
    private static final File ENCRYPTED_FILE = new File(TEST_DATA_FOLDER.getAbsolutePath() + "/plaintext.encrypted");
    private static final File DECRYPTED_FILE = new File(TEST_DATA_FOLDER.getAbsolutePath() + "/plaintext.decrypted");

    private static final String LINE_CONTENT = "test content";


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void prepareFiles() throws IOException {
        TEST_DATA_FOLDER.mkdir();
        for (File file: TEST_DATA_FOLDER.listFiles()) {
            file.delete();
        }
        PrintWriter writer = new PrintWriter(ORIGINAL_FILE);
        writer.println("1" + LINE_CONTENT);
        writer.println("2" + LINE_CONTENT);
        writer.println("3" + LINE_CONTENT);
        writer.close();
    }

    @After
    public void cleanUp() throws IOException {
        FileUtils.deleteDirectory(TEST_DATA_FOLDER);
    }

    @Test
    public void decryptedFileIsEqualToOriginal() throws Exception {
        EncryptionMethod method = new EncryptionMethod(context());

        new EncryptionPipe(method).connect(ORIGINAL_FILE, ENCRYPTED_FILE);
        new DecryptionPipe(method).connect(ENCRYPTED_FILE, DECRYPTED_FILE);

        assertThat(FileUtils.contentEquals(ORIGINAL_FILE, ENCRYPTED_FILE), is(false));
        assertThat(FileUtils.contentEquals(ORIGINAL_FILE, DECRYPTED_FILE), is(true));
    }

    private static Context context() {
        return InstrumentationRegistry.getContext();
    }

}
