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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
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
    private String metaData;

    public NettskjemaQueueableFormSubmission(FilledInForm filledInForm, StorageDirectory storageDirectory, Context context) {
        this.filledInForm = filledInForm;
        this.storageDirectory = storageDirectory;
        this.context = context;
    }

    public NettskjemaQueueableFormSubmission(FilledInForm filledInForm, StorageDirectory storageDirectory, Context context, String metaData) {
        this.filledInForm = filledInForm;
        this.storageDirectory = storageDirectory;
        this.context = context;
        this.metaData = metaData;
    }

    public void submit() throws MobileNettskjemaException {
        String filename = UUID.randomUUID().toString();
        JsonFile jsonFile = new JsonFile(storageDirectory.fileNamed(filename));
        jsonFile.store(filledInForm);
        File metadata = storageDirectory.fileNamed(filename + ".metadata");

        try {
            JSONObject a = new JSONObject();
            a.put("id", filename);
            a.put("dateCreated", new Date().getTime());
            a.put("submitted", false);
            this.metaData = a.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter printWriter = new PrintWriter(metadata);
            printWriter.println(this.metaData);
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new MobileNettskjemaException(e);
        }

        SubmissionFile submissionFile;
        if(metaData == null) {
             submissionFile = new SubmissionFile(jsonFile);
        } else {
             submissionFile = new SubmissionFile(jsonFile, metadata);
        }

        SubmissionState submission = new InitialSubmission(submissionFile, new SubmitIfConnectionIsSatisfactory());
        Intent intent = new Intent(context, QueueService.class);
        submission.bundleWithIntent(intent);
        context.startService(intent);
    }


}
