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
package no.uio.mobileapps.mobilenettskjema.android.testdata;

import java.util.Arrays;

import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.Form;
import no.uio.mobileapps.mobilenettskjema.android.submission.nettskjema.NettskjemaForm;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.MultipleChoiceQuestion;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.RadioQuestion;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.TextQuestion;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.UploadQuestion;

public class TestForm {
    public static Long formId = 75319L;
    public static Form form = new NettskjemaForm(formId);
    public static TextQuestion textQuestion = new TextQuestion(577795L);
    public static RadioQuestion radioQuestion = new RadioQuestion(578095L, Arrays.asList(1275519L, 1275520L));
    public static MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(578096L, Arrays.asList(1275521L, 1275522L, 1275523L));
    public static UploadQuestion uploadField1 = new UploadQuestion(577797L);
    public static UploadQuestion uploadField2 = new UploadQuestion(577798L);

}
