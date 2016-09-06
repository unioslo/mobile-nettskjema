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
package no.uio.mobileapps.reactnativenettskjema.answers;

import no.uio.mobileapps.reactnativenettskjema.interfaces.FieldValue;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FilledInFormField;
import no.uio.mobileapps.reactnativenettskjema.interfaces.MultipleOptionsFormField;
import okhttp3.MultipartBody;

public class SelectedMultiOption implements FilledInFormField {
    private final String identifier;
    private final Long selectedOption;

    public SelectedMultiOption(MultipleOptionsFormField question, int index) {
        this.identifier = question.identifier().name();
        this.selectedOption = question.optionIds().get(index);
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
}

