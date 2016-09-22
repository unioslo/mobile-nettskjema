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
package no.uio.mobileapps.mobilenettskjema.android.submission.answers;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import no.uio.mobileapps.mobilenettskjema.android.MobileNettskjemaException;
import no.uio.mobileapps.mobilenettskjema.android.submission.interfaces.FilledInFormField;
import no.uio.mobileapps.mobilenettskjema.android.submission.questions.UploadQuestion;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUploadForField implements FilledInFormField {
    private final String mediaType;
    private final byte[] fileContents;
    private final String filename;
    private final String identifier;

    public FileUploadForField(UploadQuestion uploadQuestion, File file, String mediaType) throws MobileNettskjemaException  {
        this.mediaType = mediaType;
        this.fileContents = contentsOf(file);
        this.identifier = uploadQuestion.identifier().name();
        this.filename = file.getName();
    }

    public FileUploadForField(JSONObject field) throws MobileNettskjemaException {
        try {
            this.mediaType = field.getString("mediaType");
            this.identifier = field.getString("identifier");
            this.filename = field.getString("name");
            this.fileContents = Base64.decode(field.getString("base64"), Base64.DEFAULT);
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    @Override
    public void addToBuilder(MultipartBody.Builder bodyBuilder) {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse(mediaType), fileContents);
        bodyBuilder.addFormDataPart(identifier, filename, fileRequestBody);
    }

    private byte[] contentsOf(File file) throws MobileNettskjemaException {
        try {
            RandomAccessFile f = new RandomAccessFile(file.getAbsolutePath(), "r");
            byte[] b = new byte[(int) file.length()];
            f.readFully(b);
            return b;
        } catch (IOException e) {
            throw new MobileNettskjemaException(e);
        }
    }

    @Override
    public JSONObject serialized() throws MobileNettskjemaException {
        JSONObject fileObject = new JSONObject();
        try {
            fileObject.put("name", filename);
            fileObject.put("mediaType", mediaType);
            fileObject.put("base64", Base64.encodeToString(fileContents, Base64.DEFAULT));
            fileObject.put("identifier", identifier);
        } catch (JSONException e) {
            throw new MobileNettskjemaException(e);
        }
        return fileObject;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FileUploadForField)) return false;
        if (object == this) return true;
        FileUploadForField other = (FileUploadForField) object;
        return this.identifier.equals(other.identifier)
                && this.mediaType.equals(other.mediaType)
                && this.filename.equals(other.filename)
                && Arrays.equals(this.fileContents, other.fileContents);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + identifier.hashCode();
        result = 31 * result + mediaType.hashCode();
        result = 31 * result + filename.hashCode();
        result = 31 * result + Arrays.hashCode(fileContents);
        return result;
    }
}
