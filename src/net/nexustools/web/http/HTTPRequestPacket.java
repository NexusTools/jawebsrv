/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.http;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.nexustools.utils.log.Logger;
import net.nexustools.web.WebRequest;

/**
 *
 * @author kate
 */
public class HTTPRequestPacket<T, C extends HTTPClient, S extends HTTPServer> extends HTTPPacket<T, C, S> {

    private static final Pattern statusLinePattern = Pattern.compile("^([A-Z]{3,10}) (/.*?) (HTTP/\\d\\.\\d)$");
    
    String path;
    String method;

    @Override
    protected void validateHeader(String statusLine, C client) throws UnsupportedOperationException, IOException {
        Matcher matcher = statusLinePattern.matcher(statusLine);
        if(!matcher.matches())
            throw new IOException("Invalid request status line.");
        else {
            method = matcher.group(1);
            path = matcher.group(2);
            Logger.debug(method, path);
        }
    }

    @Override
    protected void recvFromClient(C client, S server) {
        try {
            server.module().handle(client, new WebRequest() {
                @Override
                public String method() {
                    return method;
                }
                @Override
                public String path() {
                    return path;
                }
                @Override
                public Map<String, String> get() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                @Override
                public Map<String, String> post() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                @Override
                public String header(String key) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                @Override
                public Iterable<String> headers(String key) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                @Override
                public boolean hasHeader(String key) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                @Override
                public Iterable<String> headerKeys() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        } catch(Throwable t) {
            client.sendException(t);
        }
    }
    
    
    
}
