/**
 * -----------------------------------------------------------------------------
 * File=Func0.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Function that returns a result and takes 0 parameters.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions.functional;

/**
 * Function that returns a result and takes 0 parameters.
 *
 * @param <TResult> The type of the result.
 */
@FunctionalInterface
public interface Func0<TResult> {

    /**
     * Invokes the function.
     *
     * @return The result of the function.
     */
    TResult invoke();
}
