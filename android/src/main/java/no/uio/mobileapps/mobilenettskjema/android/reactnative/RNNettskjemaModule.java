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
package no.uio.mobileapps.mobilenettskjema.android.reactnative;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjema;
import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.StorageDirectory;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.SubmissionState;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.AutoSubmissionSetting;

public class RNNettskjemaModule extends ReactContextBaseJavaModule {

    private final MobileNettskjema mobileNettskjema;

    public RNNettskjemaModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        this.mobileNettskjema = new MobileNettskjema(
                new StorageDirectory() {
                    @Override
                    public File fileNamed(String name) {
                        return reactContext.getFileStreamPath(name);
                    }
                },
                reactContext,
                new RNEventSink(reactContext)
        );
    }

    @Override
    public String getName() {
        return "RNNettskjema";
    }

    @ReactMethod
    public void deleteSubmission(String id, Promise promise) {
        try {
            mobileNettskjema.deleteSubmission(id);
        } catch (MobileNettskjemaException e) {
            e.printStackTrace();
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void deleteSubmissionsIfTooOld(Promise promise) {
        try {
            mobileNettskjema.deleteSubmissionsIfTooOld();
        } catch (MobileNettskjemaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void addToSubmissionQueue(ReadableMap submission, Promise promise) {
        try {
            mobileNettskjema.addToSubmissionQueue(submission);
        } catch (MobileNettskjemaException e) {
            promise.reject(e);
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void uploadSubmission(String id, Promise promise) {
        try {
            mobileNettskjema.retryUploadForFile(id);
        } catch (MobileNettskjemaException e) {
            e.printStackTrace();
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void addToSubmissionQueueWithMetaData(ReadableMap submission, String metaData, Promise promise) {
        try {
            mobileNettskjema.addToSubmissionQueueWithMetaData(submission, metaData);
        } catch (MobileNettskjemaException e) {
            promise.reject(e);
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void forceRetryAllSubmissions(Promise promise) {
        try {
            mobileNettskjema.forceRetryAllSubmissions();
        } catch (MobileNettskjemaException e) {
            promise.reject(e);
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void setAutoSubmissionsPreference(String value, Promise promise) {
        mobileNettskjema.setAutoSubmissionsPreference(AutoSubmissionSetting.valueOf(value));
        promise.resolve(null);
    }

    @ReactMethod
    public void stateOfSubmissions(Promise promise) {
        WritableArray output = new WritableNativeArray();
        try {
            for (SubmissionState submissionState: mobileNettskjema.submissionStates()) {
                //output.pushString(submissionState.getClass().getSimpleName() + ": " + submissionState.getSubmissionMetaData());
                output.pushString(submissionState.getSubmissionMetaData());

            }
        } catch (MobileNettskjemaException e) {
            promise.reject(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        promise.resolve(output);
    }

    @ReactMethod
    public void clearTemporaryFiles(Promise promise) {
	try {
	    mobileNettskjema.clearTemporaryFiles();
	} catch (MobileNettskjemaException e) {
	    promise.reject(e);
	}
	promise.resolve(null);
    }
}
