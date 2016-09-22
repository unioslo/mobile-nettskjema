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
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;

public class NettskjemaJsonField {
    private final JSONObject field;
    public NettskjemaJsonField(JSONObject field) {
        this.field = field;
    }

    public FilledInFormField deserialized() throws MobileNettskjemaException {
        try {
            if (field.opt("base64") != null)
                return new FileUploadForField(field);
            else if (field.opt("selectedOption") != null)
                return new SelectedMultiOption(field);
            else if (field.opt("answer") != null)
                return new TextQuestionAnswer(field);
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
        throw new MobileNettskjemaException("JSONObject " + field + " does not match an implementation of FilledInFormField");
    }
}
