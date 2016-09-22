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
package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.events;

import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.Event;
import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.submissionstates
        .SubmissionFileState;

public class SubmissionStateChanged implements Event {

    private final String description;

    public SubmissionStateChanged(SubmissionFileState submissionFileState) {
        this.description = submissionFileState.toString();
    }

    @Override
    public String name() {
        return "SubmissionStateChanged";
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SubmissionStateChanged)) return false;
        if (object == this) return true;
        SubmissionStateChanged other = (SubmissionStateChanged) object;
        return this.description.equals(other.description);
    }

    @Override
    public String toString() {
        return "[SubmissionStateChanged]: " + description;
    }
}
