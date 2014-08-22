/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;
import net.nexustools.io.DataOutputStream;
import net.nexustools.utils.StringUtils;
import net.nexustools.utils.log.Logger;

/**
 *
 * @author kate
 */
public class HTTPResponsePacket<T, C extends HTTPClient, S extends HTTPServer> extends HTTPPacket<T, C, S> {
    
    final int status;
    final String statusString;
    final InputStream payload;
    final Map<String, String> headers;
    
    public HTTPResponsePacket(int status, String statusString, InputStream payload, Map<String, String> headers) {
        this.status = status;
        this.statusString = statusString;
        this.payload = payload;
        this.headers = headers;
    }

    @Override
    public void write(DataOutputStream dataOutput, C client) throws UnsupportedOperationException, IOException {
        {
            StringBuilder headerBuilder = new StringBuilder();
            headerBuilder.append("HTTP/1.1 ");
            headerBuilder.append(status);
            headerBuilder.append(' ');
            headerBuilder.append(statusString);
            headerBuilder.append("\r\n");
            Logger.debug(headers);
            for(Map.Entry<String,String> header : headers.entrySet()) {
                headerBuilder.append(header.getKey());
                headerBuilder.append(": ");
                headerBuilder.append(header.getValue());
                headerBuilder.append("\r\n");
            }

            headerBuilder.append("\r\n");
            dataOutput.write(headerBuilder.toString().getBytes(StringUtils.UTF8));
        }
        
        int read;
        byte[] buffer = new byte[8096];
        while((read = payload.read(buffer)) > 0) {
            dataOutput.write(buffer, 0, read);
        }
    }

    @Override
    protected void validateHeader(String statusLine, C client) throws UnsupportedOperationException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
