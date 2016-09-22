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

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;

public class EncryptionMethod {

    private final Crypto crypto;

    public EncryptionMethod(Context context) throws MobileNettskjemaException {
        this.crypto = AndroidConceal.get().createDefaultCrypto(new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256));
    }

    OutputStream newCipherOutputStream(File file, FileEntity fileEntity) throws MobileNettskjemaException {
        try {
            return crypto.getCipherOutputStream(new FileOutputStream(file), fileEntity.entity());
        } catch (IOException | CryptoInitializationException | KeyChainException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    InputStream newCipherInputStream(File file,  FileEntity fileEntity) throws MobileNettskjemaException {
        try {
            return crypto.getCipherInputStream(new FileInputStream(file), fileEntity.entity());
        } catch (IOException | CryptoInitializationException | KeyChainException e) {
            throw new MobileNettskjemaException(e);
        }
    }
}
