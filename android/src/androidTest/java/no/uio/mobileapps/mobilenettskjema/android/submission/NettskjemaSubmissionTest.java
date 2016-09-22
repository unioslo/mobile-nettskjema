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

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import no.uio.mobileapps.mobilenettskjema.android.submission.answers.FileUploadForField;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.SelectedMultiOption;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.TextQuestionAnswer;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FormSubmission;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FormSubmissionStatusCode;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaFormSubmissionFactory;
import no.uio.mobileapps.mobilenettskjema.android.testdata.TestFile;
import no.uio.mobileapps.mobilenettskjema.android.testdata.TestForm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
public class NettskjemaSubmissionTest {

    NettskjemaFormSubmissionFactory submissionFactory = new NettskjemaFormSubmissionFactory();

    @Test
    @MediumTest
    public void completeSubmissionWithFilesIsSuccessful() throws Exception {
        TestFile testFile1 = new TestFile(4);
        TestFile testFile2 = new TestFile(8);
        FormSubmission formSubmission = submissionFactory.newSubmission(
                TestForm.formId,
                Arrays.asList(
                        new TextQuestionAnswer(TestForm.textQuestion, "Android test run on " + new Date().toString()),
                        new SelectedMultiOption(TestForm.radioQuestion, 0),
                        new SelectedMultiOption(TestForm.multipleChoiceQuestion, 0),
                        new SelectedMultiOption(TestForm.multipleChoiceQuestion, 1),
                        new FileUploadForField(TestForm.uploadField1, testFile1.randomContent(), "text/txt"),
                        new FileUploadForField(TestForm.uploadField2, testFile2.randomContent(), "text/txt")
                ));
        formSubmission.post();
        assertThat(formSubmission.status().statusCode(), equalTo(FormSubmissionStatusCode.POST_SUCCESSFUL));
    }

    @Test
    @MediumTest
    public void incompleteSubmissionFails() throws Exception {
        FormSubmission formSubmission = submissionFactory.newSubmission(
                TestForm.formId, Collections.<FilledInFormField>singletonList(new SelectedMultiOption(TestForm.radioQuestion, 0))
        );
        formSubmission.post();
        assertThat(formSubmission.status().statusCode(), equalTo(FormSubmissionStatusCode.POST_FAILED));
        assertThat(formSubmission.status().description(), containsString("errors"));
        assertThat(formSubmission.status().description(), containsString("status"));
        assertThat(formSubmission.status().description(), containsString("failure"));
    }


}
