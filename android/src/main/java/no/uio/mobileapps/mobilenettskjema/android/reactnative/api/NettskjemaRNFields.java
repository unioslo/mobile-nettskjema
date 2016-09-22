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
package no.uio.mobileapps.mobilenettskjema.android.reactnative.api;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;

public class NettskjemaRNFields implements Iterable<FilledInFormField> {
    private final List<FilledInFormField> deserializedFields = new LinkedList<>();

    public NettskjemaRNFields(ReadableArray fields) throws MobileNettskjemaException {
        for (int i = 0; i < fields.size(); i++) {
            ReadableMap field = fields.getMap(i);
            this.deserializedFields.add(new NettskjemaRNField(field).deserialized());
        }
    }

    @Override
    public Iterator<FilledInFormField> iterator() {
        return deserializedFields.iterator();
    }
}
