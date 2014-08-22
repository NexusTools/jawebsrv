/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.http;

import java.io.IOException;
import java.net.Socket;
import net.nexustools.web.WebModule;
import net.nexustools.web.WebServer;

/**
 *
 * @author kate
 */
public class HTTPServer<P extends HTTPPacket, C extends HTTPClient<P, ? extends HTTPServer>> extends WebServer<P, C> {

    public HTTPServer(WebModule module, int port, Protocol protocol) throws IOException {
        super(module, port, protocol);
    }

    @Override
    public C createClient(Socket socket) throws IOException {
        return (C) new HTTPClient(socket, this);
    }
    
}
