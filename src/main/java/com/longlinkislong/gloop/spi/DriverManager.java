/* 
 * Copyright (c) 2016, longlinkislong.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.longlinkislong.gloop.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * A Manager object that supplies driver instances as requested.
 *
 * @author zmichaels
 * @since 16.03.08
 */
public final class DriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
    private static final Marker MARKER = MarkerFactory.getMarker("gloop-spi");

    private final ServiceLoader<DriverProvider> driverLoader;

    /**
     * Retrieves an unordered set of all supported drivers.
     *
     * @return the set of all supported drivers.
     * @since 16.03.08
     */
    public Set<DriverProvider> getSupportedDrivers() {
        return getAllDrivers().stream()
                .filter(DriverProvider::isSupported)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves an unordered set of all drivers.
     *
     * @return the set of all drivers.
     * @since 16.03.08
     */
    public Set<DriverProvider> getAllDrivers() {
        final Set<DriverProvider> out = new HashSet<>();

        for (DriverProvider testDriver : this.driverLoader) {
            out.add(testDriver);
        }

        return out;
    }

    /**
     * Selects the best supported driver.
     *
     * @return the best supported driver wrapped in an Optional. May be empty if
     * no drivers are supported.
     * @since 16.03.08
     */
    public Optional<Driver> selectBestDriver() {
        DriverProvider bestDriver = null;

        for (DriverProvider testDriver : driverLoader) {
            if (testDriver.isSupported()) {
                if (bestDriver == null) {
                    bestDriver = testDriver;
                } else if (testDriver.getSupportRating() > bestDriver.getSupportRating()) {
                    LOGGER.debug(
                            MARKER,
                            "Selecting [{}] over [{}] as best driver",
                            testDriver.getDriverName(),
                            bestDriver.getDriverName());
                    bestDriver = testDriver;
                }
            }
        }

        if (bestDriver == null) {
            return Optional.empty();
        } else {
            LOGGER.info(MARKER, "Selected driver: [{}]", bestDriver.getDriverName());
            return Optional.of(bestDriver.getDriverInstance());
        }
    }

    /**
     * Selects a driver by name.
     *
     * @param driverName
     * @return the driver wrapped in an Optional. The optional may be empty if
     * no drivers match.
     * @since 16.03.08
     */
    public Optional<Driver> selectDriverByName(final String driverName) {
        for (DriverProvider testDriver : driverLoader) {
            if (testDriver.getDriverName().equalsIgnoreCase(driverName)) {
                if (testDriver.isSupported()) {
                    return Optional.of(testDriver.getDriverInstance());
                } else {
                    LOGGER.warn(MARKER, "Selected driver [{}] is not supported!", driverName);
                    return Optional.of(testDriver.getDriverInstance());
                }
            }
        }

        LOGGER.warn(MARKER, "No drivers were found with name: [{}]!", driverName);
        return Optional.empty();
    }

    /**
     * Selects the driver that best matches the description.
     *
     * @param descriptions the list of Strings that are expected to match.
     * @return the driver wrapped in an Optional. The Optional may be empty if
     * no drivers match.
     * @since 16.03.10
     */
    public Optional<Driver> selectDriverByDescription(final String... descriptions) {
        final List<DriverProvider> matches = new ArrayList<>();        
        final List<String> descList = Arrays.asList(descriptions);

        for (DriverProvider testDriver : driverLoader) {
            final Set<String> mustMatch = new HashSet<>();
            
            mustMatch.addAll(testDriver.getDriverDescription());
            
            if (testDriver.isSupported()) {
                if (mustMatch.containsAll(descList)) {
                    matches.add(testDriver);
                }
            }
        }

        DriverProvider bestDriver = null;
        for (DriverProvider testDriver : matches) {
            if (bestDriver == null || testDriver.getSupportRating() > bestDriver.getSupportRating()) {
                bestDriver = testDriver;
            }
        }

        if (bestDriver != null) {
            return Optional.of(bestDriver.getDriverInstance());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Constructs a new DriverFactory using the default system class loader.
     *
     * @since 16.03.08
     */
    public DriverManager() {
        this.driverLoader = ServiceLoader.load(DriverProvider.class);
    }

    /**
     * Constructs a new DriverFactory using the specified ClassLoader.
     *
     * @param loader the ClassLoader to use.
     * @since 16.03.08
     */
    public DriverManager(final ClassLoader loader) {
        this.driverLoader = ServiceLoader.load(DriverProvider.class, loader);
    }
}
