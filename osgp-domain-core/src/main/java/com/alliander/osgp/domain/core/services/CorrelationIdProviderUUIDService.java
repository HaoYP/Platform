/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class CorrelationIdProviderUUIDService implements CorrelationIdProviderService {

    @Override
    public String getCorrelationId(final String organisationIdentification, final String deviceIdentification) {
        return organisationIdentification + "|||" + deviceIdentification + "|||" + UUID.randomUUID().toString();
    }
}