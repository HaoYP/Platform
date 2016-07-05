/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.shared.db.domain.repositories.writable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alliander.osgp.domain.core.entities.DeviceModel;
import com.alliander.osgp.domain.core.entities.DeviceModelFirmware;

@Repository
public interface WritableDeviceModelFirmwareRepository extends JpaRepository<DeviceModelFirmware, Long> {
    List<DeviceModelFirmware> findByDeviceModel(DeviceModel deviceModel);

    DeviceModelFirmware findById(Long id);

    List<DeviceModelFirmware> findByDeviceModelAndFilename(DeviceModel deviceModel, String fileName);
}