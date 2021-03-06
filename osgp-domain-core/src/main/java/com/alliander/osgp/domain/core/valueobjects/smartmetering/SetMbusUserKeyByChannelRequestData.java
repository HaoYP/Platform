/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;

public class SetMbusUserKeyByChannelRequestData implements Serializable, ActionRequest {

    private static final long serialVersionUID = 3484509683303452336L;

    private static final short MIN_CHANNEL = 1;
    private static final short MAX_CHANNEL = 4;

    private final short channel;

    public SetMbusUserKeyByChannelRequestData(final short channel) {
        this.channel = channel;
    }

    public short getChannel() {
        return this.channel;
    }

    @Override
    public void validate() throws FunctionalException {
        if (this.channel < MIN_CHANNEL || this.channel > MAX_CHANNEL) {
            throw new FunctionalException(FunctionalExceptionType.VALIDATION_ERROR, ComponentType.DOMAIN_SMART_METERING,
                    new IllegalArgumentException(
                            "Channel not in range [" + MIN_CHANNEL + ".." + MAX_CHANNEL + "]: " + this.channel));
        }
    }

    @Override
    public DeviceFunction getDeviceFunction() {
        return DeviceFunction.SET_MBUS_USER_KEY_BY_CHANNEL;
    }

}
