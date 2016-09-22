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
package no.uio.mobileapps.mobilenettskjema.android.submission.questions;

import java.util.List;

import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FieldIdentifier;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.MultipleOptionsFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaFieldIdentifier;

public class RadioQuestion implements MultipleOptionsFormField {
    private final long id;
    private final List<Long> optionIds;

    public RadioQuestion(long id, List<Long> optionIds) {
        this.id = id;
        this.optionIds = optionIds;
    }

    @Override
    public FieldIdentifier identifier() {
        return new NettskjemaFieldIdentifier("answerOption", id);
    }

    @Override
    public List<Long> optionIds() {
        return optionIds;
    }
}
