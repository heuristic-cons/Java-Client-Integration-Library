/**
 * -----------------------------------------------------------------------------
 * File=Action2.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Function that returns a void and takes 2 parameters.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions.functional;

/**
 * Function that returns a void and takes 2 parameters.
 *
 * @param <T1> The type of the first parameter.
 * @param <T2> The type of the second parameter.
 */
@FunctionalInterface
public interface Action2<T1, T2> {

    /**
     * Invoke the function.
     *
     * @param param1 The first parameter value.
     * @param param2 The second parameter value.
     */
    void invoke(T1 param1, T2 param2);
}
