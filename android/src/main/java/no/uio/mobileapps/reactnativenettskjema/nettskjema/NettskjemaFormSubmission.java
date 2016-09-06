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
package no.uio.mobileapps.reactnativenettskjema.nettskjema;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import no.uio.mobileapps.reactnativenettskjema.interfaces.CsrfRequestFactory;
import no.uio.mobileapps.reactnativenettskjema.interfaces.CsrfTokenFactory;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FilledInFormField;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FormSubmission;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FormSubmissionStatus;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FormSubmissionStatusCode;
import no.uio.mobileapps.reactnativenettskjema.interfaces.SubmissionRequestFactory;
import okhttp3.OkHttpClient;
import okhttp3.Response;

class NettskjemaFormSubmission implements FormSubmission {

    private final OkHttpClient client;
    private final CsrfRequestFactory csrfRequestFactory;
    private final CsrfTokenFactory csrfTokenFactory;
    private final SubmissionRequestFactory postRequestFactory;
    private String responseString;

    public NettskjemaFormSubmission(OkHttpClient client, CsrfRequestFactory csrfRequestFactory, CsrfTokenFactory csrfTokenFactory, SubmissionRequestFactory postRequestFactory) {
        this.client = client;
        this.csrfRequestFactory = csrfRequestFactory;
        this.csrfTokenFactory = csrfTokenFactory;
        this.postRequestFactory = postRequestFactory;
    }

    @Override
    public void post() throws IOException {
        Response csrfResponse = client.newCall(csrfRequestFactory.newRequest()).execute();
        FilledInFormField csrfToken = csrfTokenFactory.newCsrfToken(csrfResponse.body().string());
        Response response = client.newCall(postRequestFactory.newRequest(csrfToken)).execute();
        this.responseString = removeTextAreaTag(response.body().string());
    }

    private static String removeTextAreaTag(String completeStringResponse) {
        return completeStringResponse.substring(10, completeStringResponse.length() - 11);
    }

    @Override
    public FormSubmissionStatus status() {
        Log.d("TEST", responseString);
        if (responseString == null) return new FormSubmissionStatus() {
            @Override
            public FormSubmissionStatusCode statusCode() {
                return FormSubmissionStatusCode.NOT_POSTED;
            }

            @Override
            public String description() {
                return "Submission not yet posted";
            }
        };
        if (isSuccessful()) return new FormSubmissionStatus() {
            @Override
            public FormSubmissionStatusCode statusCode() {
                return FormSubmissionStatusCode.POST_SUCCESSFUL;
            }

            @Override
            public String description() {
                return "Submission successfully posted";
            }
        };
        else return new FormSubmissionStatus() {
            @Override
            public FormSubmissionStatusCode statusCode() {
                return FormSubmissionStatusCode.POST_FAILED;
            }

            @Override
            public String description() {
                    return "Server response: " + responseString;
            }
        };
    }

    private boolean isSuccessful() {
        if (responseString == null) throw new IllegalStateException();
        try {
            JSONObject responseJson = new JSONObject(responseString);
            return responseJson.getString("message").equals("Svaret er levert")
                    && responseJson.getString("status").equals("success");
        } catch (JSONException e) {
            return false;
        }
    }

}