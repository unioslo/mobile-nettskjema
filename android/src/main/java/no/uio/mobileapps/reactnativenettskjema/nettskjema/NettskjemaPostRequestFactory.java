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
package no.uio.mobileapps.reactnativenettskjema.nettskjema;

import no.uio.mobileapps.reactnativenettskjema.interfaces.FilledInFormField;
import no.uio.mobileapps.reactnativenettskjema.interfaces.Form;
import no.uio.mobileapps.reactnativenettskjema.interfaces.SubmissionRequestFactory;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

class NettskjemaPostRequestFactory implements SubmissionRequestFactory {

    private final Form form;
    private final Iterable<FilledInFormField> filledInFormFields;

    public NettskjemaPostRequestFactory(Form form, Iterable
            <FilledInFormField> filledInFormFields) {
        this.form = form;
        this.filledInFormFields = filledInFormFields;
    }

    @Override
    public Request newRequest(FilledInFormField csrfToken) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        csrfToken.addToBuilder(bodyBuilder);
        for (FilledInFormField field: filledInFormFields) {
            field.addToBuilder(bodyBuilder);
        }
        RequestBody requestBody = bodyBuilder.build();
        return new Request.Builder().url(form.postUrl()).post(requestBody).build();
    }
}
