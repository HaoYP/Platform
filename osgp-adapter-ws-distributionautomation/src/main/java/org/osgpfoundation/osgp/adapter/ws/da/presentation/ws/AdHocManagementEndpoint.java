/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.adapter.ws.da.presentation.ws;

import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import org.osgpfoundation.osgp.adapter.ws.da.application.exceptionhandling.ResponseNotFoundException;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.common.AsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.common.OsgpResultType;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelAsyncResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetDeviceModelResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AdHocManagementEndpoint extends GenericDistributionAutomationEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdHocManagementEndpoint.class);

    @PayloadRoot(localPart = "GetDeviceModelRequest",
            namespace = NAMESPACE)
    @ResponsePayload
    public GetDeviceModelAsyncResponse getDeviceModel(@OrganisationIdentification final String organisationIdentification,
                                                      @RequestPayload final GetDeviceModelRequest request) throws OsgpException {

        LOGGER.info("Get Device Model Request received from organisation: {} for device: {}.", organisationIdentification,
                request.getDeviceIdentification());
        GetDeviceModelAsyncResponse response = new GetDeviceModelAsyncResponse();
        try {
            final org.osgpfoundation.osgp.domain.da.valueobjects.GetDeviceModelRequest getDeviceModelRequest = this.mapper
                    .map(request, org.osgpfoundation.osgp.domain.da.valueobjects.GetDeviceModelRequest.class);
            final String correlationUid = this.service
                    .enqueueGetDeviceModelRequest(organisationIdentification, request.getDeviceIdentification(), getDeviceModelRequest);
            final AsyncResponse asyncResponse = new AsyncResponse();
            asyncResponse.setCorrelationUid(correlationUid);
            asyncResponse.setDeviceId(request.getDeviceIdentification());
            response.setAsyncResponse(asyncResponse);
        } catch (final Exception e) {
            this.handleException(LOGGER, e);
        }
        return response;
    }

    @PayloadRoot(localPart = "GetDeviceModelAsyncRequest",
            namespace = NAMESPACE)
    @ResponsePayload
    public GetDeviceModelResponse getDeviceModelResponse(@OrganisationIdentification final String organisationIdentification,
                                                         @RequestPayload final GetDeviceModelAsyncRequest request) throws OsgpException {

        LOGGER.info("Get Device Model Response received from organisation: {} for correlationUid: {}.", organisationIdentification,
                request.getAsyncRequest().getCorrelationUid());

        GetDeviceModelResponse response = new GetDeviceModelResponse();
        try {
            final org.osgpfoundation.osgp.domain.da.valueobjects.GetDeviceModelResponse dataResponse = this.service
                    .dequeueGetDeviceModelResponse(request.getAsyncRequest().getCorrelationUid());
            if (dataResponse != null) {
                response = this.mapper.map(dataResponse, GetDeviceModelResponse.class);
                response.setResult(OsgpResultType.OK);
            } else {

                response.setResult(OsgpResultType.NOT_FOUND);
            }
        } catch (final ResponseNotFoundException e) {
            LOGGER.warn("ResponseNotFoundException for getDeviceModel", e);
            response.setResult(OsgpResultType.NOT_FOUND);
        } catch (final Exception e) {
            this.handleException(LOGGER, e);
        }
        response.setDeviceIdentification(request.getAsyncRequest().getDeviceId());
        return response;
    }
}
