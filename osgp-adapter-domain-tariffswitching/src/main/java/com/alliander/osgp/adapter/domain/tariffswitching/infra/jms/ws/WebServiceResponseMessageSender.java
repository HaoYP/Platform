/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.tariffswitching.infra.jms.ws;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alliander.osgp.shared.infra.jms.Constants;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;
import com.alliander.osgp.shared.infra.jms.ResponseMessageSender;

// Send response message to the web service adapter.
//@Component(value = "domainTariffSwitchingOutgoingWebServiceResponseMessageSender")
public class WebServiceResponseMessageSender implements ResponseMessageSender {

    @Autowired
    @Qualifier("domainTariffSwitchingOutgoingWebServiceResponsesJmsTemplate")
    private JmsTemplate outgoingWebServiceResponsesJmsTemplate;

    @Override
    public void send(final ResponseMessage responseMessage) {

        this.outgoingWebServiceResponsesJmsTemplate.send(new MessageCreator() {

            @Override
            public Message createMessage(final Session session) throws JMSException {
                final ObjectMessage objectMessage = session.createObjectMessage(responseMessage);
                objectMessage.setJMSPriority(responseMessage.getMessagePriority());
                objectMessage.setJMSCorrelationID(responseMessage.getCorrelationUid());
                objectMessage.setStringProperty(Constants.ORGANISATION_IDENTIFICATION,
                        responseMessage.getOrganisationIdentification());
                objectMessage.setStringProperty(Constants.DEVICE_IDENTIFICATION,
                        responseMessage.getDeviceIdentification());
                objectMessage.setStringProperty(Constants.RESULT, responseMessage.getResult().toString());
                if (responseMessage.getOsgpException() != null) {
                    objectMessage.setStringProperty(Constants.DESCRIPTION,
                            responseMessage.getOsgpException().getMessage());
                }
                return objectMessage;
            }
        });
    }
}
