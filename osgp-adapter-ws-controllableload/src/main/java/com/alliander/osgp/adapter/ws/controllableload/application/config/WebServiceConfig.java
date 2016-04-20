/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.controllableload.application.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;

import com.alliander.osgp.adapter.ws.controllableload.application.exceptionhandling.DetailSoapFaultMappingExceptionResolver;
import com.alliander.osgp.adapter.ws.controllableload.application.exceptionhandling.SoapFaultMapper;
import com.alliander.osgp.adapter.ws.endpointinterceptors.AnnotationMethodArgumentResolver;
import com.alliander.osgp.adapter.ws.endpointinterceptors.CertificateAndSoapHeaderAuthorizationEndpointInterceptor;
import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.adapter.ws.endpointinterceptors.SoapHeaderEndpointInterceptor;
import com.alliander.osgp.adapter.ws.endpointinterceptors.WebServiceMonitorInterceptor;
import com.alliander.osgp.adapter.ws.endpointinterceptors.X509CertificateRdnAttributeValueEndpointInterceptor;

@Configuration
@PropertySource("file:${osp/osgpAdapterWsControllableLoad/config}")
public class WebServiceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceConfig.class);

    private static final String PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_CONTROLLABLE_LOAD_AD_HOC_MANAGEMENT = "jaxb2.marshaller.context.path.controllableload.adhocmanagement";

    private static final String ORGANISATION_IDENTIFICATION_HEADER = "OrganisationIdentification";
    private static final String ORGANISATION_IDENTIFICATION_CONTEXT = ORGANISATION_IDENTIFICATION_HEADER;

    private static final String USER_NAME_HEADER = "UserName";

    private static final String APPLICATION_NAME_HEADER = "ApplicationName";

    private static final String X509_RDN_ATTRIBUTE_ID = "cn";
    private static final String X509_RDN_ATTRIBUTE_VALUE_CONTEXT_PROPERTY_NAME = "CommonNameSet";

    @Resource
    private Environment environment;

    // === CONTROLLABLE LOAD MARSHALLERS ===

    /**
     * Method for creating the Marshaller for controllable load ad hoc
     * management.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller controllableLoadAdHocManagementMarshaller() {
        LOGGER.debug("Creating Controllable Load Ad Hoc Management JAXB 2 Marshaller Bean");

        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.environment
                .getRequiredProperty(PROPERTY_NAME_MARSHALLER_CONTEXT_PATH_CONTROLLABLE_LOAD_AD_HOC_MANAGEMENT));

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for
     * controllable load ad hoc management.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor controllableLoadAdHocManagementMarshallingPayloadMethodProcessor() {
        LOGGER.debug("Creating Controllable Load Ad Hoc Management Marshalling Payload Method Processor Bean");

        return new MarshallingPayloadMethodProcessor(this.controllableLoadAdHocManagementMarshaller(),
                this.controllableLoadAdHocManagementMarshaller());
    }

    /**
     * Method for creating the Default Method Endpoint Adapter.
     *
     * @return DefaultMethodEndpointAdapter
     */
    @Bean
    public DefaultMethodEndpointAdapter defaultMethodEndpointAdapter() {
        LOGGER.debug("Creating Default Method Endpoint Adapter Bean");

        final DefaultMethodEndpointAdapter defaultMethodEndpointAdapter = new DefaultMethodEndpointAdapter();

        final List<MethodArgumentResolver> methodArgumentResolvers = new ArrayList<MethodArgumentResolver>();

        // Add Controllable Load Marshalling Payload Method Processors to Method
        // Argument Resolvers
        methodArgumentResolvers.add(this.controllableLoadAdHocManagementMarshallingPayloadMethodProcessor());

        // Add Organisation Identification Annotation Method Argument Resolver
        methodArgumentResolvers.add(new AnnotationMethodArgumentResolver(ORGANISATION_IDENTIFICATION_CONTEXT,
                OrganisationIdentification.class));
        defaultMethodEndpointAdapter.setMethodArgumentResolvers(methodArgumentResolvers);

        final List<MethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<MethodReturnValueHandler>();

        // Add Controllable Load Marshalling Payload Method Processors to Method
        // Return Value Handlers
        methodReturnValueHandlers.add(this.controllableLoadAdHocManagementMarshallingPayloadMethodProcessor());

        defaultMethodEndpointAdapter.setMethodReturnValueHandlers(methodReturnValueHandlers);

        return defaultMethodEndpointAdapter;
    }

    @Bean
    public DetailSoapFaultMappingExceptionResolver exceptionResolver() {
        LOGGER.debug("Creating Detail Soap Fault Mapping Exception Resolver Bean");
        final DetailSoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultMappingExceptionResolver(
                new SoapFaultMapper());
        exceptionResolver.setOrder(1);

        final Properties props = new Properties();
        props.put("com.alliander.osgp.shared.exceptionhandling.FunctionalException", "SERVER");
        props.put("com.alliander.osgp.shared.exceptionhandling.TechnicalException", "SERVER");
        props.put("com.alliander.osgp.shared.exceptionhandling.ConnectionFailureException", "SERVER");

        exceptionResolver.setExceptionMappings(props);
        return exceptionResolver;
    }

    // === ENDPOINT INTERCEPTORS ===

    /**
     * @return
     */
    @Bean
    public X509CertificateRdnAttributeValueEndpointInterceptor x509CertificateSubjectCnEndpointInterceptor() {
        return new X509CertificateRdnAttributeValueEndpointInterceptor(X509_RDN_ATTRIBUTE_ID,
                X509_RDN_ATTRIBUTE_VALUE_CONTEXT_PROPERTY_NAME);
    }

    /**
     * @return
     */
    @Bean
    public SoapHeaderEndpointInterceptor organisationIdentificationInterceptor() {
        return new SoapHeaderEndpointInterceptor(ORGANISATION_IDENTIFICATION_HEADER,
                ORGANISATION_IDENTIFICATION_CONTEXT);
    }

    /**
     * @return
     */
    @Bean
    public CertificateAndSoapHeaderAuthorizationEndpointInterceptor organisationIdentificationInCertificateCnEndpointInterceptor() {
        return new CertificateAndSoapHeaderAuthorizationEndpointInterceptor(
                X509_RDN_ATTRIBUTE_VALUE_CONTEXT_PROPERTY_NAME, ORGANISATION_IDENTIFICATION_CONTEXT);
    }

    @Bean
    public WebServiceMonitorInterceptor webServiceMonitorInterceptor() {
        return new WebServiceMonitorInterceptor(ORGANISATION_IDENTIFICATION_HEADER, USER_NAME_HEADER,
                APPLICATION_NAME_HEADER);
    }

}
