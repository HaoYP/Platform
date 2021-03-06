/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.shared.services;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.adapter.ws.domain.repositories.ResponseDataRepository;

@Service
@Transactional(value = "transactionManager")
public class ResponseDataCleanupService {

    @Autowired
    private ResponseDataRepository responseDataRepository;

    @Autowired
    private int cleanupJobRetentionTimeInDays;

    public void execute() {

        final DateTime removeBeforeDateTime = DateTime.now(DateTimeZone.UTC)
                .minusDays(this.cleanupJobRetentionTimeInDays);
        this.responseDataRepository.removeByCreationTimeBefore(removeBeforeDateTime.toDate());
    }
}
