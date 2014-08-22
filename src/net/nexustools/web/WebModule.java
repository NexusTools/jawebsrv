/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web;

/**
 *
 * @author kate
 */
public interface WebModule {
    
    public void handle(WebClient client, WebRequest request);
    
}
