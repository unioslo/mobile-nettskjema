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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;

public class NettskjemaJsonFields implements Iterable<FilledInFormField> {
    private final LinkedList<FilledInFormField> deserializedFields = new LinkedList<>();

    public NettskjemaJsonFields(JSONArray fields) throws MobileNettskjemaException {
        for (int i = 0; i < fields.length(); i++) {
            try {
                JSONObject field = fields.getJSONObject(i);
                deserializedFields.add(new NettskjemaJsonField(field).deserialized());
            } catch (JSONException e) {
                throw new MobileNettskjemaException(e);
            }
        }
    }

    @Override
    public Iterator<FilledInFormField> iterator() {
        return deserializedFields.iterator();
    }
}
