/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.tariffswitching.infra.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alliander.osgp.shared.infra.jms.Constants;

/**
 * Class for sending tariff switching request messages to a queue
 */
public class TariffSwitchingRequestMessageSender {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TariffSwitchingRequestMessageSender.class);

    /**
     * Autowired field for tariff switching requests jms template
     */
    @Autowired
    @Qualifier("wsTariffSwitchingOutgoingRequestsJmsTemplate")
    private JmsTemplate tariffSwitchingRequestsJmsTemplate;

    /**
     * Method for sending a request message to the queue
     *
     * @param requestMessage
     *            The TariffSwitchingRequestMessage request message to send.
     */
    public void send(final TariffSwitchingRequestMessage requestMessage) {
        LOGGER.debug("Sending tariff switching request message to the queue");

        if (requestMessage.getMessageType() == null) {
            LOGGER.error("MessageType is null");
            return;
        }
        if (StringUtils.isBlank(requestMessage.getOrganisationIdentification())) {
            LOGGER.error("OrganisationIdentification is blank");
            return;
        }
        if (StringUtils.isBlank(requestMessage.getDeviceIdentification())) {
            LOGGER.error("DeviceIdentification is blank");
            return;
        }
        if (StringUtils.isBlank(requestMessage.getCorrelationUid())) {
            LOGGER.error("CorrelationUid is blank");
            return;
        }

        this.sendMessage(requestMessage);
    }

    /**
     * Method for sending a request message to the public lighting requests
     * queue
     *
     * @param requestMessage
     *            The TariffSwitchingRequestMessage request message to send.
     */
    private void sendMessage(final TariffSwitchingRequestMessage requestMessage) {
        LOGGER.info("Sending message to the tariff switching requests queue");

        this.tariffSwitchingRequestsJmsTemplate.send(new MessageCreator() {

            @Override
            public Message createMessage(final Session session) throws JMSException {
                final ObjectMessage objectMessage = session.createObjectMessage(requestMessage.getRequest());
                objectMessage.setJMSCorrelationID(requestMessage.getCorrelationUid());
                objectMessage.setJMSType(requestMessage.getMessageType().toString());
                objectMessage.setJMSPriority(requestMessage.getMessagePriority());
                objectMessage.setStringProperty(Constants.ORGANISATION_IDENTIFICATION,
                        requestMessage.getOrganisationIdentification());
                objectMessage.setStringProperty(Constants.DEVICE_IDENTIFICATION,
                        requestMessage.getDeviceIdentification());
                if (requestMessage.getScheduleTime() != null) {
                    objectMessage.setLongProperty(Constants.SCHEDULE_TIME,
                            requestMessage.getScheduleTime().getMillis());
                }
                return objectMessage;
            }

        });
    }
}
