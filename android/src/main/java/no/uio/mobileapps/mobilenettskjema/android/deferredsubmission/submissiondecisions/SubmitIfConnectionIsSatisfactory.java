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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissiondecisions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import no.uio.mobileapps.mobilenettskjema.android.R;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.AutoSubmissionSetting;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates
        .EncryptedSubmission;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.SubmittedSubmission;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class SubmitIfConnectionIsSatisfactory implements SubmissionDecision {
    static final String INTENT_KEY = SubmitIfConnectionIsSatisfactory.class.getName();

    public SubmitIfConnectionIsSatisfactory() {
    }

    @Override
    public SubmissionState nextSubmissionState(SubmissionFile submissionFile, Context context) {
        if (uploadConditionIsMet(context)) return new SubmittedSubmission(submissionFile, this);
        else return new EncryptedSubmission(submissionFile, this);
    }

    private boolean uploadConditionIsMet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.deferred_submissions_settings_key), Context.MODE_PRIVATE);
        String autoUploadSettingKey = context.getString(R.string.auto_upload_setting_key);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        AutoSubmissionSetting setting = AutoSubmissionSetting.valueOf(sharedPreferences.getString(autoUploadSettingKey, AutoSubmissionSetting.NEVER.toString()));

        return !setting.equals(AutoSubmissionSetting.NEVER) && (
                setting.equals (AutoSubmissionSetting.ALWAYS) ||
                        setting.equals(AutoSubmissionSetting.ONLY_WITH_WIFI) &&
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                                        .isConnected()
        );
    }

    @Override
    public void bundleWithIntent(Intent intent) {
        intent.putExtra(INTENT_KEY, true);
    }
}
