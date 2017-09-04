/**
 * -----------------------------------------------------------------------------
 * File=TokenResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 *  Models a response from an OpenID Connect/OAuth 2 token endpoint
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;

/**
 * Models a response from an OpenID Connect/OAuth 2 token endpoint
 */
public class TokenResponse {

    /**
     * Initializes a new instance of the IdentityModel.Client.TokenResponse
     * class.
     *
     * @param response The OAuth access token response.
     */
    public TokenResponse(OAuthAccessTokenResponse response) {
        this.accessToken = response.getAccessToken();
        this.expiresIn = response.getExpiresIn();
        this.refreshToken = response.getRefreshToken();
        this.tokenType = response.getTokenType();
    }

    /**
     * Initializes a new instance of the IdentityModel.Client.TokenResponse
     * class.
     *
     * @param exception The exception.
     */
    public TokenResponse(Throwable exception) {
        this.errorDescription = exception.getMessage();
    }

    /**
     * Initializes a new instance of the IdentityModel.Client.TokenResponse
     * class.
     *
     * @param statusCode The status code.
     * @param reason The reason.
     */
    public TokenResponse(int statusCode, String reason) {

        this.isHttpError = true;
        this.httpErrorstatusCode = statusCode;
        this.httpErrorReason = reason;
    }

    /**
     * The access token.
     */
    private String accessToken;

    /**
     * Gets the access token.
     *
     * @return The access token.
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * The identity token.
     */
    private String identityToken;

    /**
     * Gets the identity token.
     *
     * @return The identity token.
     */
    public String getIdentityToken() {
        return this.identityToken;
    }

    /**
     * The type of the token.
     */
    private String tokenType;

    /**
     * Gets the type of the token.
     *
     * @return The type of the token.
     */
    public String getTokenType() {
        return this.tokenType;
    }

    /**
     * The refresh token.
     */
    private String refreshToken;

    /**
     * Gets the refresh token.
     *
     * @return The refresh token.
     */
    public String getRefreshToken() {
        return this.refreshToken;
    }

    /**
     * The error description.
     */
    private String errorDescription;

    /**
     * Gets the error description.
     *
     * @return The error description.
     */
    public String getErrorDescription() {
        return this.errorDescription;
    }

    /**
     * The number of seconds the token expires in.
     */
    private Long expiresIn;

    /**
     * Gets the number of seconds the token expires in.
     *
     * @return The number of seconds the token expires in.
     */
    public Long getExpiresIn() {
        return this.expiresIn;
    }

    /**
     * A value indicating whether an HTTP error occurred.
     */
    private boolean isHttpError;

    /**
     * Gets a value indicating whether an HTTP error occurred.
     *
     * @return A value indicating whether an HTTP error occurred.
     */
    public boolean getIsHttpError() {
        return this.isHttpError;
    }

    /**
     * The value of the HTTP status code for an error.
     */
    private int httpErrorstatusCode;

    /**
     * Gets the value of the HTTP status code for an error.
     *
     * @return The value of the HTTP status code for an error.
     */
    public int getHttpErrorStatusCode() {
        return this.httpErrorstatusCode;
    }

    /**
     * The HTTP response string for an error.
     */
    private String httpErrorReason;

    /**
     * Gets the HTTP response string for an error.
     *
     * @return The HTTP response string for an error.
     */
    public String getHttpErrorReason() {
        return this.httpErrorReason;
    }
}
