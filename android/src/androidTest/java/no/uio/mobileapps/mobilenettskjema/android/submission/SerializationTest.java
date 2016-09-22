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
package no.uio.mobileapps.mobilenettskjema.android.submission;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

import no.uio.mobileapps.mobilenettskjema.android.submission.answers.FileUploadForField;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.NettskjemaJsonField;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.SelectedMultiOption;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.TextQuestionAnswer;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.JSONSerializable;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaFilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.RadioQuestion;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.TextQuestion;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.UploadQuestion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class SerializationTest {

    @Test
    public void textQuestionAnswer() throws Exception {
        assertDeserializedEqualsOriginal(
                new TextQuestionAnswer(new TextQuestion(123), "test"));
    }

    @Test
    public void selectedMultiOption() throws Exception {
        assertDeserializedEqualsOriginal(
                new SelectedMultiOption(new RadioQuestion(123, Arrays.asList
                (234L, 345L)), 0));
    }

    @Test
    public void fileUploadForField() throws Exception {
        PrintWriter out = new PrintWriter(getFile("uploaded.txt"));
        out.println("uploaded file contents");
        out.close();
        assertDeserializedEqualsOriginal(
                new FileUploadForField(new UploadQuestion(123), getFile("uploaded.txt"), "text/txt"));
        getFile("uploaded.txt").delete();
    }

    @Test
    public void filledInForm() throws Exception {
        FilledInForm form = new NettskjemaFilledInForm(new NettskjemaForm(123L),
                Arrays.asList(
                        new TextQuestionAnswer(new TextQuestion(123), "test"),
                        new SelectedMultiOption(new RadioQuestion(123, Arrays.asList
                (234L, 345L)), 0)
        ));
        FilledInForm slightlyDifferentForm = new NettskjemaFilledInForm(new NettskjemaForm(123L),
                Arrays.asList(
                        new TextQuestionAnswer(new TextQuestion(123), "test"),
                        new SelectedMultiOption(new RadioQuestion(123, Arrays.asList
                                (234L, 345L)), 1)
                ));
        JSONObject serialized = form.serialized();
        NettskjemaFilledInForm deserializedForm = new NettskjemaFilledInForm(serialized.toString());
        assertThat(deserializedForm, equalTo(form));
        assertThat(deserializedForm, is(not(equalTo(slightlyDifferentForm))));
    }

    private static void assertDeserializedEqualsOriginal(JSONSerializable field) throws Exception {
        JSONObject serialized = field.serialized();
        FilledInFormField deserializedAnswer = new NettskjemaJsonField(serialized).deserialized();
        assertThat(deserializedAnswer, equalTo(field));
    }

    private File getFile(String filename) {
        return InstrumentationRegistry.getContext().getFileStreamPath(filename);
    }
}