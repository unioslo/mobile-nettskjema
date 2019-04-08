package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions;

import android.content.Context;
import android.content.Intent;

import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.DeletedSubmission;

public class DeleteSubmission implements SubmissionDecision {
    static final String INTENT_KEY = DeleteSubmission.class.getName();

    @Override
    public SubmissionState nextSubmissionState(SubmissionFile submissionFile, Context context) {
        return new DeletedSubmission(submissionFile);
    }

    @Override
    public void bundleWithIntent(Intent intent) {
        intent.putExtra(INTENT_KEY, true);
    }
}
