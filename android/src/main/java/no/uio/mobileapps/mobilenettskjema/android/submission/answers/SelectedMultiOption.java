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
package no.uio.mobileapps.mobilenettskjema.android.submission.answers;

import org.json.JSONException;
import org.json.JSONObject;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FieldValue;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.MultipleOptionsFormField;
import okhttp3.MultipartBody;

public class SelectedMultiOption implements FilledInFormField {
    private final String identifier;
    private final Long selectedOption;

    public SelectedMultiOption(MultipleOptionsFormField question, int index) {
        this.identifier = question.identifier().name();
        this.selectedOption = question.optionIds().get(index);
    }

    public SelectedMultiOption(JSONObject field) throws MobileNettskjemaException {
        try {
            this.identifier = field.getString("identifier");
            this.selectedOption = field.getLong("selectedOption");
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    @Override
    public void addToBuilder(MultipartBody.Builder bodyBuilder) {
        bodyBuilder.addFormDataPart(identifier, new FieldValue() {
            @Override
            public String value() {
                return String.valueOf(selectedOption);
            }
        }.value());
    }

    @Override
    public JSONObject serialized() throws MobileNettskjemaException {
        JSONObject object = new JSONObject();
        try {
            object.put("identifier", identifier);
            object.put("selectedOption", selectedOption);
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
        return object;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SelectedMultiOption)) return false;
        if (object == this) return true;
        SelectedMultiOption other = (SelectedMultiOption) object;
        return this.identifier.equals(other.identifier) && this.selectedOption.equals(other.selectedOption);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + identifier.hashCode();
        result = 31 * result + selectedOption.hashCode();
        return result;
    }

}

