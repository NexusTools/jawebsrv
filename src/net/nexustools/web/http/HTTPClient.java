/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import net.nexustools.io.net.Server;
import net.nexustools.web.WebClient;

/**
 *
 * @author kate
 */
public class HTTPClient<P extends HTTPPacket, S extends HTTPServer<P, ?>> extends WebClient<P, S> {

    public HTTPClient(Socket socket, Server server) throws IOException {
        super(socket, server);
    }

    @Override
    public P nextPacket() throws IOException {
        HTTPPacket httpPacket = new HTTPRequestPacket();
        httpPacket.read(socket.i, this);
        return (P) httpPacket;
    }

    @Override
    protected void writePacket(P packet) throws IOException, NoSuchMethodException {
        packet.write(socket.v, this);
    }

    @Override
    public void writePayloadImpl(int status, String statusString, InputStream payload, Map<String, String> headers) {
        send((P)new HTTPResponsePacket(status, statusString, payload, headers));
    }
    
}
