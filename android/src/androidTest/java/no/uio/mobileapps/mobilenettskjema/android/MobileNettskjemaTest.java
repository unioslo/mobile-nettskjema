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
package no.uio.mobileapps.mobilenettskjema.android;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.events.SubmissionStateChanged;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.Event;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.StorageDirectory;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.AutoSubmissionSetting;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates.SubmissionFileState;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.FileUploadForField;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.SelectedMultiOption;
import no.uio.mobileapps.mobilenettskjema.android.submission.answers.TextQuestionAnswer;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaFilledInForm;
import no.uio.mobileapps.mobilenettskjema.android.testdata.TestFile;
import no.uio.mobileapps.mobilenettskjema.android.testdata.TestForm;
import no.uio.mobileapps.mobilenettskjema.interfaces.EventSink;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
public class MobileNettskjemaTest {
    private final List<Event> emittedEvents = new ArrayList<>();
    private final MobileNettskjema mobileNettskjema = new MobileNettskjema(
            new StorageDirectory() {
                @Override
                public File fileNamed(String name) {
                    return InstrumentationRegistry.getContext().getFileStreamPath(name);
                }
    }, InstrumentationRegistry.getContext(),
            new EventSink() {
                @Override
                public void put(Event event) {
                    emittedEvents.add(event);
                }
    });

