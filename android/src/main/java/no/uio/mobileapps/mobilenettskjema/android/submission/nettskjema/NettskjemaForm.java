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

import org.json.JSONException;
import org.json.JSONObject;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.Form;

public class NettskjemaForm implements Form {
    private Long id;

    public NettskjemaForm(Long id) {
        this.id = id;
    }

    public NettskjemaForm(JSONObject form) throws JSONException {
        this.id = form.getLong("form");
    }

    @Override
    public String postUrl() {
        return "https://nettskjema.no/answer/deliver.json?formId=" + String.valueOf(id);
    }

    @Override
    public JSONObject serialized() throws MobileNettskjemaException {
        JSONObject object = new JSONObject();
        try {
            object.put("form", id);
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
        return object;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NettskjemaForm)) return false;
        if (object == this) return true;
        NettskjemaForm other = (NettskjemaForm) object;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        return result;
    }
}
