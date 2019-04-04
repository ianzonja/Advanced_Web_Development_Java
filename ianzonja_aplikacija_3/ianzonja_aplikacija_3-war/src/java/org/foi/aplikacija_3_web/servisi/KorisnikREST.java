/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_3_web.servisi;

import com.google.gson.Gson;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("/{korisnickoIme}")
public class KorisnikREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KorisnikREST
     */
    public KorisnikREST() {
    }

    /**
     * Retrieves representation of an instance of org.foi.aplikacija_3_web.servisi.KorisnikREST
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("lozinka") String lozinka) {
        String naredba = "Korisnik ianzonja; LOZINKA tava8574; LISTAJ;";
        String odgovor = SocketKlijent.posaljiZahtjevNaSocket(naredba);
        if (odgovor.contains("prezime") && odgovor.contains("ime") && odgovor.contains("ki")) {
            odgovor = odgovor.substring(7, odgovor.length() - 2);
        }
        System.out.println(odgovor);
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
            if(korisnik.getKorime().equals(context.getPath()))
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
     * PUT method for updating or creating an instance of KorisnikREST
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String putJson(String content) {
        boolean postojiLozinka = false;
        boolean postojiIme = false;
        boolean postojiPrezime = false;
        boolean postojiNovoKorIme = false;
        boolean postojiNovaLozinka = false;
        if(content.contains("lozinkaVrijednost"))
            postojiLozinka = true;
        if(content.contains("imeVridnost"))
            postojiIme = true;
        if(content.contains("prezimeVrijednost"))
            postojiPrezime = true;
        if(content.contains("novoKorIme"))
            postojiNovoKorIme = true;
        if(content.contains("novaLozinka"))
            postojiNovaLozinka = true;
        
        JsonReader reader = Json.createReader(new StringReader(content));
        JsonObject jo = reader.readObject();
        String komanda = "";
        if(postojiLozinka)
        {
            String lozinka = jo.getJsonString("lozinkaVrijednost").toString();
            lozinka = lozinka.substring(1, lozinka.length()-1);
            komanda = "KORISNIK "+context.getPath()+"; LOZINKA "+lozinka+"; AZURIRAJ";
            if(postojiPrezime)
                komanda = komanda + " "+jo.getJsonString("prezimeVrijednost").toString()+"";
            if(postojiIme)
                komanda = komanda + " "+jo.getJsonString("imeVridnost").toString()+"";
            if(postojiNovoKorIme)
                komanda = komanda + " "+jo.getJsonString("novoKorIme")+"";
            if(postojiNovaLozinka)
                komanda = komanda + " "+jo.getJsonString("novaLozinka").toString();
            komanda = komanda + ";";
            String odgovor = SocketKlijent.posaljiZahtjevNaSocket(komanda);
            System.out.println("pa onda odgovor je: "+odgovor);
            if(odgovor.contains("OK 10;"))
                return "{\"odgovor\": [{...}], \"status\": \"OK\"}";
            else if(odgovor.contains("ERR 10"))
                return "{\"odgovor\": [{...}], \"status\": \"ERR\", \"poruka\": \"korisnik nije azuriran\"}";
            System.out.println(komanda);
        }
        return "{\"odgovor\": \""+komanda+"\", \"status\": \"ERR\", \"poruka\": \"Sintaksa json-a kriva. Pokusajte ponovo.\"}";
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteJson(String content){
        if (content.contains("lozinka")) {
            System.out.println("uso sam u deleteJson if uvjet");
            JsonReader reader = Json.createReader(new StringReader(content));
            
            JsonObject jo = reader.readObject();
            System.out.println("dobio wam json objekt");
            String lozinka = jo.getJsonString("lozinka").toString();
            System.out.println("dobio sam lozinku");
            String komanda = "KORISNIK "+context.getPath()+"; LOZINKA "+lozinka+";";
            String odgovor = SocketKlijent.posaljiZahtjevNaSocket(komanda);
            if(odgovor.contains("OK 10"))
                return "{\"odgovor\": [{\"korisnik\": \""+context.getPath()+"\"}], \"status\": \"OK\"}";
            else if(odgovor.contains("ERR 11"))
                return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Korisnik nije autenticiran\"}";
            else
                return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Pogresna sintaksa za autenticiranje, posaljite ispravno korisnicko ime i lozinku\"}";
        }
        return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Pogresna sintaksa za autenticiranje, posaljite ispravno korisnicko ime i lozinku\"}";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String content){
        if(content.contains("lozinka") && content.contains("ime") && content.contains("prezime")){
            JsonReader reader = Json.createReader(new StringReader(content));
            
            JsonObject jo = reader.readObject();
            String lozinka = jo.getJsonString("lozinka").toString();
            String ime = jo.getJsonString("ime").toString();
            String prezime = jo.getJsonString("prezime").toString();
            String naredba = "KORISNIK " + context.getPath() + "; LOZINKA " + lozinka + "; DODAJ \"" + prezime + "\" \"" + ime + "\";";
            String odgovor = SocketKlijent.posaljiZahtjevNaSocket(naredba);
            if (odgovor.contains("OK 10;")) {
                return "{\"odgovor\": [], \"status\": \"OK\"}";
            }
            return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"" + odgovor + "\"}";
        }
        else
            return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Pogresna sintaksa poziva\"}";
    }
}
