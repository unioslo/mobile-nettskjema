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

import android.content.Intent;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.ClassIdentifier;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.ClassName;

public class SubmissionStateFromIntent {

    private final ClassName className;
    private final Intent intent;

    public SubmissionStateFromIntent(Intent intent) {
        String className = intent.getStringExtra(ClassIdentifier.CLASS_INTENTKEY);
        this.className = new ClassName(className);
        this.intent = intent;
    }

    public SubmissionState deserialized() throws MobileNettskjemaException {
        if (className.matches(DecryptedSubmission.class)) return new DecryptedSubmission(intent);
        if (className.matches(EncryptedSubmission.class)) return new EncryptedSubmission(intent);
        if (className.matches(InitialSubmission.class)) return new InitialSubmission(intent);
        if (className.matches(SubmittedSubmission.class)) return new SubmittedSubmission(intent);
        if (className.matches(DeletedSubmission.class)) return new DeletedSubmission(intent);
        if (className.matches(NotASubmission.class)) return new NotASubmission();
        throw new MobileNettskjemaException("Class " + className + " does not have a corresponding " + SubmissionState.class.getSimpleName());
    }
}
