package com.kingh.netty.server.handler;


import com.kingh.netty.server.netty.HttpRequest;

public interface Handler {
    void handle(HttpRequest request);
}
