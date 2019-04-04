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
 * Jersey REST client generated for REST resource:KorisnikREST
 * [/{korisnickoIme}]<br>
 * USAGE:
 * <pre>
 *        KorisnikRestKlijent client = new KorisnikRestKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Ian
 */
public class KorisnikRestKlijent {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ianzonja_aplikacija_3-war/webresources";

    public KorisnikRestKlijent(String korisnickoIme) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("{0}", new Object[]{korisnickoIme});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String korisnickoIme) {
        String resourcePath = java.text.MessageFormat.format("{0}", new Object[]{korisnickoIme});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public String putJson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String postJson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

//    public String deleteJson(Object requestEntity) throws ClientErrorException {
//        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
//    }

    public String getJson(String lozinka) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }
    
}
