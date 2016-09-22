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

import com.facebook.react.bridge.ReadableMap;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;

public class NettskjemaRNField {
    private final ReadableMap field;

    public NettskjemaRNField(ReadableMap field) {
        this.field = field;
    }

    public FilledInFormField deserialized() throws MobileNettskjemaException {
        String type = field.getString("type");
        if (type.equals("upload")) return new RNFileUploadForField(field).field();
        if (type.equals("radio")) return new RNSelectedRadioOption(field).field();
        if (type.equals("multipleChoice")) return new RNSelectedMultipleChoiceOption(field).field();
        if (type.equals("text")) return new RNTextQuestionAnswer(field).field();
        throw new MobileNettskjemaException("Field `type` must be one of `upload`, `radio`, `multipleChoice` or `text`, instead got " + type);
    }
}
