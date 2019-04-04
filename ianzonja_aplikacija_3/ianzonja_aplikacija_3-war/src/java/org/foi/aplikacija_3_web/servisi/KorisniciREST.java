/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_3_web.servisi;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.foi.aplikacija_3_web.klijenti.SocketKlijent;
import org.foi.aplikacija_3_web_podaci.Korisnik;

/**
 * REST Web Service
 *
 * @author Ian
 */
@Path("/")
public class KorisniciREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KorisniciREST
     */
    public KorisniciREST() {
    }

    /**
     * Retrieves representation of an instance of org.foi.aplikacija_3_web.servisi.KorisniciREST
     * @param username
     * @param lozinka
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("username") String username, @QueryParam("password") String lozinka) {
        //treba jos dodati uvjet da su uneseni username i password kod requesta
        System.out.println("poziv je dosao!!!! haleluja");
        String naredba = "Korisnik "+username+"; LOZINKA "+lozinka+"; LISTAJ;";
        System.out.println("nardba dohvacanja: " + naredba);
        String odgovor = SocketKlijent.posaljiZahtjevNaSocket(naredba);
        if (odgovor.contains("prezime") && odgovor.contains("ime") && odgovor.contains("ki")) {
            odgovor = odgovor.substring(7, odgovor.length() - 2);
        }
        else{
            return "{\"odgovor\": \"[]\", \"status\": \"ERR\", \"poruka\": \"Korisnik nije autenticiran\"}";
        }
        JsonReader reader = Json.createReader(new StringReader(odgovor));
        System.out.println(odgovor);
        JsonArray ja = reader.readArray();
        List<Korisnik> listaKorisnika = new ArrayList<>();
        for (int i = 0; i < ja.size(); i++) {
            String redak = ja.get(i).toString();
            JsonReader readerRedka = Json.createReader(new StringReader(redak));
            JsonObject jo = readerRedka.readObject();
            Korisnik korisnik = new Korisnik();
            korisnik.setIme(jo.getJsonString("ime").toString().substring(1, jo.getJsonString("ime").toString().length()-1));
            korisnik.setPrezime(jo.getJsonString("prezime").toString().substring(1, jo.getJsonString("prezime").toString().length()-1));
            korisnik.setKorime(jo.getJsonString("ki").toString().substring(1, jo.getJsonString("ki").toString().length()-1));
            listaKorisnika.add(korisnik);
        }
        String odgovorResta = "";
        odgovorResta = "{\"odgovor\": ";
        String json = "";
        if (listaKorisnika.size() > 0) {
            Gson gson = new Gson();
            json = gson.toJson(listaKorisnika);
            odgovorResta = odgovorResta + json + ", \"status\": \"OK\"}";
        }
        else{
            odgovorResta = "\"\", \"status\": \"ERR\", \"poruka\": \"Nema klijenata u bazi\"}";
        }
        System.out.println(odgovorResta);
        return odgovorResta;
    }

    /**
     * PUT method for updating or creating an instance of KorisniciREST
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String putJson(String content) {
        if (content.contains("lozinka")) {
            System.out.println("uso sam u deleteJson if uvjet");
            JsonReader reader = Json.createReader(new StringReader(content));
            
            JsonObject jo = reader.readObject();
            System.out.println("dobio wam json objekt");
            String lozinka = jo.getJsonString("lozinka").toString();
            String korIme = jo.getJsonString("korime").toString();
            System.out.println("dobio sam lozinku");
            String komanda = "KORISNIK "+korIme+"; LOZINKA "+lozinka+";";
            System.out.println(komanda);
            String odgovor = SocketKlijent.posaljiZahtjevNaSocket(komanda);
            if(odgovor.contains("OK 10"))
                return "{\"odgovor\": [], \"status\": \"OK\"}";
            else if(odgovor.contains("ERR 11"))
                return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Korisnik nije autenticiran\"}";
            else
                return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Pogresna sintaksa za autenticiranje, posaljite ispravno korisnicko ime i lozinku\"}";
        }
        return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Pogresna sintaksa za autenticiranje, posaljite ispravno korisnicko ime i lozinku\"}";
    }
    
    private String spojiSeNaPosluzitelj(String naredba) {
        String adresa = "127.0.0.1";
        int port = 446;
        Socket socket;
        try {
            socket = new Socket(adresa, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            os.write(naredba.getBytes());
            os.flush();
            socket.shutdownOutput();
            StringBuffer sb = new StringBuffer();
            int bajt = 0;
            while (bajt != -1) {
                bajt = is.read();
                sb.append((char) bajt);
            }
            return sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(KorisniciREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Doso sam do kraja";
    }
       
}
