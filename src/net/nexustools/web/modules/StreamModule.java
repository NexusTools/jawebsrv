/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.modules;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import net.nexustools.io.Stream;
import net.nexustools.web.WebClient;
import net.nexustools.web.WebModule;
import net.nexustools.web.WebRequest;

/**
 *
 * @author kate
 */
public class StreamModule implements WebModule {
    
    private final String documentRoot;
    public StreamModule(String root) {
        documentRoot = root;
    }

    boolean loadedJava7APIs = false;
    Method probeContentType;
    Method pathForUri;
    @Override
    public void handle(WebClient client, WebRequest request) {
        Stream stream;
        try {
            stream = Stream.open(documentRoot + request.path());
            if(stream.exists())
                if(stream.hasChildren()) {
                    String path = request.path();
                    if(!path.endsWith("/"))
                        path += "/";
                    StringBuilder builder = new StringBuilder();
                    for(String child : stream.children()) {
                        builder.append("<a href='");
                        builder.append(path);
                        builder.append(child);
                        builder.append("'>");
                        builder.append(child);
                        builder.append("</a><br />");
                    }

                    client.sendSystemPage(200, "Okay", "Index Of: " + request.path(), builder.toString());
                } else {
                    HashMap<String, String> headers = new HashMap();
                    headers.put("Content-Type", stream.getMimeType());
                    headers.put("Content-Length", String.valueOf(stream.size()));

                    client.writePayload(200, "Okay", stream.createInputStream(), headers);
                }
            else
                client.sendStatus(404);
        } catch (IOException ex) {
            client.sendException(ex);
        } catch (URISyntaxException ex) {
            client.sendException(ex);
        }
    }
    
}
