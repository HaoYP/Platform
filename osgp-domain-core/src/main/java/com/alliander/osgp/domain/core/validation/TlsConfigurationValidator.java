/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.alliander.osgp.domain.core.valueobjects.Configuration;

public class TlsConfigurationValidator implements ConstraintValidator<TlsConfiguration, Configuration> {

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(final TlsConfiguration constraintAnnotation) {
        // No initialization needed.
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     * javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final Configuration value, final ConstraintValidatorContext context) {
        if (value.isTlsEnabled() == null && value.getTlsPortNumber() == null && value.getCommonNameString() == null) {
            // If non of the TLS variables are set, the configuration is valid.
            return true;
        }
        if (!value.isTlsEnabled() && this.checkTlsPortNumber(value.getTlsPortNumber())) {
            // If TLS is disabled, the configuration is valid.
            // The port number should be valid as well.
            return true;
        }
        if (value.isTlsEnabled() && this.checkTlsPortNumber(value.getTlsPortNumber())) {
            // If TLS is enabled and the port number is valid, the configuration
            // is valid.
            return true;
        }
        return false;
    }

    private boolean checkTlsPortNumber(final Integer tlsPortNumber) {
        if (tlsPortNumber == null) {
            return true;
        }
        if (tlsPortNumber > 0 && tlsPortNumber <= 65535) {
            return true;
        }
        return false;
    }
}
