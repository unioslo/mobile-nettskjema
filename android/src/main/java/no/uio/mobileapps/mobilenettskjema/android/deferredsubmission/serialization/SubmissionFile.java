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

import android.content.Intent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.encryption.DecryptionPipe;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.encryption.EncryptionMethod;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.encryption.EncryptionPipe;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.events.SubmissionStateChanged;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.filemanagement
        .InplaceFileTransformation;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.IntentSerializable;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.InitialSubmission;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.SubmissionFileState;

public class SubmissionFile implements IntentSerializable {
    private static final String PATH_INTENTKEY = InitialSubmission.class.getName() + ".filepath";
    private File file;

    public SubmissionFile(File file) {
        this.file = file;
    }

    public SubmissionFile(Intent intent) throws MobileNettskjemaException {
        this(new File(intent.getStringExtra(PATH_INTENTKEY)));
    }

    public SubmissionFile(JsonFile jsonFile) {
        this(jsonFile.storageFile());
    }

    @Override
    public void bundleWithIntent(Intent intent) {
        intent.putExtra(PATH_INTENTKEY, file.getAbsolutePath());
    }

    public void deleteStoredFile() throws MobileNettskjemaException {
        if (!file.delete()) throw new MobileNettskjemaException("Deleting file " + file.getAbsolutePath() + " failed");
    }

    public String contents() throws MobileNettskjemaException {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    public void encrypt(EncryptionMethod encryptionMethod) throws MobileNettskjemaException {
        new InplaceFileTransformation(file, new EncryptionPipe(encryptionMethod)).perform();
    }

    public void decrypt(EncryptionMethod encryptionMethod) throws MobileNettskjemaException {
        new InplaceFileTransformation(file, new DecryptionPipe(encryptionMethod)).perform();
    }

    public void markAs(SubmissionFileState submissionFileState) throws MobileNettskjemaException {
        String identifier = submissionFileState.extension();
        File newFile = new File(FilenameUtils.removeExtension(file.getAbsolutePath()) + "." + identifier);
        if (!file.renameTo(newFile)) throw new MobileNettskjemaException("Renaming file " + file.getAbsolutePath() + " to " + newFile.getAbsolutePath() + "failed");
        this.file = newFile;
        EventBus.getDefault().post(new SubmissionStateChanged(submissionFileState));
    }


    public boolean isMarked(SubmissionFileState submissionFileState) {
        return FilenameUtils.getExtension(file.getAbsolutePath()).equals(submissionFileState.extension());
    }
}
