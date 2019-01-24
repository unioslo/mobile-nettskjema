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

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions
        .SubmissionDecision;

public class SubmissionStateFromFile {

    private final File file;
    private final String metaData;

    public SubmissionStateFromFile(File file) {
        this.file = file;
        metaData = null;
    }
    public SubmissionStateFromFile(File file, String metaData) {
        this.file = file;
        this.metaData = metaData;
    }


    public SubmissionState withDecision(SubmissionDecision submissionDecision) throws MobileNettskjemaException {
        String extension = FilenameUtils.getExtension(file.getName());
        if (extension.equals(SubmissionFileState.DECRYPTED.extension())) return new DecryptedSubmission(file, submissionDecision);
        if (extension.equals(SubmissionFileState.ENCRYPTED.extension())) return new EncryptedSubmission(file, submissionDecision, this.metaData);
        if (extension.equals(SubmissionFileState.SUBMISSION_FAILED.extension())) return new SubmittedSubmission(file, submissionDecision);
        if (extension.equals(SubmissionFileState.SUBMITTED.extension())) return new SubmittedSubmission(file, submissionDecision);
        if (extension.equals(SubmissionFileState.UNENCRYPTED.extension())) return new InitialSubmission(file, submissionDecision);
        return new NotASubmission();
    }
}
