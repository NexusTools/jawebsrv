/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import net.nexustools.io.DataOutputStream;
import net.nexustools.io.EfficientOutputStream;
import net.nexustools.io.net.Client;
import net.nexustools.io.net.PacketRegistry;
import net.nexustools.io.net.Server;
import net.nexustools.runtime.RunQueue;
import net.nexustools.runtime.ThreadedRunQueue;
import net.nexustools.utils.StringUtils;

/**
 *
 * @author kate
 */
public abstract class WebClient<P extends WebPacket, S extends WebServer<P, ? extends WebClient>> extends Client<P, S> {
    
    private static final HashMap<Integer, String> statusMessages = new HashMap() {
        {
            put(200, "Okay");
            put(403, "Insufficient Permissions");
            put(404, "No Handler for Path");
            put(500, "Unhandled Exception Thrown");
            put(501, "Not Supported Yet");
        }
    };
    
    public static interface CacheSync {
        public long version();
        public int lifetime();
        public void write(DataOutputStream outputStream);
    }
    
    protected static final ThreadedRunQueue proxyRunQueue = new ThreadedRunQueue("WebProxy");

    public WebClient(Socket socket, Server server) throws IOException {
        super(socket, server);
    }
    public WebClient(String name, final Socket socket, RunQueue runQueue, PacketRegistry packetRegistry) throws IOException {
        super(socket, runQueue, packetRegistry);
    }
    
    public final void sendStatus(int status, String statusMessage) {
        String message;
        switch(status) {
            case 404:
                message = "The path you entered could not be resolved to a usable response.";
                break;
                
            case 403:
                message = "The path you entered requires authorization.";
                break;
                
            default:
                message = "Unknown status";
        }
        sendSystemPage(status, statusMessage, message);
    }
    
    public final void sendException(Throwable t) {
        String title = t.getMessage();
        if(title == null)
            title = "Unhandled " + t.getClass().getSimpleName();
        final StringBuilder exceptionBody = new StringBuilder();
        t.printStackTrace(new PrintStream(new EfficientOutputStream() {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                exceptionBody.append(new String(b, off, len));
            }
        }));
        sendSystemPage(500, title, exceptionBody.toString());
    }
    
    public final void sendSystemPage(int status, String statusMessage, String body) {
        sendSystemPage(status, statusMessage, statusMessage, body);
    }
    public final void sendSystemPage(int status, String statusMessage, String title, String body) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>");
        html.append("</title></head><body><h1>");
        html.append(title);
        html.append("</h1><pre>");
        html.append(body);
        html.append("</pre></body></html>");
        writePayload(status, statusMessage, html.toString(), "text/html");
    }
    
    public final void sendStatus(int status) {
        sendStatus(status, statusMessages.get(status));
    }
    
    public final void writePayload(int status, String statusString, String payload, String contentType) {
        writePayload(status, statusString, payload, contentType, new HashMap());
    }
    
    public final void writePayload(int status, String statusString, String payload) {
        writePayload(status, statusString, payload, "text/plain", new HashMap());
    }
    
    public final void writePayload(int status, String statusString, String payload, String contentType, Map<String,String> headers) {
        headers.put("Content-Type", contentType+"; charset=UTF8");
        writePayload(status, statusString, payload.getBytes(StringUtils.UTF8), headers);
    }
    
    public final void writePayload(int status, String statusString, String payload, Map<String,String> headers) {
        writePayload(status, statusString, payload, "text-plain", headers);
    }
    
    public final void writePayload(int status, String statusString, byte[] payload, Map<String,String> headers) {
        headers.put("Content-Length", String.valueOf(payload.length));
        writePayload(status, statusString, new ByteArrayInputStream(payload), headers);
    }
    
    public final void writePayload(int status, String statusString, InputStream payload, Map<String,String> headers) {
        headers.put("Server", "JaWebSrv/0.1");
        headers.put("Date", "Tuna");
        writePayloadImpl(status, statusString, payload, headers);
    }
    
    protected abstract void writePayloadImpl(int status, String statusString, InputStream payload, Map<String,String> headers);
    
    public final void proxy(WebRequest request) {
        throw new UnsupportedOperationException("Not Supported Yet");
    }
    
    public final void cgiScript(String path) {
        throw new UnsupportedOperationException("Not Supported Yet");
    }
    
}
