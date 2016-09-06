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
import no.uio.mobileapps.reactnativenettskjema.interfaces.FormSubmission;
import okhttp3.OkHttpClient;

public class NettskjemaFormSubmissionFactory {
    public FormSubmission newSubmission(long formId, Iterable<FilledInFormField> filledInFormFields) {
        return new NettskjemaFormSubmission(
                new OkHttpClient.Builder().cookieJar(
                        new NettskjemaCookieJar()
                ).build(),
                new NettskjemaCsrfRequestFactory(),
                new NettskjemaCsrfTokenFactory(),
                new NettskjemaPostRequestFactory(
                        new NettskjemaForm(formId),
                        filledInFormFields)
                );
    }
}
