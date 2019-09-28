package com.cyecize.reporter.common.sockets;

import com.cyecize.reporter.common.sockets.events.OnClose;
import com.cyecize.reporter.common.sockets.events.OnError;
import com.cyecize.reporter.common.sockets.events.OnMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientSocket extends WebSocketClient {

    private final OnMessage onMessage;

    private final OnClose onClose;

    private final OnError onError;

    public ClientSocket(int port) throws URISyntaxException {
        this(port, null, null, null);
    }

    public ClientSocket(URI serverUri) {
        this(serverUri, null, null, null);
        super.connect();
    }

    public ClientSocket(int port, OnMessage onMessage, OnClose onClose, OnError onError) throws URISyntaxException {
        this(new URI(String.format("127.0.0.1:%d", port)), onMessage, onClose, onError);
    }

    public ClientSocket(URI serverUri, OnMessage onMessage, OnClose onClose, OnError onError) {
        super(serverUri);
        this.onMessage = onMessage;
        this.onClose = onClose;
        this.onError = onError;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        if (this.onMessage != null) {
            this.onMessage.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (this.onClose != null) {
            this.onClose.onClose(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception ex) {
        if (this.onError != null) {
            this.onError.onError(ex);
        }
    }
}
