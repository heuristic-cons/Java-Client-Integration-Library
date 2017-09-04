/**
 * -----------------------------------------------------------------------------
 * File=Func1.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Function that returns a result and takes 1 parameter.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions.functional;

/**
 * Function that returns a result and takes 1 parameter.
 *
 * @param <TResult> The type of the result.
 */
@FunctionalInterface
public interface Func1<T, TResult> {

    /**
     * Invokes the function.
     *
     * @param param The single parameter value.
     * @return The result of the function.
     */
    TResult invoke(T param);
}
