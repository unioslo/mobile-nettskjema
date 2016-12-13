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
package no.uio.mobileapps.mobilenettskjema.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.react.bridge.ReadableMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.filemanagement.TemporaryFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.Event;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.StorageDirectory;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.AutoSubmissionSetting;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.NettskjemaQueueableFormSubmission;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.QueueService;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions.AlwaysSubmit;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.SubmissionStateFromFile;
import no.uio.mobileapps.mobilenettskjema.android.reactnative.api.RNFilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInForm;
import no.uio.mobileapps.mobilenettskjema.interfaces.EventSink;

public class MobileNettskjema {

    private final StorageDirectory storageDirectory;
    private final Context context;
    private final EventSink eventSink;

    @Subscribe
    public void onEvent(Event event) {
        eventSink.put(event);
    }

    public MobileNettskjema(StorageDirectory storageDirectory, Context context, EventSink eventSink) {
        EventBus.getDefault().register(this);
        this.storageDirectory = storageDirectory;
        this.context = context;
        this.eventSink = eventSink;
    }

    public void addToSubmissionQueue(ReadableMap submission) throws MobileNettskjemaException {
        new NettskjemaQueueableFormSubmission(
                new RNFilledInForm(submission).field(),
                this.storageDirectory,
                context
        ).submit();
    }

    public void addToSubmissionQueue(FilledInForm filledInForm) throws MobileNettskjemaException {
        new NettskjemaQueueableFormSubmission(
                filledInForm,
                storageDirectory,
                context
        ).submit();
    }

    public void forceRetryAllSubmissions() throws MobileNettskjemaException {
        clearTemporaryFiles();

        for (File file: filesInStorageDirectory()) {
            Intent intent = new Intent(context, QueueService.class);
            SubmissionState submissionState = new SubmissionStateFromFile(file).withDecision(new AlwaysSubmit()).next(context);
            submissionState.bundleWithIntent(intent);
            context.startService(intent);
        }
    }

    private File[] filesInStorageDirectory() {
        return storageDirectory.fileNamed(".").listFiles();
    }

    public void setAutoSubmissionsPreference(AutoSubmissionSetting value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.deferred_submissions_settings_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.auto_upload_setting_key), value.toString()).apply();
    }

    public Iterable<SubmissionState> submissionStates() throws MobileNettskjemaException {
        List<SubmissionState> output = new LinkedList<>();
        for (File file: filesInStorageDirectory()) {
            SubmissionState submissionState = new SubmissionStateFromFile(file).withDecision(null);
            if (submissionState.indicatesSemiPermanentStorageOnDevice()) output.add(submissionState);
        }
        return output;
    }

    public void clearTemporaryFiles() throws MobileNettskjemaException {
        for (File file: filesInStorageDirectory()) {
            if (TemporaryFile.isTemporary(file)) {
                boolean deleted = file.delete();
                if (!deleted) throw new MobileNettskjemaException("Unable to delete temporary file " + file);
            }
        }
    }
}
