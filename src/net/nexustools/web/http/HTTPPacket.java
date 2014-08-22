/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.http;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import net.nexustools.io.DataInputStream;
import net.nexustools.io.net.DisconnectedException;
import net.nexustools.web.WebPacket;

/**
 *
 * @author kate
 */
public abstract class HTTPPacket<T, C extends HTTPClient, S extends HTTPServer> extends WebPacket<T, C, S> {
    
    private static final Pattern headerPattern = Pattern.compile("^([a-z][a-z0-9_\\-]*): (.+)$", Pattern.CASE_INSENSITIVE);
    ArrayList<String> headers = new ArrayList();

    @Override
    public void read(DataInputStream dataInput, C client) throws UnsupportedOperationException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataInput));
        String line = reader.readLine();
        if(line == null)
            throw new DisconnectedException();
        if(line.trim().length() < 1)
            throw new RuntimeException("Missing HTTP Status Line");
        validateHeader(line, client);
        
        while(true) {
            line = reader.readLine();
            if(line == null)
                throw new DisconnectedException();
            if(line.trim().length() < 1)
                break;
            
            headers.add(line);
        }
    }
    
    protected abstract void validateHeader(String statusLine, C client) throws UnsupportedOperationException, IOException;
    
}
