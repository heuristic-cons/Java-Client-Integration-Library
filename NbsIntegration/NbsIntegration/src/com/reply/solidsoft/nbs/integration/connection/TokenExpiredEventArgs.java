/**
 * -----------------------------------------------------------------------------
 * File=TokenExpiredEventArgs.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Event arguments for the Token Expired event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

/**
 * Event arguments for the Token Expired event.
 */
public class TokenExpiredEventArgs extends com.reply.solidsoft.nbs.integration.extensions.events.EventArgs {

    /**
     * Initializes a new instance of the TokenExpiredEventArgs class.
     *
     * @param expiresIn The number of seconds after which the token expired.
     * @param refreshToken The refresh token, if it exists.
     */
    public TokenExpiredEventArgs(Long expiresIn, String refreshToken) {
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    /**
     * Gets the number of seconds after which the token expired.
     */
    private final Long expiresIn;

    public final Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Gets the refresh token, if it exists.
     */
    private final String refreshToken;

    public final String getRefreshToken() {
        return refreshToken;
    }
}
