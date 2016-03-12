/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.alspi;

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
 *
 * @author zmichaels
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
    
    public DriverManager() {
        this.driverLoader = ServiceLoader.load(DriverProvider.class);
    } 
    
    public DriverManager(final ClassLoader loader) {
        this.driverLoader = ServiceLoader.load(DriverProvider.class, loader);
    }
}
