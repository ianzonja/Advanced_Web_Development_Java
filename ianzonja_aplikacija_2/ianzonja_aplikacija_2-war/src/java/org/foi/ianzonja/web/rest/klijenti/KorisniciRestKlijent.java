/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.ianzonja.web.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:KorisniciREST [/]<br>
 * USAGE:
 * <pre>
 *        KorisniciRestKlijent client = new KorisniciRestKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Ian
 */
public class KorisniciRestKlijent {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ianzonja_aplikacija_3-war/webresources";

    public KorisniciRestKlijent() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
    }

    public String putJson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String getJson(String password, String username) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (password != null) {
            resource = resource.queryParam("password", password);
        }
        if (username != null) {
            resource = resource.queryParam("username", username);
        }
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }
    
}
