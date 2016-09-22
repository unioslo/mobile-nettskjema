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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.serialization.SubmissionFile;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.SelectedMultiOption;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.TextQuestionAnswer;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaFilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.RadioQuestion;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.TextQuestion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
public class JsonDeserializationTest {
    private static final File JSON_FILE = getFile("filledInForm.json");

    @Before
    public void prepareFiles() throws IOException {
        cleanUp();
        PrintWriter writer = new PrintWriter(JSON_FILE);
        writer.println("{\"form\":{\"form\":123},\"fields\":[{\"identifier\":\"answersAsMap[123]" +
                ".textAnswer\",\"answer\":\"test\"},{\"identifier\":\"answersAsMap[123]" +
                ".answerOption\",\"selectedOption\":234}]}");
        writer.close();
    }

    @After
    public void cleanUp() {
        JSON_FILE.delete();
    }


   @Test
    public void jsonIsProperlyDeserialized() throws Exception {
        SubmissionFile jsonFile = new SubmissionFile(JSON_FILE);
        FilledInForm filledInForm = new NettskjemaFilledInForm(jsonFile.contents());
        assertThat(filledInForm, equalTo((FilledInForm) new NettskjemaFilledInForm(
                new NettskjemaForm(123L),
                Arrays.asList(
                        new TextQuestionAnswer(new TextQuestion(123), "test"),
                        new SelectedMultiOption(new RadioQuestion(123, Arrays.asList
                                (234L, 345L)), 0)
                ))));
    }

    private static File getFile(String filename) {
        return InstrumentationRegistry.getContext().getFileStreamPath(filename);
    }
}
