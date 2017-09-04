/**
 * -----------------------------------------------------------------------------
 * File=Func2.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Function that returns a result and takes 2 parameters.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions.functional;

/**
 * Function that returns a result and takes 2 parameters.
 *
 * @param <TResult> The type of the result.
 */
@FunctionalInterface
public interface Func2<T1, T2, TResult> {

    /**
     * Invokes the function.
     *
     * @param param1 The first parameter value.
     * @param param2 The second parameter value.
     * @return The result of the function.
     */
    TResult invoke(T1 param1, T2 param2);
}
