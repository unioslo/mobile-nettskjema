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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.StorageDirectory;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.JsonFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions
        .SubmitIfConnectionIsSatisfactory;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.InitialSubmission;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInForm;

public class NettskjemaQueueableFormSubmission {

    private final FilledInForm filledInForm;
    private final Context context;
    private final StorageDirectory storageDirectory;

    public NettskjemaQueueableFormSubmission(FilledInForm filledInForm, StorageDirectory storageDirectory, Context context) {
        this.filledInForm = filledInForm;
        this.storageDirectory = storageDirectory;
        this.context = context;
    }

    public void submit() throws MobileNettskjemaException {
        JsonFile jsonFile = new JsonFile(storageDirectory.fileNamed(UUID.randomUUID().toString()));
        jsonFile.store(filledInForm);
        SubmissionFile submissionFile = new SubmissionFile(jsonFile);
        SubmissionState submission = new InitialSubmission(submissionFile, new SubmitIfConnectionIsSatisfactory());
        Intent intent = new Intent(context, QueueService.class);
        submission.bundleWithIntent(intent);
        context.startService(intent);
    }


}
