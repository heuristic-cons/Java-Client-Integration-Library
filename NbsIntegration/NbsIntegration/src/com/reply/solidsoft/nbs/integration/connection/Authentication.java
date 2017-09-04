/**
 * -----------------------------------------------------------------------------
 * File=Authentication.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The authentication class.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentials;
import com.reply.solidsoft.nbs.integration.clientcredentials.ClientCredentialsService;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;

/**
 * The authentication class.
 */
public final class Authentication {

    /**
     * Obtains a token client.
     *
     * @param identityServerUrl The identity server address.
     * @param clientCredentialsService The client credentials service.
     * @param connectionIdentifier The connection identifier.
     * @return The authenticated HTTP client with a bearer token.
     */
    public static TokenClient getTokenClient(String identityServerUrl, ClientCredentialsService clientCredentialsService, ConnectionIdentifier connectionIdentifier) {
        // The code does not assume support for HTTPS.  NB.  HTTPS will be enforced
        // in validated systems.    
        ClientCredentials clientCredentials = clientCredentialsService.getClientCredentials(connectionIdentifier);

        return new TokenClient(identityServerUrl, clientCredentials, null, null);
    }
}
