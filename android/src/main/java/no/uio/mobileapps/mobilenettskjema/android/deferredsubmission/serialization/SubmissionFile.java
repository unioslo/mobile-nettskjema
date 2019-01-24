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
import org.json.JSONObject;
import org.json.JSONStringer;

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
    private static final String PATH_METAKEY = InitialSubmission.class.getName() + "-metadata.filepath";
    private File file;
    private File metaDataFile;

    private String metaData;

    public SubmissionFile(File file) {
        this.file = file;
    }

    public SubmissionFile(File file, File metadata) {
        this.file = file;
        this.metaDataFile = metadata;
        try {
            this.metaData = metaDataContents();
        } catch (MobileNettskjemaException e) {
            e.printStackTrace();
        }
    }

    public SubmissionFile(Intent intent) throws MobileNettskjemaException {
        this(new File(intent.getStringExtra(PATH_INTENTKEY)), new File(intent.getStringExtra(PATH_METAKEY)));
    }

    public SubmissionFile(JsonFile jsonFile) {
        this(jsonFile.storageFile());
    }

    public SubmissionFile(JsonFile jsonFile, File metaDataFile) {
        this(jsonFile.storageFile(), metaDataFile);
    }

    @Override
    public void bundleWithIntent(Intent intent) {
        intent.putExtra(PATH_INTENTKEY, file.getAbsolutePath());
        intent.putExtra(PATH_METAKEY, metaDataFile == null ? "no metadata" : metaDataFile.getAbsolutePath() );
    }

    public void deleteStoredFile() throws MobileNettskjemaException {
        if (!file.delete()) throw new MobileNettskjemaException("Deleting file " + file.getAbsolutePath() + " failed");
        if (!metaDataFile.delete()) throw new MobileNettskjemaException("Deleting file " + metaDataFile.getAbsolutePath() + " failed");
    }

    public String contents() throws MobileNettskjemaException {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    public String metaDataContents() throws MobileNettskjemaException {
        if(metaDataFile != null && metaDataFile.exists()) {
            try {
                return FileUtils.readFileToString(metaDataFile, "UTF-8");
            } catch (IOException e) {
                throw new MobileNettskjemaException(e);
            }
        }
        return "no metadata";
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

    public void updateMetaData(SubmissionFileState submissionFileState) throws MobileNettskjemaException {
        File newFile = new File(FilenameUtils.removeExtension(file.getAbsolutePath()) + ".metadata");
        /* TODO: add info about delivery in metadatafile */
        this.file = newFile;
        EventBus.getDefault().post(new SubmissionStateChanged(submissionFileState));
    }


    public boolean isMarked(SubmissionFileState submissionFileState) {
        return FilenameUtils.getExtension(file.getAbsolutePath()).equals(submissionFileState.extension());
    }
}
