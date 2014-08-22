/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web;

import java.util.Map;

/**
 *
 * @author kate
 */
public interface WebRequest {
    
    public String method();
    public String path();
    
    public Map<String,String> get();
    public Map<String,String> post();
    
    public String header(String key);
    public Iterable<String> headers(String key);
    public Iterable<String> headerKeys();
    public boolean hasHeader(String key);
    
}
