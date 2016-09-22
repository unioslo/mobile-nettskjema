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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.Pipe;

public class EncryptionPipe implements Pipe<File, File> {
    private final EncryptionMethod encryptionMethod;

    public EncryptionPipe(EncryptionMethod encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }

    public void connect(File inputFile, File outputFile) throws MobileNettskjemaException {
        try {
            InputStream fileInputStream = new FileInputStream(inputFile);
            OutputStream fileOutputStream = new FileOutputStream(outputFile);
            OutputStream cipherOutputStream = encryptionMethod.newCipherOutputStream(outputFile, new FileEntity(inputFile));
            new StreamPipe().connect(fileInputStream, cipherOutputStream);
            cipherOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            throw new MobileNettskjemaException(e);
        }

    }
}
