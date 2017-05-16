/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.adapter.ws.da.application.config;

import com.alliander.osgp.domain.core.exceptions.PlatformException;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.osgpfoundation.osgp.adapter.ws.da.domain.repositories.RtuResponseDataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;


@EnableJpaRepositories(transactionManagerRef = "wsTransactionManager", entityManagerFactoryRef = "wsEntityManagerFactory", basePackageClasses = { RtuResponseDataRepository.class })
@Configuration
@PropertySources({ @PropertySource("classpath:osgp-adapter-ws-distributionautomation.properties"),
    @PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true),
    @PropertySource(value = "file:${osgp/AdapterWsDistributionAutomation/config}", ignoreResourceNotFound = true), })
public class PersistenceConfigWs extends AbstractPersistenceConfigBase {

    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
    private static final String PROPERTY_NAME_DATABASE_PW = "db.password";

    private static final String PROPERTY_NAME_DATABASE_HOST = "db.host";
    private static final String PROPERTY_NAME_DATABASE_PORT = "db.port";
    private static final String PROPERTY_NAME_DATABASE_NAME = "db.name";

    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    private static final String PROPERTY_NAME_FLYWAY_INITIAL_VERSION = "flyway.initial.version";
    private static final String PROPERTY_NAME_FLYWAY_INITIAL_DESCRIPTION = "flyway.initial.description";
    private static final String PROPERTY_NAME_FLYWAY_INIT_ON_MIGRATE = "flyway.init.on.migrate";

    public PersistenceConfigWs() {
        super("OSGP_WS_ADAPTER_DISTRIBUTION_AUTOMATION", PROPERTY_NAME_DATABASE_USERNAME, PROPERTY_NAME_DATABASE_PW,
                PROPERTY_NAME_DATABASE_HOST, PROPERTY_NAME_DATABASE_PORT, PROPERTY_NAME_DATABASE_NAME,
                PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN, PersistenceConfigWs.class);
    }

    @Bean(name = "wsTransactionManager")
    public JpaTransactionManager wsTransactionManager() throws PlatformException {
        return this.createTransactionManager();
    }

    @DependsOn("flyway")
    @Bean(name = "wsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean wsEntityManagerFactory() throws ClassNotFoundException {
        return this.createEntityManagerFactory();
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        final Flyway flyway = new Flyway();

        // Initialization for non-empty schema with no metadata table
        flyway.setBaselineVersion(MigrationVersion
                .fromVersion(this.environment.getRequiredProperty(PROPERTY_NAME_FLYWAY_INITIAL_VERSION)));
        flyway.setBaselineDescription(this.environment.getRequiredProperty(PROPERTY_NAME_FLYWAY_INITIAL_DESCRIPTION));
        flyway.setBaselineOnMigrate(
                Boolean.parseBoolean(this.environment.getRequiredProperty(PROPERTY_NAME_FLYWAY_INIT_ON_MIGRATE)));

        flyway.setDataSource(this.getDataSource());

        return flyway;
    }
}
