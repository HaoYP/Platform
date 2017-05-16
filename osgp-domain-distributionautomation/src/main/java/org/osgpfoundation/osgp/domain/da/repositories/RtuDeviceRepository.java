/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.domain.da.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.osgpfoundation.osgp.domain.da.entities.RtuDevice;

@Repository
public interface RtuDeviceRepository extends JpaRepository<RtuDevice, Long> {
    RtuDevice findById(long id);

    RtuDevice findByDeviceIdentification(String deviceIdentification);

    List<RtuDevice> findByIsActiveAndLastCommunicationTimeBefore(boolean active, Date lastCommunicationTime);
}
