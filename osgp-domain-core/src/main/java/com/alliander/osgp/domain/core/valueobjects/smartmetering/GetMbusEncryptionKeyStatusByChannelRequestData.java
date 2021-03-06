/**
 * Copyright 2018 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;

public class GetMbusEncryptionKeyStatusByChannelRequestData implements ActionRequest {

    private static final long serialVersionUID = 1714006845592365110L;
    private final short channel;

    public GetMbusEncryptionKeyStatusByChannelRequestData(final short channel) {
        this.channel = channel;
    }

    @Override
    public void validate() throws FunctionalException {
        // No validation needed
    }

    @Override
    public DeviceFunction getDeviceFunction() {
        return DeviceFunction.GET_MBUS_ENCRYPTION_KEY_STATUS_BY_CHANNEL;
    }

    public short getChannel() {
        return this.channel;
    }

}
