/**
 * -----------------------------------------------------------------------------
 * File=NbsHttpClient.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Provides a base class for sending HTTP requests and receiving HTTP responses
 * from the National Blueprint System.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryType;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import com.reply.solidsoft.nbs.integration.clientcredentials.ClientCredentialsService;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;
import com.reply.solidsoft.nbs.integration.logging.LoggingService;

/**
 * Provides a base class for sending HTTP requests and receiving HTTP responses
 * from the National Blueprint System.
 */
public class NbsHttpClient implements HttpClient, Closeable {

    /**
     * An event handler that must be fired on token expiry
     */
    private com.reply.solidsoft.nbs.integration.extensions.functional.Action2<Object, TokenExpiredEventArgs> tokenExpired;

    /**
     * The logging service.
     */
    private LoggingService loggingService;

    /**
     * The timer that raises the Token Expired event.
     */
    private Timer tokenExpiryTimer;

    /**
     * The base address for URIs.
     */
    private URI baseAddress;

    /**
     * The default HTTP client.
     */
    private HttpClient currentClient;

    /**
     * Initializes a new instance of the NbsHttpClient class.
     *
     * @param apiConnectionData The connection data for connecting to the
     * National System.
     * @param loggingService the logging service.
     * @param clientCredentialsService The client credentials used by the API
     * client to connect to the NBS.
     * @param connectionIdentifier A connection identifier.
     * @param tokenExpired The token expiry event handler.
     * @param isLogging Indicates whether the library is providing data to a
     * logging service.
     */
    public NbsHttpClient(
            ApiConnection apiConnectionData,
            LoggingService loggingService,
            ClientCredentialsService clientCredentialsService,
            ConnectionIdentifier connectionIdentifier,
            com.reply.solidsoft.nbs.integration.extensions.functional.Action2<Object, TokenExpiredEventArgs> tokenExpired,
            boolean isLogging) {
        this(
                apiConnectionData,
                loggingService,
                clientCredentialsService,
                connectionIdentifier,
                tokenExpired,
                isLogging,
                null);
    }

    /**
     * Initializes a new instance of the NbsHttpClient class.
     *
     * @param apiConnectionData The connection data for connecting to the
     * National System.
     * @param loggingService the logging service.
     * @param clientCredentialsService The client credentials used by the API
     * client to connect to the NBS.
     * @param connectionIdentifier A connection identifier.
     * @param tokenExpired The token expiry event handler.
     * @param isLogging Indicates whether the library is providing data to a
     * logging service.
     * @param refreshToken The refresh token, if it exists.
     */
    public NbsHttpClient(
            ApiConnection apiConnectionData,
            LoggingService loggingService,
            ClientCredentialsService clientCredentialsService,
            ConnectionIdentifier connectionIdentifier,
            com.reply.solidsoft.nbs.integration.extensions.functional.Action2<Object, TokenExpiredEventArgs> tokenExpired,
            boolean isLogging,
            String refreshToken) {
        this.defaultRequestHeaders = new ArrayList<>();
        this.tokenExpired = (Object arg1, TokenExpiredEventArgs arg2) -> tokenExpired.invoke(arg1, arg2);
        this.loggingService = loggingService;
        this.initialize(
                apiConnectionData,
                clientCredentialsService,
                connectionIdentifier,
                refreshToken,
                isLogging);
    }

    /**
     * Gets the event arguments for the token expired event.
     */
    private TokenExpiredEventArgs tokenExpiredEventArgs;

    /**
     * Gets the event arguments for the token expired event.
     *
     * @return The event arguments for the token expired event.
     */
    public final TokenExpiredEventArgs getTokenExpiredEventArgs() {
        return tokenExpiredEventArgs;
    }

    /**
     * Sets the event arguments for the token expired event.
     *
     * @param value The event arguments for the token expired event.
     */
    private void setTokenExpiredEventArgs(TokenExpiredEventArgs value) {
        tokenExpiredEventArgs = value;
    }

    /**
     * Closes the client.
     */
    @Override
    public void close() {
        try {
            this.tokenExpiryTimer.cancel();
        } catch (NullPointerException nullEx) {
            String s = "Help!";
        }

        this.tokenExpiryTimer = null;
    }

