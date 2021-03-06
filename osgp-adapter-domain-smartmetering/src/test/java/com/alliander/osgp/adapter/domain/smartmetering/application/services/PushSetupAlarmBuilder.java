/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.domain.smartmetering.application.services;

import java.util.ArrayList;
import java.util.List;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObisCode;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObjectDefinition;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.MessageType;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PushSetupAlarm;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SendDestinationAndMethod;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.TransportServiceType;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.WindowElement;

public class PushSetupAlarmBuilder {

    private CosemObisCode logicalName = new CosemObisCode(new int[] { 1, 2, 3, 4, 5, 6 });
    private TransportServiceType transportServiceType = TransportServiceType.TCP;
    private MessageType messageType = MessageType.A_XDR_ENCODED_X_DLMS_APDU;
    private SendDestinationAndMethod sendDestinationAndMethod = new SendDestinationAndMethod(this.transportServiceType,
            "destination", this.messageType);
    private Integer randomisationStartInterval = new Integer(1);
    private Integer numberOfRetries = new Integer(10);
    private Integer repetitionDelay = new Integer(2);

    private List<CosemObjectDefinition> pushObjectList;
    private List<WindowElement> communicationWindow;

    public PushSetupAlarm build() {

        final PushSetupAlarm.Builder pushSetupAlarmBuilder = new PushSetupAlarm.Builder();
        pushSetupAlarmBuilder.withLogicalName(this.logicalName).withPushObjectList(this.pushObjectList)
                .withSendDestinationAndMethod(this.sendDestinationAndMethod)
                .withCommunicationWindow(this.communicationWindow)
                .withRandomisationStartInterval(this.randomisationStartInterval)
                .withNumberOfRetries(this.numberOfRetries).withRepetitionDelay(this.repetitionDelay);
        return pushSetupAlarmBuilder.build();
    }

    public PushSetupAlarmBuilder withNullValues() {
        this.logicalName = null;
        this.pushObjectList = null;
        this.sendDestinationAndMethod = null;
        this.communicationWindow = null;
        this.randomisationStartInterval = null;
        this.numberOfRetries = null;
        this.repetitionDelay = null;
        return this;
    }

    public PushSetupAlarmBuilder withEmptyLists(final ArrayList<CosemObjectDefinition> pushObjectList,
            final ArrayList<WindowElement> communicationWindow) {
        this.pushObjectList = pushObjectList;
        this.communicationWindow = communicationWindow;
        return this;
    }

    public PushSetupAlarmBuilder withFilledLists(final CosemObjectDefinition cosemObjectDefinition,
            final WindowElement windowElement) {
        this.pushObjectList = new ArrayList<>();
        this.pushObjectList.add(cosemObjectDefinition);
        this.communicationWindow = new ArrayList<>();
        this.communicationWindow.add(windowElement);
        return this;
    }
}