    @Before
    public void deleteLeftOverFiles() {
        for (File file: testFileFolder().listFiles()){
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    @Before
    public void enableUploads() {
        mobileNettskjema.setAutoSubmissionsPreference(AutoSubmissionSetting.ALWAYS);
    }

    @After
    public void clearEvents() {
        emittedEvents.clear();
    }

    @Test
    @MediumTest
    public void completeSubmissionWithFilesSubmitsAndIsDeleted() throws Exception {
        submitValidSubmissionWithFiles();
        assertThat(emittedEvents, equalTo(Arrays.<Event>asList(
                new SubmissionStateChanged(SubmissionFileState.UNENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.SUBMITTED),
                new SubmissionStateChanged(SubmissionFileState.DELETED)
        )));
        assertThat(testFileFolder().listFiles().length, is(0));
    }

    private void submitValidSubmissionWithFiles() throws Exception {
        TestFile testFile1 = new TestFile(4);
        TestFile testFile2 = new TestFile(8);
        mobileNettskjema.addToSubmissionQueue(
                new NettskjemaFilledInForm(
                        TestForm.form,
                        Arrays.asList(
                                new TextQuestionAnswer(TestForm.textQuestion, "Android queueing test run on " + new Date().toString()),
                                new SelectedMultiOption(TestForm.radioQuestion, 0),
                                new SelectedMultiOption(TestForm.multipleChoiceQuestion, 0),
                                new SelectedMultiOption(TestForm.multipleChoiceQuestion, 1),
                                new FileUploadForField(TestForm.uploadField1, testFile1.randomContent(), "text/txt"),
                                new FileUploadForField(TestForm.uploadField2, testFile2.randomContent(), "text/txt")
                        )
                )
        );
        Thread.sleep(800);
        testFile1.delete();
        testFile2.delete();
    }

    private void submitValidSubmission() throws Exception {
        mobileNettskjema.addToSubmissionQueue(
                new NettskjemaFilledInForm(
                        TestForm.form,
                        Arrays.asList(
                                new TextQuestionAnswer(TestForm.textQuestion, "Android queueing test run on " + new Date().toString()),
                                new SelectedMultiOption(TestForm.radioQuestion, 0),
                                new SelectedMultiOption(TestForm.multipleChoiceQuestion, 0),
                                new SelectedMultiOption(TestForm.multipleChoiceQuestion, 1)
                        )
                )
        );
    }

    @Test
    @MediumTest
    public void incompleteSubmissionFailsAndEncrypts() throws Exception {
        submitInvalidSubmission();
        Thread.sleep(800);
        assertThat(emittedEvents, equalTo(Arrays.<Event>asList(
                new SubmissionStateChanged(SubmissionFileState.UNENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.SUBMISSION_FAILED),
                new SubmissionStateChanged(SubmissionFileState.ENCRYPTED)
        )));
        assertThat(testFileFolder().listFiles().length, is(1));
    }

    @Test
    @MediumTest
    public void retryFailedUploadsTriesToSubmitAgain() throws Exception {
        submitInvalidSubmission();
        Thread.sleep(800);
        mobileNettskjema.forceRetryAllSubmissions();
        Thread.sleep(800);
        assertThat(emittedEvents, equalTo(Arrays.<Event>asList(
                new SubmissionStateChanged(SubmissionFileState.UNENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.SUBMISSION_FAILED),
                new SubmissionStateChanged(SubmissionFileState.ENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.DECRYPTED),
                new SubmissionStateChanged(SubmissionFileState.SUBMISSION_FAILED),
                new SubmissionStateChanged(SubmissionFileState.ENCRYPTED)
        )));
    }

    @Test
    @MediumTest
    public void disablingUploadsCausesNewSubmissionToJustEncrypt() throws Exception {
        mobileNettskjema.setAutoSubmissionsPreference(AutoSubmissionSetting.NEVER);
        submitValidSubmission();
        Thread.sleep(200);
        assertThat(emittedEvents, equalTo(Arrays.<Event>asList(
                new SubmissionStateChanged(SubmissionFileState.UNENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.ENCRYPTED)
        )));
    }

    @Test
    @MediumTest
    public void forceRetryTriesToSubmitEvenWhenUploadsAreDisabled() throws Exception {
        mobileNettskjema.setAutoSubmissionsPreference(AutoSubmissionSetting.NEVER);
        submitValidSubmission();
        Thread.sleep(400);
        mobileNettskjema.forceRetryAllSubmissions();
        Thread.sleep(400);
        assertThat(emittedEvents, equalTo(Arrays.<Event>asList(
                new SubmissionStateChanged(SubmissionFileState.UNENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.ENCRYPTED),
                new SubmissionStateChanged(SubmissionFileState.DECRYPTED),
                new SubmissionStateChanged(SubmissionFileState.SUBMITTED),
                new SubmissionStateChanged(SubmissionFileState.DELETED)
        )));
    }

    @Test
    @MediumTest
    public void retryIgnoresIrrelevantFiles() throws Exception {
        File[] irrelevantFiles = new File[] {
                new File(testFileFolder().getAbsolutePath() + "test.tull"),
                new File(testFileFolder().getAbsolutePath() + "irrelevant.junk"),
        };
        for (File file: irrelevantFiles) {
            file.createNewFile();
        }
        mobileNettskjema.forceRetryAllSubmissions();
        Thread.sleep(800);
        assertThat(emittedEvents.isEmpty(), is(true));
        for (File file: irrelevantFiles) {
            assertThat(file.exists(), is(true));
        }
        for (File file: irrelevantFiles) {
            file.delete();
        }
    }

    @Test
    @MediumTest
    public void retryDeletesTemporaryFiles() throws Exception {
        File[] irrelevantFiles = new File[] {
                new File(testFileFolder().getAbsolutePath() + "submission1.queueTemp"),
                new File(testFileFolder().getAbsolutePath() + "submission2.queueTemp"),
        };
        for (File file: irrelevantFiles) {
            file.createNewFile();
        }
        mobileNettskjema.forceRetryAllSubmissions();
        Thread.sleep(800);
        for (File file: irrelevantFiles) {
            assertThat(file.exists(), is(false));
        }
    }

    private void submitInvalidSubmission() throws MobileNettskjemaException {
       mobileNettskjema.addToSubmissionQueue(
                new NettskjemaFilledInForm(
                        TestForm.form,
                        Collections.<FilledInFormField>singletonList(new SelectedMultiOption(TestForm.radioQuestion, 0))
                )
       );
    }

    private File testFileFolder() {
        return  InstrumentationRegistry.getContext().getFileStreamPath(".");
    }

}
