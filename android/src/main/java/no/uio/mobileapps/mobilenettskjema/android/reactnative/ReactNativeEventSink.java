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
package no.uio.mobileapps.mobilenettskjema.android.reactnative;

import android.support.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.interfaces.Event;
import no.uio.mobileapps.mobilenettskjema.interfaces.EventSink;

class ReactNativeEventSink implements EventSink {

    private final String prefix;
    private final ReactContext reactContext;

    ReactNativeEventSink(ReactContextBaseJavaModule module, ReactContext reactContext) {
        this.prefix = module.getName();
        this.reactContext = reactContext;
    }

    @Override
    public void put(Event event) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(emittedEventName(event), null);
    }

    @NonNull
    private String emittedEventName(Event event) {
        return prefix + ":" + event.name();
    }
}
