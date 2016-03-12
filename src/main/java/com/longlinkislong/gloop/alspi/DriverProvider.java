/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.alspi;

import java.util.Collection;

/**
 *
 * @author zmichaels
 */
public interface DriverProvider {

    public Collection<? extends String> getDriverDescription();

    public double getSupportRating();

    public boolean isSupported();

    Driver getDriverInstance();

    default String getDriverName() {
        return getDriverInstance().getClass().getSimpleName();
    }
}
