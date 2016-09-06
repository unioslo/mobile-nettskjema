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
package no.uio.mobileapps.reactnativenettskjema.answers;

import java.io.File;

import no.uio.mobileapps.reactnativenettskjema.interfaces.FilledInFormField;
import no.uio.mobileapps.reactnativenettskjema.questions.UploadQuestion;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUploadForField implements FilledInFormField {
    private final String mediaType;
    private final File file;
    private final String identifier;

    public FileUploadForField(UploadQuestion uploadQuestion, File file, String mediaType) {
        this.mediaType = mediaType;
        this.file = file;
        this.identifier = uploadQuestion.identifier().name();
    }

    @Override
    public void addToBuilder(MultipartBody.Builder bodyBuilder) {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse(mediaType), file);
        bodyBuilder.addFormDataPart(identifier, file.getName(), fileRequestBody);
    }
}
