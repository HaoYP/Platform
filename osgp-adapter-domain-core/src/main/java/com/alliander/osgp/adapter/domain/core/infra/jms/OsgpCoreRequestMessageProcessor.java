/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.core.infra.jms;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageSender;
import com.alliander.osgp.shared.infra.jms.RequestMessage;
import com.alliander.osgp.shared.infra.jms.UnknownMessageTypeException;

@Component(value = "domainCoreIncomingOsgpCoreRequestsMessageProcessor")
public class OsgpCoreRequestMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsgpCoreRequestMessageProcessor.class);

    @Qualifier("domainCoreWebServiceRequestsMessageSender")
    @Autowired
    private WebServiceRequestMessageSender webServiceRequestMessageSender;

    public void processMessage(final RequestMessage requestMessage, final String messageType)
            throws UnknownMessageTypeException {

        final String organisationIdentification = requestMessage.getOrganisationIdentification();
        final String deviceIdentification = requestMessage.getDeviceIdentification();
        final String correlationUid = requestMessage.getCorrelationUid();
        final Serializable dataObject = requestMessage.getRequest();

        LOGGER.info(
                "Received request message from OSGP-CORE messageType: {} deviceIdentification: {}, organisationIdentification: {}, correlationUid: {}, dataObject.className: {}",
                messageType, deviceIdentification, organisationIdentification, correlationUid,
                dataObject == null ? "null" : dataObject.getClass().getCanonicalName());

        if ("EVENTS_FOR_DEVICE".equals(messageType)) {
            final RequestMessage requestMsg = new RequestMessage(correlationUid, organisationIdentification,
                    deviceIdentification, dataObject);
            this.webServiceRequestMessageSender.send(requestMsg, messageType);
        } else {
            throw new UnknownMessageTypeException("Unknown JMSType: " + messageType);
        }
    }
}
