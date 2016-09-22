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
import no.uio.mobileapps.mobilenettskjema.android.reactnative.interfaces.RNApiField;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.TextQuestionAnswer;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.TextQuestion;

public class RNTextQuestionAnswer implements RNApiField<TextQuestionAnswer> {
    private final Long id;
    private final String answer;

    public RNTextQuestionAnswer(ReadableMap field) {
        this.id = (long) field.getInt("questionId");
        this.answer = field.getString("answer");
    }

    @Override
    public TextQuestionAnswer field() throws MobileNettskjemaException {
        return new TextQuestionAnswer(new TextQuestion(id), this.answer);
    }
}
