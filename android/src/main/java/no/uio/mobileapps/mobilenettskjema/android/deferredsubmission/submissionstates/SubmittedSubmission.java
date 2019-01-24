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
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.ClassIdentifier;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions
        .SubmissionDecision;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions
        .SubmissionDecisionFromIntent;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FormSubmission;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FormSubmissionStatus;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FormSubmissionStatusCode;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaFilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema
        .NettskjemaFormSubmissionFactory;


public class SubmittedSubmission implements SubmissionState {

    private final SubmissionFile submissionFile;
    private final ClassIdentifier classIdentifier;
    private final SubmissionDecision submissionDecision;

    public SubmittedSubmission(SubmissionFile submissionFile, SubmissionDecision submissionDecision) {
        this.submissionFile = submissionFile;
        this.submissionDecision = submissionDecision;
        this.classIdentifier = new ClassIdentifier(this);
    }

    SubmittedSubmission(Intent intent) throws MobileNettskjemaException {
        this(new SubmissionFile(intent), new SubmissionDecisionFromIntent(intent).deserialized());
    }

    SubmittedSubmission(File file, SubmissionDecision submissionDecision) throws  MobileNettskjemaException {
        this(new SubmissionFile(file), submissionDecision);
    }

    @Override
    public void transformToState(Context context) throws MobileNettskjemaException {
        FormSubmissionStatus status = postSubmission();
        if (status.statusCode() == FormSubmissionStatusCode.POST_SUCCESSFUL) {
            submissionFile.markAs(SubmissionFileState.SUBMITTED);
        } else {
            submissionFile.markAs(SubmissionFileState.SUBMISSION_FAILED);
        }
    }

    @Override
    public SubmissionState next(Context context) throws MobileNettskjemaException {
        /* update metadata file? */
        if (submissionFile.isMarked(SubmissionFileState.SUBMITTED)) {
            return new EncryptedSubmission(submissionFile, submissionDecision, 1);
        } else {
            return new EncryptedSubmission(submissionFile, submissionDecision, 2);
        }
        /*
        if (submissionFile.isMarked(SubmissionFileState.SUBMITTED)) return new DeletedSubmission(submissionFile);
        else return new EncryptedSubmission(submissionFile, submissionDecision, "SubmissionState next");
        */
    }

    @Override
    public boolean isEndOfProcessing() {
        return false;
    }

    @Override
    public boolean indicatesSemiPermanentStorageOnDevice() {
        return false;
    }

    @Override
    public String getSubmissionMetaData() {
        return null;
    }

    private FormSubmissionStatus postSubmission() throws MobileNettskjemaException {
        FormSubmission formSubmission = new NettskjemaFormSubmissionFactory().newSubmission(
                new NettskjemaFilledInForm(submissionFile.contents()));
        formSubmission.post();
        return formSubmission.status();
    }

    @Override
    public void bundleWithIntent(Intent intent) {
        classIdentifier.bundleWithIntent(intent);
        submissionFile.bundleWithIntent(intent);
        submissionDecision.bundleWithIntent(intent);
    }
}
