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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates
        .SubmissionStateFromIntent;

public class QueueService extends IntentService {
    private static final Object threadLock = new Object();
    public QueueService() {
        super(QueueService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        try {
            SubmissionState input = new SubmissionStateFromIntent(intent).deserialized();
            synchronized (threadLock) {
                /*
                A very crude way of avoiding race conditions: Only one file operation can be
                performed at a QueueService instance at any given time.
                A more sophisticated solution might be to have a separate lock for each file.
                */
                input.transformToState(context);
            }
            if (input.isEndOfProcessing()) return;
            relaunchIntent(input.next(context));
        } catch (MobileNettskjemaException e) {
            throw new IllegalStateException(e);
        }
    }

    private void relaunchIntent(SubmissionState submissionState) {
        Intent newIntent = new Intent(getApplicationContext(), QueueService.class);
        submissionState.bundleWithIntent(newIntent);
        getApplicationContext().startService(newIntent);
    }
}
