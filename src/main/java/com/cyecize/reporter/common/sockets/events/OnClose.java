package com.cyecize.reporter.common.sockets.events;

public interface OnClose {
    void onClose(int code, String reason, boolean remote);
}
