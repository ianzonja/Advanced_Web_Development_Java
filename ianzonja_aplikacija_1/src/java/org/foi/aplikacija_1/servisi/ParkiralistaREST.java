/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.servisi;

import com.google.gson.Gson;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.foi.aplikacija_1.klijenti.GMKlijent;
import org.foi.aplikacija_1.podaci.Parkiraliste;
import org.foi.aplikacija_1.podaci.Lokacija;
import org.foi.aplikacija_1.podaci.RestOdgovor;
import org.foi.aplikacija_1.slusaci.SlusacAplikacije;
import org.foi.aplikacija_1.utils.RadSaBazom;
import org.foi.nwtis.dkermek.ws.serveri.Parkiranje_Service;
import org.foi.nwtis.ianzonja.konfiguracije.Konfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ianzonja.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.dkermek.ws.serveri.Parkiranje;

/**
 *
 * @author Ian
 */
@Path("/parkiralista")
public class ParkiralistaREST {
    
    private Konfiguracija konfig;
    private String url;
    @Context
    private UriInfo context;
    
    public ParkiralistaREST(){
        String datoteka = SlusacAplikacije.sc.getInitParameter("konfiguracija");
        String putanja = SlusacAplikacije.sc.getRealPath("/WEB-INF") + java.io.File.separator;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja + datoteka);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ParkiralistaREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.url = konfig.dajPostavku("server.database") + konfig.dajPostavku("admin.database");
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        BP_Konfiguracija bpk = SlusacAplikacije.bpk;
        String zapis = "Primljen REST zahtjev: Preuzimanje svih parkiralista";
        RadSaBazom.upisiUDnevnik(zapis, bpk);
        String upit = "Select * from PARKIRALISTA";
        List<Parkiraliste> listaParkiralista = RadSaBazom.dohvatiParkiralista(upit, SlusacAplikacije.bpk);
        Parkiranje_Service servis = new Parkiranje_Service();
        Parkiranje parkiranje = servis.getParkiranjePort();
        List<org.foi.nwtis.dkermek.ws.serveri.Parkiraliste> listaParkiralistaWS = parkiranje.dajSvaParkiralistaGrupe("ianzonja", "tava8574");       
        String json = "";
        RestOdgovor odgovor = new RestOdgovor();
        if (listaParkiralistaWS.size() > 0) {
            Gson gson = new Gson();
            String jsonparkiralista = gson.toJson(listaParkiralistaWS);
            json = "{\"odgovor\": "+jsonparkiralista+", \"status\": \"OK\"}";
        } else {
            odgovor.setStatus("ERR");
            odgovor.setPoruka("Nema nijednog parkiralista");
            json = new Gson().toJson(odgovor);
        }

        return json;
    }
    
    @POST 
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String podaci) {
        System.out.println("uso u dodavanje parkiralista");
        String naziv;
        String adresa;
        String kapacitet;
        BP_Konfiguracija bpk = SlusacAplikacije.bpk;
        String zapis = "Primljen REST zahtjev: Dodavanje parkiralista";
        RadSaBazom.upisiUDnevnik(zapis, bpk);
        try {
            JsonReader reader = Json.createReader(new StringReader(podaci));
            JsonObject jo = reader.readObject();
            naziv = jo.getJsonString("naziv").toString();
            adresa = jo.getJsonString("adresa").toString();
            kapacitet = jo.getJsonString("kapacitet").toString();
            System.out.println("sto se desava?");
        }catch(Exception ex){
            return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Pogresna json sintaksa\"}";
        }
        GMKlijent gmk = new GMKlijent(konfig.dajPostavku("gmapikey"));
        Lokacija l = gmk.getGeoLocation(adresa);
        adresa = adresa.replace("\"", "");
        naziv = naziv.replace("\"", "");
        kapacitet = kapacitet.replace("\"", "");
        String upit = "INSERT into parkiralista (id, naziv, adresa, latitude, longitude) "
                + "VALUES(default,'" + naziv + "','" + adresa + "'," + l.getLatitude() + "," + l.getLongitude() + ")";
        boolean uneseno = RadSaBazom.unesiParkiraliste(upit, bpk);
        System.out.println("uneseno u bazu: " + uneseno);
        if(uneseno){
            int id = 0;
            System.out.println("uso u uneseno");
            id = dohvatiIdParkiralista(naziv);
            if (id!=0) {
                Parkiranje_Service servis = new Parkiranje_Service();
                Parkiranje parkiranje = servis.getParkiranjePort();
                parkiranje.dodajNovoParkiralisteGrupi("ianzonja", "tava8574", id, naziv, adresa, Integer.valueOf(kapacitet));
                return "{\"odgovor\": [], \"status\": \"OK\" }";
            }else
                return "{\"odgovor\": [], \"status\": \"ERR\": \"poruka\": \"Dogodila se greska, parkiraliste nije uneseno\"}";
        }
        return "{\"odgovor\": [], \"status\": \"ERR\": \"poruka\": \"Parkiraliste nije uneseno!\"}";
    }
  
    private int dohvatiIdParkiralista(String naziv) {
        String upit = "Select * from parkiralista";
        BP_Konfiguracija bpk = SlusacAplikacije.bpk;
        List<Parkiraliste> listaDodanihParkiralista = RadSaBazom.dohvatiParkiralista(upit, bpk);
        int id = 0;
        for (Parkiraliste p : listaDodanihParkiralista) {
            if (p.getNaziv().equals(naziv)) {
                id = p.getId();
            }
        }
        return id;
    }
}
