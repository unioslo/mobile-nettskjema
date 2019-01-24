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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates;

import android.content.Context;
import android.content.Intent;

import java.io.File;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.encryption.EncryptionMethod;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.ClassIdentifier;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions
        .SubmissionDecision;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions
        .SubmissionDecisionFromIntent;

public class EncryptedSubmission implements SubmissionState {
    private final SubmissionFile submissionFile;
    private final ClassIdentifier classIdentifier;
    private final SubmissionDecision submissionDecision;
    private int deliveryStatus;
    private String metaData;

    public EncryptedSubmission(SubmissionFile submissionFile, SubmissionDecision submissionDecision, int delivered) {
        this.submissionFile = submissionFile;
        this.classIdentifier = new ClassIdentifier(this);
        this.submissionDecision = submissionDecision;
        this.deliveryStatus = delivered;
    }

    public EncryptedSubmission(SubmissionFile submissionFile, SubmissionDecision submissionDecision, String metaData) {
        this.submissionFile = submissionFile;
        this.classIdentifier = new ClassIdentifier(this);
        this.submissionDecision = submissionDecision;
        this.metaData = metaData;
        this.deliveryStatus = 0;
    }

    public EncryptedSubmission(SubmissionFile submissionFile, SubmissionDecision submissionDecision) {
        this.submissionFile = submissionFile;
        this.classIdentifier = new ClassIdentifier(this);
        this.submissionDecision = submissionDecision;
        this.deliveryStatus = 0;
    }

    EncryptedSubmission(Intent intent)  throws MobileNettskjemaException {
        this(new SubmissionFile(intent), new SubmissionDecisionFromIntent(intent).deserialized(), "intent");
    }

    EncryptedSubmission(File file, SubmissionDecision submissionDecision, String metaData) throws MobileNettskjemaException {
        this(new SubmissionFile(file), submissionDecision, metaData);
    }

    @Override
    public void transformToState(Context context) throws MobileNettskjemaException {
        submissionFile.encrypt(new EncryptionMethod(context));
        submissionFile.markAs(SubmissionFileState.ENCRYPTED);
    }

    @Override
    public SubmissionState next(Context context) throws MobileNettskjemaException {
        if(/*TODO*/ true) {
            return new DecryptedSubmission(submissionFile, submissionDecision);
        } else {
            return new DeletedSubmission(submissionFile);
        }
    }

    @Override
    public boolean isEndOfProcessing() {
        return true;
    }

    @Override
    public boolean indicatesSemiPermanentStorageOnDevice() {
        return true;
    }

    @Override
    public String getSubmissionMetaData() {
        return this.metaData;
    }

    @Override
    public void bundleWithIntent(Intent intent) {
        classIdentifier.bundleWithIntent(intent);
        submissionFile.bundleWithIntent(intent);
        submissionDecision.bundleWithIntent(intent);
    }

}
