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
package no.uio.mobileapps.reactnativenettskjema;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import no.uio.mobileapps.reactnativenettskjema.answers.FileUploadForField;
import no.uio.mobileapps.reactnativenettskjema.answers.SelectedMultiOption;
import no.uio.mobileapps.reactnativenettskjema.answers.TextQuestionAnswer;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FilledInFormField;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FormSubmission;
import no.uio.mobileapps.reactnativenettskjema.interfaces.FormSubmissionStatusCode;
import no.uio.mobileapps.reactnativenettskjema.nettskjema.NettskjemaFormSubmissionFactory;
import no.uio.mobileapps.reactnativenettskjema.questions.MultipleChoiceQuestion;
import no.uio.mobileapps.reactnativenettskjema.questions.RadioQuestion;
import no.uio.mobileapps.reactnativenettskjema.questions.TextQuestion;
import no.uio.mobileapps.reactnativenettskjema.questions.UploadQuestion;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
public class NettskjemaPostTest {

    NettskjemaFormSubmissionFactory submissionFactory = new NettskjemaFormSubmissionFactory();
    TextQuestion question = new TextQuestion(577795L);
    RadioQuestion radioQuestion = new RadioQuestion(578095L, Arrays.asList(1275519L, 1275520L));
    MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(578096L, Arrays.asList(1275521L, 1275522L, 1275523L));
    UploadQuestion upload1 = new UploadQuestion(577797L);
    UploadQuestion upload2 = new UploadQuestion(577798L);
    String testFilename1 = "test1.txt";
    String testFilename2 = "test2.txt";

    @Test
    @MediumTest
    public void completeSubmissionWithFilesIsSuccessful() throws Exception {
        FormSubmission formSubmission = submissionFactory.newSubmission(
                75319L,
                Arrays.asList(
                        new TextQuestionAnswer(question, "Android test run on " + new Date().toString()),
                        new SelectedMultiOption(radioQuestion, 0),
                        new SelectedMultiOption(multipleChoiceQuestion, 0),
                        new SelectedMultiOption(multipleChoiceQuestion, 1),
                        new FileUploadForField(upload1, createTestFile(testFilename1, 4), "text/txt"),
                        new FileUploadForField(upload2, createTestFile(testFilename2, 8), "text/txt")
                ));
        formSubmission.post();
        assertThat(formSubmission.status().statusCode(), equalTo(FormSubmissionStatusCode.POST_SUCCESSFUL));
    }

    @Test
    @MediumTest
    public void incompleteSubmissionFails() throws Exception {
        FormSubmission formSubmission = submissionFactory.newSubmission(
                75319L, Collections.<FilledInFormField>singletonList(new SelectedMultiOption(radioQuestion, 0))
        );
        formSubmission.post();
        assertThat(formSubmission.status().statusCode(), equalTo(FormSubmissionStatusCode.POST_FAILED));
        assertThat(formSubmission.status().description(), containsString("errors"));
        assertThat(formSubmission.status().description(), containsString("status"));
        assertThat(formSubmission.status().description(), containsString("failure"));
    }



    private File createTestFile(String filename, int kbSize) throws IOException {
        File file =  InstrumentationRegistry.getContext().getFileStreamPath(filename);
        RandomAccessFile f = new RandomAccessFile(file, "rw");
        f.setLength(1024 * kbSize);
        f.close();
        return file;
    }


}
