/**
 * -----------------------------------------------------------------------------
 * File=TokenClient.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 *
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentials;
import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import org.apache.http.client.ResponseHandler;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * Client for OAuth 2.0 token provider using Client Credentials grant.
 */
public class TokenClient {

    /**
     * The response handler.
     */
    private ResponseHandler responseHandler;

    /**
     * Initializes an instance of the TokenClient class.
     *
     * @param address The address of the token server.
     */
    public TokenClient(String address) {
        this.address = address;
    }

    /**
     * Initializes an instance of the TokenClient class.
     *
     * @param address The address of the token server.
     * @param responseHandler The response handler.
     */
    public TokenClient(
            String address,
            ResponseHandler responseHandler) {
        this.address = address;
        this.responseHandler = responseHandler;
    }

    /**
     * Initializes an instance of the TokenClient class.
     *
     * @param address The address of the token server.
     * @param clientId The client identifier.
     * @param authenticationStyle The style of authentication.
     */
    public TokenClient(
            String address,
            String clientId,
            AuthenticationStyle authenticationStyle) {
        this.address = address;
        this.clientId = clientId;
        this.authenticationStyle = authenticationStyle;
    }

    /**
     * Initializes an instance of the TokenClient class.
     *
     * @param address The address of the token server.
     * @param clientId The client identifier.
     * @param responseHandler The response handler.
     */
    public TokenClient(
            String address,
            String clientId,
            ResponseHandler responseHandler) {
        this.address = address;
        this.clientId = clientId;
        this.responseHandler = responseHandler;
    }

    /**
     * Initializes an instance of the TokenClient class.
     *
     * @param address The address of the token server.
     * @param clientId The client identifier.
     * @param clientSecret The client secret.
     * @param authenticationStyle The style of authentication.
     */
    public TokenClient(
            String address,
            String clientId,
            String clientSecret,
            AuthenticationStyle authenticationStyle) {
        this.address = address;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authenticationStyle = authenticationStyle;
    }

    /**
     * Initializes an instance of the TokenClient class.
     *
     * @param address The address of the token server.
     * @param clientCredentials A set of client credentials.
     * @param responseHandler The response handler.
     * @param authenticationStyle The style of authentication.
     */
    public TokenClient(
            String address,
            ClientCredentials clientCredentials,
            ResponseHandler responseHandler,
            AuthenticationStyle authenticationStyle) {
        this.address = address;
        this.clientId = clientCredentials.getClientId();
        this.clientSecret = clientCredentials.getClientSecret();
        this.responseHandler = responseHandler;
        this.authenticationStyle = authenticationStyle;
    }

    /**
     * The client identifier.
     */
    private String clientId;

    /**
     * Gets the client identifier.
     *
     * @return The client identifier.
     */
    public String getClientId() {
        return this.clientId;
    }

    /**
     * Sets the client identifier.
     *
     * @param value The client identifier.
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    /**
     * The client secret.
     */
    private String clientSecret;

    /**
     * Gets the client secret.
     *
     * @return The client secret.
     */
    public String getClientSecret() {
        return this.clientSecret;
    }

    /**
     * Sets the client secret.
     *
     * @param value The client secret.
     */
    public void setClientSecret(String value) {
        this.clientSecret = value;
    }

    /**
     * The address.
     */
    private String address;

    /**
     * Gets the address.
     *
     * @return The address.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Sets the address.
     *
     * @param value The address.
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * The authentication style.
     */
    private AuthenticationStyle authenticationStyle;

    /**
     * Gets the authentication style.
     *
     * @return The authentication style.
     */
    public AuthenticationStyle getAuthenticationStyle() {
        return this.authenticationStyle;
    }

    /**
     * Sets the authentication style.
     *
     * @param value The authentication style.
     */
    public void setAuthenticationStyle(AuthenticationStyle value) {
        this.authenticationStyle = value;
    }

    /**
     * Requests a token based on client credentials.
     *
     * @return A token based on client credentials.
     */
    public TokenResponse RequestClientCredentials() {
        return RequestClientCredentials(null, null);
    }

    /**
     * Requests a token based on client credentials.
     *
     * @param scope The scope.
     * @return A token based on client credentials.
     */
    public TokenResponse RequestClientCredentials(String scope) {
        return RequestClientCredentials(scope, null);
    }

    /**
     * Requests a token based on client credentials.
     *
     * @param scope The scope.
     * @param extra Extra parameters.
     * @return A token based on client credentials.
     */
    public TokenResponse RequestClientCredentials(String scope, Object extra) {
        OAuthClient client = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest request = this.getOauthClientRequest(scope);

        try {
            OAuthJSONAccessTokenResponse response = client.accessToken(request, OAuthJSONAccessTokenResponse.class);

            if (response.getResponseCode() == HttpStatusCode.SC_OK || response.getResponseCode() == HttpStatusCode.SC_BAD_REQUEST) {
                return new TokenResponse(response);
            } else {
                return new TokenResponse(response.getResponseCode(), "");
            }
        } catch (OAuthSystemException systemEx) {
            return new TokenResponse(systemEx);
        } catch (OAuthProblemException problemEx) {
            return new TokenResponse(problemEx);
        } catch (Throwable ex) {
            return new TokenResponse(ex);
        }
    }

    /**
     * Requests a token using a refresh token.
     *
     * @param refreshToken The refresh token.
     * @return a token using a refresh token.
     */
    public TokenResponse RequestRefreshToken(String refreshToken) {
        return RequestRefreshToken(refreshToken, null);
    }

    /**
     * Requests a token using a refresh token.
     *
     * @param refreshToken The refresh token.
     * @param extra Extra parameters.
     * @return A token using a refresh token.
     */
    public TokenResponse RequestRefreshToken(String refreshToken, Object extra) {
        OAuthClient client = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest bearerClientRequest = getOauthRefreshTokenRequest(refreshToken);

        try {
            OAuthAccessTokenResponse resourceResponse = client.accessToken(bearerClientRequest, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);
            return new TokenResponse(resourceResponse);
        } catch (OAuthSystemException systemEx) {
            return new TokenResponse(systemEx);
        } catch (OAuthProblemException problemEx) {
            return new TokenResponse(problemEx);
        } catch (Throwable ex) {
            return new TokenResponse(ex);
        }
    }

    /**
     * Gets an OAuth 2.0 request.
     *
     * @param scope The scope of the request.
     * @return An OAuth 2.0 request.
     */
    private OAuthClientRequest getOauthClientRequest(String scope) {
        try {
            String tokenLocation = "connect/token";
            if (!this.address.endsWith("/")) {
                tokenLocation = "/" + tokenLocation;
            }

            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(this.address + tokenLocation)
                    .setGrantType(GrantType.CLIENT_CREDENTIALS)
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .buildBodyMessage();
            request.addHeader("Accept", "application/json");
            return request;
        } catch (OAuthSystemException ex) {
            return null;
        }
    }

    /**
     * Gets an OAuth 2.0 request for a refresh token.
     *
     * @param refreshToken The refresh token.
     * @return An OAuth 2.0 request for a refresh token.
     */
    private OAuthClientRequest getOauthRefreshTokenRequest(String refreshToken) {
        try {
            return OAuthClientRequest.tokenLocation(this.address)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setClientId(this.clientId)
                    .setClientSecret(this.clientSecret)
                    .setRefreshToken(refreshToken)
                    .buildBodyMessage();

        } catch (OAuthSystemException systemEx) {
            return null;
        }
    }
}
