/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web;

import java.io.IOException;
import net.nexustools.DefaultAppDelegate;
import net.nexustools.io.net.Server.Protocol;
import net.nexustools.web.http.HTTPServer;
import net.nexustools.web.modules.StreamModule;

/**
 *
 * @author kate
 */
public class JaWebSrv extends DefaultAppDelegate {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new JaWebSrv(args, 8080, Protocol.TCP).mainLoop();
    }

    final WebServer webServer;
    public JaWebSrv(String[] args, int port, Protocol protocol) throws IOException {
        super(args, "JaWebSrv", "NexusTools");
        webServer = new HTTPServer(new StreamModule("home://"), port, protocol);
    }

    @Override
    protected void launch(String[] args) {}

    public boolean needsMainLoop() {
        return false;
    }

    public void mainLoop() {
        try {
            webServer.join();
        } catch (InterruptedException ex) {}
    }
    
}
