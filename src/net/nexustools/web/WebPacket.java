/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web;

import java.util.ArrayList;
import java.util.HashMap;
import net.nexustools.io.net.DisconnectedException;
import net.nexustools.io.net.Packet;

/**
 *
 * @author kate
 */
public abstract class WebPacket<T, C extends WebClient, S extends WebServer> extends Packet<T, C, S> {

    @Override
    protected void recvFromServer(C client) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    protected void recvFromClient(C client, S server) {
    }

    @Override
    protected void failedToSend(C client, Throwable reason) {
        super.failedToSend(client, reason);
        if(!(reason instanceof DisconnectedException)) {
            // TODO: Send response packet containing error information
            client.sendException(reason);
        }
    }
    
}
