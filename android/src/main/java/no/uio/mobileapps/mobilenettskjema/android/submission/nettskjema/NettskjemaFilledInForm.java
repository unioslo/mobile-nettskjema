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
package no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.NettskjemaJsonFields;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.Form;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.JSONSerializable;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.MultipartRequestField;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NettskjemaFilledInForm implements FilledInForm {

    private final Form form;
    private final Iterable<FilledInFormField> filledInFormFields;

    public NettskjemaFilledInForm(Form form, Iterable
            <FilledInFormField> filledInFormFields) {
        this.form = form;
        this.filledInFormFields = filledInFormFields;
    }

    public NettskjemaFilledInForm(String jsonString) throws MobileNettskjemaException {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.form = new NettskjemaForm(jsonObject.getJSONObject("form"));
            this.filledInFormFields = new NettskjemaJsonFields(jsonObject.getJSONArray("fields"));
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    @Override
    public Request postRequest(MultipartRequestField csrfToken) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        csrfToken.addToBuilder(bodyBuilder);
        for (MultipartRequestField field: filledInFormFields) {
            field.addToBuilder(bodyBuilder);
        }
        RequestBody requestBody = bodyBuilder.build();
        return new Request.Builder().url(form.postUrl()).header("User-Agent", "Diktafon/Android").post(requestBody).build();
    }

    @Override
    public JSONObject serialized() throws MobileNettskjemaException {
        try {
            JSONObject object = new JSONObject();
            object.put("form", form.serialized());
            JSONArray fields = new JSONArray();
            for (JSONSerializable field : filledInFormFields) {
                fields.put(field.serialized());
            }
            object.put("fields", fields);
            return object;
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    private Set<FilledInFormField> fieldSet() {
        Set<FilledInFormField> set = new HashSet<>();
        for (FilledInFormField field: filledInFormFields) {
            set.add(field);
        }
        return set;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NettskjemaFilledInForm)) return false;
        if (object == this) return true;
        NettskjemaFilledInForm other = (NettskjemaFilledInForm) object;
        return this.form.equals(other.form)
                && this.fieldSet().equals(other.fieldSet());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + form.hashCode();
        result = 31 * result + fieldSet().hashCode();
        return result;
    }
}
