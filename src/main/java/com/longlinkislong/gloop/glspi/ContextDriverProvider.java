/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.glspi;

/**
 *
 * @author zmichaels
 */
public interface ContextDriverProvider {

    ContextDriver getContextDriver();

    String getContextName();
}