    /**
     * Constructs the query parameter string for a request.
     *
     * @param uriBuilder The URI builder.
     * @param batch The batch number.
     * @param expiry The expiry date.
     */
    public static void buildOptionalParametersQueryString(URIBuilder uriBuilder, String batch, String expiry) {

        if (batch == null && expiry == null) {
            return;
        }

        if (batch != null) {
            uriBuilder.setParameter("batch", batch);
        }

        if (expiry != null) {
            uriBuilder.setParameter("expiry", expiry);
        }
    }

    /**
     * Performs an HTTP get request.
     *
     * @param requestUri The request URI.
     * @param headers The request headers.
     * @return An HTTP response.
     * @throws IOException The request failed.
     */
    public final HttpResponse get(String requestUri, List<Header> headers) throws IOException {
        return this.get(createUri(requestUri), headers);
    }

    /**
     * Performs an HTTP get request.
     *
     * @param requestUri The request URI.
     * @param headers The request headers.
     * @return An HTTP response.
     * @throws IOException The request failed.
     */
    public final HttpResponse get(URI requestUri, List<Header> headers) throws IOException {
        HttpGet httpGet = new HttpGet(requestUri);

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            headers.forEach((header) -> {
                httpGet.setHeader(header);
            });

            this.setDefaultHeaders(httpGet);

            return client.execute(httpGet);
        } catch (UnsupportedEncodingException codingEx) {
            throw new AssertionError("UTF-8 not supported");
        } catch (IOException ioEx) {
            throw ioEx;
        }
    }

    /**
     * Perform a PATCH HTTP request.
     *
     * @param requestUri The request URI.
     * @param content The content of the request.
     * @param headers A list of HTTP headers.
     * @return An HTTP response from the national system.
     * @throws java.io.IOException Signals that an I/O exception of some sort
     * has occurred. This class is the general class of exceptions produced by
     * failed or interrupted I/O operations.
     */
    public final HttpResponse patch(String requestUri, StringEntity content, List<Header> headers) throws IOException {
        return this.patch(createUri(requestUri), content, headers);
    }

    /**
     * Perform a PATCH HTTP request.
     *
     * @param requestUri The request URI.
     * @param content The content of the request.
     * @param headers A list of HTTP headers.
     * @return An HTTP response from the national system.
     * @throws java.io.IOException Signals that an I/O exception of some sort
     * has occurred. This class is the general class of exceptions produced by
     * failed or interrupted I/O operations.
     */
    public final HttpResponse patch(URI requestUri, StringEntity content, List<Header> headers) throws IOException {
        HttpPatch httpPatch = new HttpPatch(requestUri);

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            httpPatch.setEntity(content);

            headers.forEach((header) -> {
                httpPatch.setHeader(header);
            });

            this.setDefaultHeaders(httpPatch);

            return client.execute(httpPatch);
        } catch (UnsupportedEncodingException codingEx) {
            throw new AssertionError("UTF-8 not supported");
        } catch (IOException ioEx) {
            throw ioEx;
        }
    }

    /**
     * Perform a POST HTTP request.
     *
     * @param requestUri The request URI.
     * @param content The content of the request.
     * @param headers A list of HTTP headers.
     * @return An HTTP response from the national system.
     * @throws java.io.IOException Signals that an I/O exception of some sort
     * has occurred. This class is the general class of exceptions produced by
     * failed or interrupted I/O operations.
     */
    public final HttpResponse post(String requestUri, StringEntity content, List<Header> headers) throws IOException {
        return this.post(createUri(requestUri), content, headers);
    }

    /**
     * Perform a POST HTTP request.
     *
     * @param requestUri The request URI.
     * @param content The content of the request.
     * @param headers A list of HTTP headers.
     * @return An HTTP response from the national system.
     * @throws java.io.IOException Signals that an I/O exception of some sort
     * has occurred. This class is the general class of exceptions produced by
     * failed or interrupted I/O operations.
     */
    public final HttpResponse post(URI requestUri, StringEntity content, List<Header> headers) throws IOException {
        HttpPost httpPost = new HttpPost(requestUri);

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            httpPost.setEntity(content);

            headers.forEach((header) -> {
                httpPost.setHeader(header);
            });

            this.setDefaultHeaders(httpPost);

            return client.execute(httpPost);
        } catch (UnsupportedEncodingException codingEx) {
            throw new AssertionError("UTF-8 not supported");
        } catch (IOException ioEx) {
            throw ioEx;
        }
    }

    /**
     * The bearer token currently used by the client.
     */
    private String bearerToken;

    /**
     * Gets the bearer token currently used by the client.
     *
     * @return The bearer token currently used by the client.
     */
    public String getBearerToken() {
        return this.bearerToken;
    }

    /**
     * Sets the bearer token currently used by the client.
     *
     * @param value The bearer token currently used by the client.
     */
    public void setBearerToken(String value) {
        this.bearerToken = value;
    }
    /**
     * A list of default headers.
     */
    private final List<Header> defaultRequestHeaders;

    /**
     * Gets the default request headers.
     *
     * @return The default request headers.
     */
    public List<Header> getDefaultRequestHeaders() {
        return this.defaultRequestHeaders;
    }

    /**
     * Merge the default headers into the headers list.
     *
     * @param headers The headers into which the default headers will be merged.
     * @return A merged list of headers.
     */
    private List<Header> mergeHeadersWithDefaults(List<Header> headers) {
        List<Header> mergedHeaders = new ArrayList(headers);
        mergedHeaders.addAll(this.getDefaultRequestHeaders());
        return mergedHeaders;
    }

    /**
     * Sets headers to their default values, if not already provided.
     *
     * @param httpAction The HTTP request action.
     */
    private void setDefaultHeaders(HttpRequestBase httpAction) {
        if (!httpAction.containsHeader("Authorization") && !com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(bearerToken)) {
            httpAction.setHeader("Authorization", String.format("%s %s", "Bearer", this.bearerToken));
        }

        if (!httpAction.containsHeader("emvs-data-entry-mode")) {
            httpAction.setHeader("emvs-data-entry-mode", "non-manual");
        }

        if (!httpAction.containsHeader("Accept")) {
            httpAction.setHeader("Accept", "application/json");
        }

        if (!(httpAction instanceof HttpGet)) {
            if (!httpAction.containsHeader("Content-type")) {
                httpAction.setHeader("Content-type", "application/json");
            }
        }
    }

    /**
     * Create a URI from a string representation.
     *
     * @param uri The string representation of the URI.
     * @return The constructed URI.
     */
    private URI createUri(String uri) {
        try {
            if (this.baseAddress == null) {
                return com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrEmpty(uri) ? null : new URI(uri);
            } else {
                URI providedUri = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrEmpty(uri) ? null : new URI(uri);

                if (providedUri == null) {
                    return this.baseAddress.normalize();
                }

                if (!providedUri.isAbsolute()) {
                    String baseAddressString = this.baseAddress.normalize().toASCIIString();
                    if (!baseAddressString.endsWith("/")) {
                        if (uri.startsWith("/")) {
                            return new URI(baseAddressString + uri);
                        }

                        return new URI(baseAddressString + "/" + uri);
                    }

                    if (uri.startsWith("/")) {
                        baseAddressString = baseAddressString.substring(0, baseAddressString.length() - 2);
                    }

                    return new URI(baseAddressString + uri);
                } else {
                    return providedUri;
                }
            }
        } catch (URISyntaxException uriSyntaxEx) {
            return null;
        }
    }

    /**
     * Initializes the NbsHttpClient class.
     *
     * @param apiConnectionData The API connection Data.
     * @param clientCredentialsService The client credentials used by the API
     * client to connect to the NBS.
     * @param connectionIdentifier A connection identifier.
     * @param refreshToken The refresh token, if it exists.
     * @param isLogging Indicates whether the library is providing data to a
     * logging service.
     *
     */
    private void initialize(ApiConnection apiConnectionData, ClientCredentialsService clientCredentialsService, ConnectionIdentifier connectionIdentifier, String refreshToken, boolean isLogging) {
        try {
            this.baseAddress = new URI(apiConnectionData.getBaseUrl());
        } catch (URISyntaxException uriSytaxEx) {
            this.baseAddress = null;
        }

        TokenClient tokenClient = Authentication.getTokenClient(apiConnectionData.getIdentityServerUrl(), clientCredentialsService, connectionIdentifier);
        TokenResponse result = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(refreshToken) ? tokenClient == null ? null : tokenClient.RequestClientCredentials() : tokenClient == null ? null : tokenClient.RequestRefreshToken(refreshToken);

        String message = Resources.getLogging_RequestBearerTokenSucceeded();

        if (result == null ? true : result.getIsHttpError()) {
            message = Resources.getLogging_RequestBearerTokenFailed();
        } else {
            this.startTokenExpiryMonitor(result.getExpiresIn(), result.getRefreshToken());
            this.setBearerToken(result.getAccessToken());
        }

        try {

            long timestamp = 0;

            if (null != this.loggingService) {
                DataTable dataTable = this.loggingService.getTables().get("log");

                if (null != dataTable) {
                    timestamp = dataTable.getCurrentTimestamp();
                }
            }

            LogEntry logEntry = new LogEntry(timestamp);
            logEntry.setUser(this.loggingService == null ? null : this.loggingService.getCurrentUser());
            logEntry.setId(UUID.randomUUID().toString());
            logEntry.setTime(Instant.now());
            logEntry.setEntryType(LogEntryType.ERROR);
            logEntry.setSeverity(1);
            logEntry.setMessage(message);

            if (isLogging && null != this.loggingService) {
                this.loggingService.getLog().invoke(this, logEntry);
            }
        } catch (java.lang.Exception e) {
            // ignored
        }
    }

    /**
     * Starts the token expiry monitor thread.
     *
     * @param expiresInSecs The number of seconds after which the token expired.
     * @param refreshToken The refresh token, if it exists.
     */
    private void startTokenExpiryMonitor(Long expiresInSecs, String refreshToken) {
        if (null == expiresInSecs || 0 == expiresInSecs) {
            return;
        }

        this.setTokenExpiredEventArgs(new TokenExpiredEventArgs(expiresInSecs, refreshToken));
        long requestedDelayInMs = this.getTokenExpiredEventArgs().getExpiresIn() * 1000;
        int actualDelay = requestedDelayInMs > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) requestedDelayInMs;
        this.tokenExpiryTimer = new Timer();

        NbsHttpClient thisClient = this;
        this.tokenExpiryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                thisClient.tokenExpiryMonitor();
            }
        }, actualDelay, actualDelay);
    }

    /**
     * Raises the Token Expired event and kills the timer.
     */
    private void tokenExpiryMonitor() {
        // Raise the TTokenExpiryMonitorokenExpired event.
        if (null != this.tokenExpired) {
            this.tokenExpired.invoke(this, this.getTokenExpiredEventArgs());
        }

        this.tokenExpiryTimer.cancel();
        this.tokenExpiryTimer = null;
    }

    /**
     * Obtains the parameters for this client. These parameters will become
     * defaults for all requests being executed with this client, and for the
     * parameters of dependent objects in this client.
     *
     * @return The parameters for this client.
     */
    @Override
    public HttpParams getParams() {
        return currentClient.getParams();
    }

    /**
     * Obtains the connection manager used by this client.
     *
     * @return The connection manager used by this client.
     */
    @Override
    public ClientConnectionManager getConnectionManager() {
        return currentClient.getConnectionManager();
    }

    /**
     * Executes HTTP request using the default context.
     *
     * @param request The request to execute
     * @return The response to the request. This is always a final response,
     * never an intermediate response with an 1xx status code. Whether redirects
     * or authentication challenges will be returned or handled automatically
     * depends on the implementation and configuration of this client.
     * @throws IOException In case of a problem or the connection was aborted.
     * @throws ClientProtocolException In case of an http protocol error.
     */
    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        return currentClient.execute(request);
    }

    /**
     * Executes HTTP request using the given context.
     *
     * @param request The request to execute.
     * @param context The context to use for the execution, or null to use the
     * default context
     * @return The response to the request. This is always a final response,
     * never an intermediate response with an 1xx status code. Whether redirects
     * or authentication challenges will be returned or handled automatically
     * depends on the implementation and configuration of this client.
     * @throws IOException In case of a problem or the connection was aborted.
     * @throws ClientProtocolException In case of an http protocol error.
     */
    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return currentClient.execute(request, context);
    }

    /**
     * Executes HTTP request using the default context.
     *
     * @param target The target host for the request. Implementations may accept
     * null if they can still determine a route, for example to a default target
     * or by inspecting the request.
     * @param request The request to execute
     * @return The response to the request. This is always a final response,
     * never an intermediate response with an 1xx status code. Whether redirects
     * or authentication challenges will be returned or handled automatically
     * depends on the implementation and configuration of this client.
     * @throws IOException In case of a problem or the connection was aborted.
     * @throws ClientProtocolException In case of an http protocol error.
     */
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        return currentClient.execute(target, request);
    }

    /**
     * Executes HTTP request using the given context.
     *
     * @param target The target host for the request. Implementations may accept
     * null if they can still determine a route, for example to a default target
     * or by inspecting the request.
     * @param request The request to execute.
     * @param context The context to use for the execution, or null to use the
     * default context.
     * @return The response to the request. This is always a final response,
     * never an intermediate response with an 1xx status code. Whether redirects
     * or authentication challenges will be returned or handled automatically
     * depends on the implementation and configuration of this client.
     * @throws IOException In case of a problem or the connection was aborted.
     * @throws ClientProtocolException In case of an http protocol error.
     */
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return currentClient.execute(target, request, context);
    }

    /**
     * Executes HTTP request using the default context and processes the
     * response using the given response handler. Implementing classes are
     * required to ensure that the content entity associated with the response
     * is fully consumed and the underlying connection is released back to the
     * connection manager automatically in all cases relieving individual
     * ResponseHandlers from having to manage resource deallocation internally.
     *
     * @param <T> The base response type.
     * @param request The request to execute.
     * @param responseHandler The response handler.
     * @return The response object as generated by the response handler.
     * @throws IOException In case of a problem or the connection was aborted.
     * @throws ClientProtocolException In case of an http protocol error.
     */
    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return currentClient.execute(request, responseHandler);
    }

    /**
     * Executes HTTP request using the given context and processes the response
     * using the given response handler. Implementing classes are required to
     * ensure that the content entity associated with the response is fully
     * consumed and the underlying connection is released back to the connection
     * manager automatically in all cases relieving individual ResponseHandlers
     * from having to manage resource deallocation internally.
     *
     * @param <T> The base response type/
     * @param request The request to execute.
     * @param responseHandler The response handler.
     * @param context The context to use for the execution, or null to use the
     * default context.
     * @return The response object as generated by the response handler.
     * @throws IOException In case of a problem or the connection was aborted
     * @throws ClientProtocolException In case of an http protocol error.
     */
    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return currentClient.execute(request, responseHandler, context);
    }

    /**
     * Executes HTTP request to the target using the default context and
     * processes the response using the given response handler. Implementing
     * classes are required to ensure that the content entity associated with
     * the response is fully consumed and the underlying connection is released
     * back to the connection manager automatically in all cases relieving
     * individual ResponseHandlers from having to manage resource deallocation
     * internally.
     *
     * @param <T> the response handler type.
     * @param target The target host for the request. Implementations may accept
     * null if they can still determine a route, for example to a default target
     * or by inspecting the request.
     * @param request The request to execute.
     * @param responseHandler The response handler.
     * @return The response object as generated by the response handler.
     * @throws IOException In case of a problem or the connection was aborted
     * @throws ClientProtocolException In case of an http protocol error
     */
    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return currentClient.execute(target, request, responseHandler);
    }

    /**
     * Executes HTTP request to the target using the given context and processes
     * the response using the given response handler. Implementing classes are
     * required to ensure that the content entity associated with the response
     * is fully consumed and the underlying connection is released back to the
     * connection manager automatically in all cases relieving individual
     * ResponseHandlers from having to manage resource deallocation internally.
     *
     * @param <T> The type of the response handler.
     * @param target The target host for the request. Implementations may accept
     * null if they can still determine a route, for example to a default target
     * or by inspecting the request.
     * @param request The request to execute.
     * @param responseHandler The response handler.
     * @param context The context to use for the execution, or null to use the
     * default context.
     * @return The response object as generated by the response handler.
     * @throws IOException In case of a problem or the connection was aborted.
     * @throws org.apache.http.client.ClientProtocolException Signals an error
     * in the HTTP protocol.
     */
    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return currentClient.execute(target, request, responseHandler, context);
    }
}
