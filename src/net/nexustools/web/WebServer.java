/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web;

import java.io.IOException;
import net.nexustools.concurrent.Prop;
import net.nexustools.io.net.Server;

/**
 *
 * @author kate
 */
public class WebServer<P extends WebPacket, C extends WebClient<P, ? extends WebServer>> extends Server<P, C> {

    private Prop<WebModule> module;
    public WebServer(WebModule module, int port, Protocol protocol) throws IOException {
        super(port, protocol);
        this.module = new Prop(module);
    }
    public WebServer(WebModule module, int port) throws IOException {
        this(module, port, Protocol.TCP);
    }
    public WebServer(WebModule module) throws IOException {
        this(module, 80);
    }
    
    public WebModule module() {
        return module.get();
    }
    
    public void setModule(WebModule module) {
        this.module.set(module);
    }
    
}
