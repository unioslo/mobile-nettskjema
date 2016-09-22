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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.filemanagement
        .InplaceFileTransformation;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.Pipe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class InplaceFileTransformationTest {
    private final File testFile = new File("testing.testFile");

    @Before
    public void createTestFile() throws Exception {
        testFile.createNewFile();
    }

    @After
    public void deleteTestFile() throws Exception {
        testFile.delete();
    }

    @Test
    public void perform() throws Exception {
        new InplaceFileTransformation(testFile, new Pipe<File, File>() {
            @Override
            public void connect(File input, File output) throws MobileNettskjemaException {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                    throw new MobileNettskjemaException(e);
                }
            }
        }).perform();
        assertThat(testFile.exists(), is(true));
        assertThatNoTemporaryFilesExist();
    }

    private void assertThatNoTemporaryFilesExist() {
        assertThat(FileUtils.listFiles(new File("."), new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return matchingFileName(file.getName());
            }

            @Override
            public boolean accept(File dir, String name) {
                return matchingFileName(name);
            }

            private boolean matchingFileName(String name) {
                return name.contains(testFile.getName());
            }
        }, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return false;
            }

            @Override
            public boolean accept(File dir, String name) {
                return false;
            }
        }).size(), is(1));
    }

}