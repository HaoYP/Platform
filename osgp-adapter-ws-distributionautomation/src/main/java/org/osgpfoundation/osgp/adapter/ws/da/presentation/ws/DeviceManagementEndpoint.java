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
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusResponse;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusRequest;
import org.osgpfoundation.osgp.adapter.ws.schema.distributionautomation.generic.GetHealthStatusAsyncResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class DeviceManagementEndpoint extends GenericDistributionAutomationEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManagementEndpoint.class);
    protected static final String NAMESPACE = "http://www.osgpfoundation.org/schemas/osgp/distributionautomation/defs/2017/04";

    @PayloadRoot(localPart = "GetHealthStatusRequest",
            namespace = NAMESPACE)
    @ResponsePayload
    public GetHealthStatusAsyncResponse getHealthStatus(@OrganisationIdentification final String organisationIdentification,
                                                        @RequestPayload final GetHealthStatusRequest request) throws OsgpException {

        LOGGER.info("Get Health Status Request received from organisation: {} for device: {}.", organisationIdentification,
                request.getDeviceIdentification());

        final GetHealthStatusAsyncResponse response = new GetHealthStatusAsyncResponse();

        try {
            final org.osgpfoundation.osgp.domain.da.valueobjects.GetHealthStatusRequest getHealthStatusRequest = this.mapper.map(request, org.osgpfoundation.osgp.domain.da.valueobjects.GetHealthStatusRequest.class);
            final String correlationUid = this.service
                    .enqueueGetHealthStatusRequest(organisationIdentification, request.getDeviceIdentification(), getHealthStatusRequest);
            final AsyncResponse asyncResponse = new AsyncResponse();
            asyncResponse.setCorrelationUid(correlationUid);
            asyncResponse.setDeviceId(request.getDeviceIdentification());
            response.setAsyncResponse(asyncResponse);
        } catch (final Exception e) {
            this.handleException(LOGGER, e);
        }
        return response;
    }

    @PayloadRoot(localPart = "GetHealthStatusAsyncRequest",
            namespace = NAMESPACE)
    @ResponsePayload
    public GetHealthStatusResponse getHealthStatusResponse(@OrganisationIdentification final String organisationIdentification,
                                                           @RequestPayload final GetHealthStatusAsyncRequest request) throws OsgpException {

        LOGGER.info("Get Health Status Response received from organisation: {} for correlationUid: {}.", organisationIdentification,
                request.getAsyncRequest().getCorrelationUid());

        GetHealthStatusResponse response = new GetHealthStatusResponse();
        try {
            final org.osgpfoundation.osgp.domain.da.valueobjects.GetHealthStatusResponse healthStatusResponse = this.service
                    .dequeueGetHealthResponse(request.getAsyncRequest().getCorrelationUid());
            if (healthStatusResponse != null) {
                response = this.mapper.map(healthStatusResponse, GetHealthStatusResponse.class);
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
