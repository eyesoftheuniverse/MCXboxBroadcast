package com.rtm516.mcxboxbroadcast.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.util.Map;

/**
 * Handle the connection and authentication with the RTA websocket
 */
public class RtaWebsocketClient extends WebSocketClient {
    private String connectionId;
    private ExpandedSessionInfo sessionInfo;
    private String tokenHeader;
    private final Logger logger;
    private boolean firstConnectionId = true;

    /**
     * Create a new websocket and add the Authorization header
     *
     * @param authenticationToken The token to use for authentication
     */
    public RtaWebsocketClient(String authenticationToken, ExpandedSessionInfo sessionInfo, String tokenHeader, Logger logger) {
        super(Constants.RTA_WEBSOCKET);
        addHeader("Authorization", authenticationToken);
        this.sessionInfo = sessionInfo;
        this.tokenHeader = tokenHeader;
        this.logger = logger;
    }

    /**
     * A helper method to get the stored connection ID
     * 
     * @return The stored connection ID
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * When the web socket connects send the request for the connection ID
     * 
     * @see WebSocketClient#onOpen(ServerHandshake)
     * 
     * @param serverHandshake The handshake of the websocket instance
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send("[1,1,\"https://sessiondirectory.xboxlive.com/connections/\"]");
    }

    /**
     * When we get a message check if it's a connection ID message
     * and handle otherwise ignore it
     * 
     * @see WebSocketClient#onMessage(String) 
     * 
     * @param message The UTF-8 decoded message that was received.
     */
    @Override
    public void onMessage(String message) {
        if (message.contains("ConnectionId") && firstConnectionId) {
            Object[] parts = Constants.GSON.fromJson(message, Object[].class);
            connectionId = ((Map<String, String>) parts[4]).get("ConnectionId");
            firstConnectionId = false;
        } else {
            logger.debug("Websocket message: " + message);
        }
    }

    /**
     * @see WebSocketClient#onClose(int, String, boolean) 
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.debug("RTAWebsocket disconnected: " + reason + " (" + code + ")");
    }

    /**
     * @see WebSocketClient#onError(Exception)
     **/
    @Override
    public void onError(Exception ex) {
        logger.debug("RTAWebsocket error: " + ex.getMessage());
    }
}
