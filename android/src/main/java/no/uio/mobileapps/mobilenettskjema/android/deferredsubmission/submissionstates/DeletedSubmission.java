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

import org.greenrobot.eventbus.EventBus;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.events.SubmissionStateChanged;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.ClassIdentifier;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;

class DeletedSubmission implements SubmissionState {
    private final SubmissionFile submissionFile;
    private final ClassIdentifier classIdentifier;

    DeletedSubmission(SubmissionFile submissionFile) {
        this.submissionFile = submissionFile;
        this.classIdentifier = new ClassIdentifier(this);
    }

    DeletedSubmission(Intent intent) throws MobileNettskjemaException {
        this(new SubmissionFile(intent));
    }

    @Override
    public void transformToState(Context context) throws MobileNettskjemaException {
        submissionFile.deleteStoredFile();
        EventBus.getDefault().post(new SubmissionStateChanged(SubmissionFileState.DELETED));
    }

    @Override
    public SubmissionState next(Context context) throws MobileNettskjemaException {
        return null;
    }

    @Override
    public boolean isEndOfProcessing() {
        return true;
    }

    @Override
    public boolean indicatesSemiPermanentStorageOnDevice() {
        return false;
    }

    @Override
    public String getSubmissionMetaData() {
        return null;
    }


    @Override
    public void bundleWithIntent(Intent intent) {
        classIdentifier.bundleWithIntent(intent);
        submissionFile.bundleWithIntent(intent);
    }

}
