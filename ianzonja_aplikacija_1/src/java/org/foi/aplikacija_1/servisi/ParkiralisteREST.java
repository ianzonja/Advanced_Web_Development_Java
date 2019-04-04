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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.foi.aplikacija_1.klijenti.GMKlijent;
import org.foi.aplikacija_1.podaci.Lokacija;
import org.foi.aplikacija_1.podaci.Parkiraliste;
import org.foi.aplikacija_1.podaci.RestOdgovor;
import org.foi.aplikacija_1.slusaci.SlusacAplikacije;
import org.foi.aplikacija_1.utils.RadSaBazom;
import org.foi.nwtis.dkermek.ws.serveri.Parkiranje_Service;
import org.foi.nwtis.dkermek.ws.serveri.StatusParkiralista;
import org.foi.nwtis.dkermek.ws.serveri.Vozilo;
import org.foi.nwtis.ianzonja.konfiguracije.Konfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;

/**
 * REST Web Service
 *
 * @author Ian
 */
@Path("/{id}")
public class ParkiralisteREST {

    @Context
    private UriInfo context;
    private BP_Konfiguracija konfig;

    /**
     * Creates a new instance of ParkiralisteREST
     */
    public ParkiralisteREST() {
    }

    /**
     * Retrieves representation of an instance of org.foi.aplikacija_1.servisi.ParkiralisteREST
     * @param id resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(String content) {
//        JsonReader reader = Json.createReader(new StringReader(content));
//        JsonObject jo = reader.readObject();
//        String korime = "";
//        String lozinka = "";
//        if (content.contains("korime") && content.contains("lozinka")) {
//            korime = jo.getJsonString("korime").toString();
//            lozinka = jo.getJsonString("lozinka").toString();
//        }else{
//            return "{\"odgovor\": [], \"ERR\", \"poruka\": \"Nisu poslani korime i lozinka za servis.\"}";
//        }
//        Parkiranje p = new Parkiranje();
//        List<org.foi.nwtis.dkermek.ws.serveri.Parkiraliste> listaParkiralistaWs = p.dajSvaParkiralistaGrupe(korime, lozinka);
        BP_Konfiguracija bpk = SlusacAplikacije.bpk;
        String zapis = "Primljen REST zahtjev: Preuzimanje jednog parkiralista";
        RadSaBazom.upisiUDnevnik(zapis, bpk);
        int idValue = 0;
        String requestId = context.getPath().replace("/", "");
        try {
            idValue = Integer.parseInt(requestId);
        } catch (Exception ex) {
            return "{\"odgovor\": [],"
                    + "\"status\": \"ERR\", \"poruka\": \"Unesena pogrešna vrijednost ID-a\"}";
        }
        Parkiranje_Service servis = new Parkiranje_Service();
        org.foi.nwtis.dkermek.ws.serveri.Parkiranje parkiranje = servis.getParkiranjePort();
        List<org.foi.nwtis.dkermek.ws.serveri.Parkiraliste> listaParkiralistaWS = parkiranje.dajSvaParkiralistaGrupe("ianzonja", "tava8574");
        for(int i = 0; i<listaParkiralistaWS.size(); i++){
            if(listaParkiralistaWS.get(i).getId() == idValue){
                String jsonParkiralista = new Gson().toJson(listaParkiralistaWS.get(i));
                return "{\"odgovor\": [" + jsonParkiralista + "], \"status\": \"OK\"}";
            }
        }
        String json = "";
        RestOdgovor odgovor = new RestOdgovor();
        odgovor.setStatus("ERR");
        odgovor.setPoruka("Nema zadanog parkirališta u bazi");
        json = new Gson().toJson(odgovor);
        String upit = "Select * from PARKIRALISTA where ID="+requestId+"";
        List<Parkiraliste> listaParkiralista = RadSaBazom.dohvatiParkiralista(upit, SlusacAplikacije.bpk);
        return json;
    }

    /**
     * PUT method for updating or creating an instance of ParkiralisteREST
     * @param id resource URI parameter
     * @param content representation for the resource
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(String content) {
        BP_Konfiguracija bpk = SlusacAplikacije.bpk;
        String zapis = "Primljen REST zahtjev: Azuriranje parkiralista";
        RadSaBazom.upisiUDnevnik(zapis, bpk);
        String naziv = "";
        String adresa = "";
        String kapacitet = "";
        String sp = "";
        System.out.println("CONTENT: " + content);
        String id = context.getPath();
        try {
            JsonReader reader = Json.createReader(new StringReader(content));
            JsonObject jo = reader.readObject();
            if (content.contains("naziv")) {
                naziv = jo.getJsonString("naziv").toString();
                if (naziv.startsWith("\"") && naziv.endsWith("\"")) {
                    naziv = naziv.replace("\"", "");
                }
            }
            if (content.contains("adresa")) {
                adresa = jo.getJsonString("adresa").toString();
                if (adresa.startsWith("\"") && adresa.endsWith("\"")) {
                    adresa = adresa.replace("\"", "");
                }
            }
            if (content.contains("kapacitet")) {
                kapacitet = jo.getJsonString("kapacitet").toString();
                if (kapacitet.startsWith("\"") && kapacitet.endsWith("\"")) {
                    kapacitet = kapacitet.replace("\"", "");
                }
            }
            if (content.contains("status")) {
                sp = jo.getJsonString("status").toString();
                if (sp.startsWith("\"") && sp.endsWith("\"")) {
                    sp = sp.replace("\"", "");
                }
            }
        }catch(Exception ex){
            return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Neispravan json\"}";
        }
        id = id.replace("/", "");
        System.out.println("pa id je: " + id);
        int idParkiralista = 0;
        try {
            idParkiralista = Integer.parseInt(id);
        } catch (Exception ex) {
            System.out.println("los id!");
            return "{\"odgovor\": [],"
                    + "\"status\": \"ERR\", \"poruka\": \"Pogrešna vrijednost id-a\"}";
        }
        System.out.println("wtf");
        GMKlijent gmk = new GMKlijent("AIzaSyDSWYWUaOQ8oU5UxasxANk7C5FXeylLtVU");
        konfig = SlusacAplikacije.bpk;
        Parkiranje_Service servis = new Parkiranje_Service();
        org.foi.nwtis.dkermek.ws.serveri.Parkiranje parkiranje = servis.getParkiranjePort();
        List<org.foi.nwtis.dkermek.ws.serveri.Parkiraliste> listaParkiralistaWS = parkiranje.dajSvaParkiralistaGrupe("ianzonja", "tava8574");
        org.foi.nwtis.dkermek.ws.serveri.Parkiraliste p = new org.foi.nwtis.dkermek.ws.serveri.Parkiraliste();
        System.out.println("Velicina liste: " + listaParkiralistaWS.size());
        for(int i = 0; i<listaParkiralistaWS.size(); i++){
            System.out.println("id:" + String.valueOf(listaParkiralistaWS.get(i).getId()));
            if(String.valueOf(listaParkiralistaWS.get(i).getId()).equals(id)){
                org.foi.nwtis.dkermek.ws.serveri.Parkiraliste park = new org.foi.nwtis.dkermek.ws.serveri.Parkiraliste();
                park = listaParkiralistaWS.get(i);
                if(parkiranje.obrisiParkiralisteGrupe("ianzonja", "tava8574", Integer.valueOf(id))){
                    System.out.println("obrisao sam parkiraliste");
                }else
                    System.out.println("nisam obrisao");
                park.setId(Integer.valueOf(id));
                if(!naziv.equals(""))
                    park.setNaziv(naziv);
                if(!adresa.equals(""))
                    park.setAdresa(adresa);
                if(!kapacitet.equals(""))
                    park.setKapacitet(Integer.parseInt(kapacitet));
                if(sp.equals("AKTIVAN"))
                    park.setStatus(StatusParkiralista.AKTIVAN);
                if(sp.equals("BLOKIRAN"))
                    park.setStatus(StatusParkiralista.BLOKIRAN);
                Lokacija l = null;
                if (!adresa.equals("")) {
                    l = gmk.getGeoLocation(adresa);
                } else {
                    l = new Lokacija(park.getGeoloc().getLatitude(), park.getGeoloc().getLongitude());
                }
                org.foi.nwtis.dkermek.ws.serveri.Lokacija lokacija = new org.foi.nwtis.dkermek.ws.serveri.Lokacija();
                lokacija.setLatitude(l.getLatitude());
                lokacija.setLongitude(l.getLongitude());
                if(naziv.equals(""))
                    naziv = park.getNaziv();
                if(adresa.equals(""))
                    adresa = park.getAdresa();
                if (parkiranje.dodajParkiralisteGrupi("ianzonja", "tava8574", park)) {
                    System.out.println("Dodajo sam");
                } else {
                    System.out.println("nisam dodao");
                }
                String upit = "Update Parkiralista set naziv='" + naziv + "', adresa='" + adresa + "', latitude=" + l.getLatitude() + ", longitude=" + l.getLongitude() + " where id=" + id;
                RadSaBazom.azurirajParkiraliste(upit, konfig);
                return "{\"odgovor\": [],"
                        + "\"status\": \"OK\"}";
            }
        }
        return "{\"odgovor\": [],"
                    + "\"status\": \"ERR\", \"poruka\": \"Zapis ne postoji u bazi\"}";
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteJson() {
        BP_Konfiguracija bpk = SlusacAplikacije.bpk;
        String zapis = "Primljen REST zahtjev: Brisanje parkiralista";
        RadSaBazom.upisiUDnevnik(zapis, bpk);
        String id = context.getPath();
        id = id.replace("/", "");
        int idParkiralista = 0;
        try {
            idParkiralista = Integer.parseInt(id);
        } catch (Exception ex) {
            return "{\"odgovor\": [],"
                    + "\"status\": \"ERR\", \"poruka\": \"Pogrešna vrijednost id-a\"}";
        }
        Parkiranje_Service servis = new Parkiranje_Service();
        org.foi.nwtis.dkermek.ws.serveri.Parkiranje parkiranje = servis.getParkiranjePort();
        if (parkiranje.obrisiParkiralisteGrupe("ianzonja", "tava8574", idParkiralista)) {
            String upit = "Delete from Parkiralista where id=" + idParkiralista;
            RadSaBazom.obrisiParkiraliste(upit, bpk);
            return "{\"odgovor\": [], \"status\": \"OK\"}";
        }
        return "{\"odgovor\": [], \"status\": \"ERR\", \"poruka\": \"Parkiralište ne postoji u bazi\"}";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String content){
        String id = context.getPath().replace("/", "");
        int idParkiralista = Integer.valueOf(id);
        Parkiranje_Service servis = new Parkiranje_Service();
        org.foi.nwtis.dkermek.ws.serveri.Parkiranje parkiranje = servis.getParkiranjePort();
        List<Vozilo> listaVozila = parkiranje.dajSvaVozilaParkiralistaGrupe("ianzonja", "tava8574", idParkiralista);
        String jsonVozila = new Gson().toJson(listaVozila);
        System.out.println("Vozila string: " + jsonVozila);
        if(listaVozila.size() > 0)
            return "{\"odgovor\": "+jsonVozila+", \"status\": \"OK\"}";
        else
            return "{\"odgovor\": [], \"status\":\"ERR\", \"poruka\": \"Parkiraliste nema vozila\"}";
    }
}
