/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.ianzonja.web.zrna;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.foi.ianzonja.web.podaci.Lokacija;
import org.foi.ianzonja.web.podaci.Parkiraliste;
import org.foi.ianzonja.web.podaci.Vozilo;
import org.foi.ianzonja.web.rest.klijenti.OdabranoParkiralisteRest;
import org.foi.ianzonja.web.rest.klijenti.ParkiralistaRestKlijent;
import org.foi.ianzonja.web.rest.klijenti.ParkiralisteRestKlijent;
import org.foi.ianzonja.web.soap.klijenti.MeteoPodaci;
import org.foi.ianzonja.web.soap.klijenti.Meteo_Service;
import org.foi.ianzonja.web.soap.klijenti.Meteo;

/**
 *
 * @author Ian
 */
@Named(value = "pregledParkiralista")
@SessionScoped
public class PregledParkiralistaZrno implements Serializable{
    private String parkiralista;
    private boolean prikaziGresku;
    private boolean prikaziListuParkiralista;
    private boolean prikaziListuMeteo;
    private boolean prikaziListuVozila;
    private List<Vozilo> listaSvihVozila = new ArrayList<>();
    private List<Vozilo> listaVozila = new ArrayList<>();

    public boolean isPrikaziListuVozila() {
        return prikaziListuVozila;
    }

    public void setPrikaziListuVozila(boolean prikaziListuVozila) {
        this.prikaziListuVozila = prikaziListuVozila;
    }

    public List<Vozilo> getListaVozila() {
        return listaVozila;
    }

    public void setListaVozila(List<Vozilo> listaVozila) {
        this.listaVozila = listaVozila;
    }
    private List<MeteoPodaci> listaMeteo = new ArrayList<>();
    private MeteoPodaci mp;
    private String id;
    private int stranicenje = 4;
    private int od = 0;
    private int doBroja = 4;
    private List<Parkiraliste> listaSvihParkiralista = new ArrayList<>();
    private boolean prikaziSljedecu;
    private boolean prikaziPrethodnu;
    private boolean prikaziSljedecuVozila;

    public boolean isPrikaziSljedecuVozila() {
        return prikaziSljedecuVozila;
    }

    public void setPrikaziSljedecuVozila(boolean prikaziSljedecuVozila) {
        this.prikaziSljedecuVozila = prikaziSljedecuVozila;
    }

    public boolean isPrikaziPrethodnuVozila() {
        return prikaziPrethodnuVozila;
    }

    public void setPrikaziPrethodnuVozila(boolean prikaziPrethodnuVozila) {
        this.prikaziPrethodnuVozila = prikaziPrethodnuVozila;
    }
    private boolean prikaziPrethodnuVozila;

    public boolean isPrikaziSljedecu() {
        return prikaziSljedecu;
    }

    public void setPrikaziSljedecu(boolean prikaziSljedecu) {
        this.prikaziSljedecu = prikaziSljedecu;
    }

    public boolean isPrikaziPrethodnu() {
        return prikaziPrethodnu;
    }

    public void setPrikaziPrethodnu(boolean prikaziPrethodnu) {
        this.prikaziPrethodnu = prikaziPrethodnu;
    }

    public List<Parkiraliste> getListaSvihParkiralista() {
        return listaSvihParkiralista;
    }

    public void setListaSvihParkiralista(List<Parkiraliste> listaSvihParkiralista) {
        this.listaSvihParkiralista = listaSvihParkiralista;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MeteoPodaci getMp() {
        return mp;
    }

    public void setMp(MeteoPodaci mp) {
        this.mp = mp;
    }

    public List<MeteoPodaci> getListaMeteo() {
        return listaMeteo;
    }

    public void setListaMeteo(List<MeteoPodaci> listaMeteo) {
        this.listaMeteo = listaMeteo;
    }

    public boolean isPrikaziListuMeteo() {
        return prikaziListuMeteo;
    }

    public void setPrikaziListuMeteo(boolean prikaziListuMeteo) {
        this.prikaziListuMeteo = prikaziListuMeteo;
    }

    public String getParkiralista() {
        return parkiralista;
    }

    public void setParkiralista(String parkiralista) {
        this.parkiralista = parkiralista;
    }

    public boolean isPrikaziListuParkiralista() {
        return prikaziListuParkiralista;
    }

    public void setPrikaziListuParkiralista(boolean prikaziListuParkiralista) {
        this.prikaziListuParkiralista = prikaziListuParkiralista;
    }

    public boolean isPrikaziGresku() {
        return prikaziGresku;
    }

    public void setPrikaziGresku(boolean prikaziGresku) {
        this.prikaziGresku = prikaziGresku;
    }

    public String getPorukaGreske() {
        return porukaGreske;
    }

    public void setPorukaGreske(String porukaGreske) {
        this.porukaGreske = porukaGreske;
    }
    private String porukaGreske;
    private List<Parkiraliste> listaParkiralista = new ArrayList<>();
    public List<Parkiraliste> getListaParkiralista() {
        return listaParkiralista;
    }
    private String naziv;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(String kapacitet) {
        this.kapacitet = kapacitet;
    }
    private String adresa;
    private String kapacitet;

    public void setListaParkiralista(List<Parkiraliste> listaParkiralista) {
        this.listaParkiralista = listaParkiralista;
    }
    /**
     * Creates a new instance of PregledParkiralista
     */
    public PregledParkiralistaZrno() {
    }
   
    public void dajListuParkiralista() {
        od = 0;
        doBroja = 4;
        prikaziListuVozila = false;
        prikaziSljedecu = true;
        prikaziPrethodnu = false;
        prikaziSljedecuVozila = false;
        prikaziPrethodnuVozila = false;
        listaParkiralista.clear();
        listaSvihParkiralista.clear();
        prikaziListuParkiralista = true;
        prikaziListuMeteo = false;
        prikaziGresku = false;
        ParkiralistaRestKlijent prk = new ParkiralistaRestKlijent();
        parkiralista = prk.getJson();
        System.out.println("tu sam " + parkiralista);
        if (!parkiralista.contains("poruka") && !parkiralista.contains("ERR")) {
            parkiralista = parkiralista.substring(parkiralista.indexOf('['), parkiralista.indexOf(']') + 1);
            JsonReader reader = Json.createReader(new StringReader(parkiralista));
            JsonArray ja = reader.readArray();
            for (int i = 0; i < ja.size(); i++) {
                String redak = ja.get(i).toString();
                JsonReader readerRedka = Json.createReader(new StringReader(redak));
                JsonObject jo = readerRedka.readObject();
                Parkiraliste p = new Parkiraliste();
                if (redak.contains("adresa")) {
                    p.setAdresa(jo.getJsonString("adresa").toString());
                }
                if (redak.contains("naziv")) {
                    p.setNaziv(jo.getJsonString("naziv").toString());
                }
                if (redak.contains("kapacitet")) {
                    p.setKapacitet(jo.getJsonNumber("kapacitet").toString());
                }
                if(redak.contains("id")){
                    p.setId(jo.getJsonNumber("id").toString());
                }
                if (redak.contains("geoloc")) {
                    JsonObject ob = jo.getJsonObject("geoloc");
                    Lokacija l = new Lokacija(ob.getJsonString("latitude").toString(), ob.getJsonString("longitude").toString());
                    p.setLokacija(l);
                }
                if(redak.contains("status")){
                    p.setStatus(jo.getJsonString("status").toString());
                }
                listaSvihParkiralista.add(p);
                System.out.println("Velicina: " + listaParkiralista.size());
            }
            for (int j = 0; j < stranicenje; j++) {
                listaParkiralista.add(listaSvihParkiralista.get(j));
            }
        } else {
            prikaziGresku = true;
            porukaGreske = parkiralista;
        }
    }

    public void dodajParkiraliste() {
        prikaziGresku = false;
        if (!naziv.equals("") && !adresa.equals("") && !kapacitet.equals("")) {
            String json = "{\"naziv\": \"" + naziv + "\", \"adresa\": \"" + adresa + "\", \"kapacitet\": \"" + kapacitet + "\"}";
            ParkiralistaRestKlijent prk = new ParkiralistaRestKlijent();
            String odgovor = prk.postJson(json);
        }
        else{
            prikaziGresku = true;
            porukaGreske = "Potrebno je unijeti sve podatke";
        }
    }
    
    public void dajZadnjeMeteoPodatke() {
        prikaziSljedecu = false;
        prikaziPrethodnu = false;
        prikaziSljedecuVozila = false;
        prikaziPrethodnuVozila = false;
        prikaziListuVozila = false;
        prikaziListuMeteo = true;
        prikaziListuParkiralista = false;
        prikaziGresku = false;
        listaMeteo.clear();
        if (!naziv.equals("")) {
            Meteo_Service service = new Meteo_Service();
            Meteo meteo = service.getMeteoPort();
            mp = meteo.zadnjePreuzetiPodaci(naziv);
            listaMeteo.add(mp);
        }else{
            prikaziGresku = true;
            porukaGreske = "Unesite naziv parkiralista!";
        }
    }
    
    public void dajVazecePodatke(){
        prikaziSljedecu = false;
        prikaziPrethodnu = false;
        prikaziSljedecuVozila = false;
        prikaziPrethodnuVozila = false;
        prikaziListuVozila = false;
        prikaziListuMeteo = true;
        prikaziListuParkiralista = false;
        prikaziGresku = false;
        listaMeteo.clear();
        if (!naziv.equals("")) {
            Meteo_Service service = new Meteo_Service();
            Meteo meteo = service.getMeteoPort();
            mp = meteo.vezeciPodaciZaParkiraliste(naziv);
            listaMeteo.add(mp);
        }else{
            prikaziGresku = true;
            porukaGreske = "Unesite naziv parkiralista!";
        }
    }
    
    public void obrisiParkiraliste(){
        prikaziGresku = true;
        if(!id.equals("")){
            OdabranoParkiralisteRest prk = new OdabranoParkiralisteRest(id);
            String odgovor = prk.deleteJson();
            porukaGreske = odgovor;
        }else
            porukaGreske = "Unesite id parkiralista!";
    }
    
    public void dajPrethodnu(){
        listaParkiralista.clear();
        od = od - stranicenje;
        doBroja = doBroja - stranicenje;
        for(int i = od; i<doBroja; i++){
            if(i < listaSvihParkiralista.size())
                listaParkiralista.add(listaSvihParkiralista.get(i));
        }
        if(od > 0)
            prikaziPrethodnu = true;
        else
            prikaziPrethodnu = false;
        if(doBroja < listaSvihParkiralista.size())
            prikaziSljedecu = true;
        else
            prikaziSljedecu = false;
    }
    
    public void dajSljedecu(){
        listaParkiralista.clear();
        od = od + stranicenje;
        doBroja = doBroja + stranicenje;
        for(int i = od; i<doBroja; i++){
            if(i < listaSvihParkiralista.size())
                listaParkiralista.add(listaSvihParkiralista.get(i));
        }
        if(od > 0)
            prikaziPrethodnu = true;
        else
            prikaziPrethodnu = false;
        if(doBroja < listaSvihParkiralista.size())
            prikaziSljedecu = true;
        else
            prikaziSljedecu = false;
    }
    
    public void aktivirajParkiraliste(){
        OdabranoParkiralisteRest prk = new OdabranoParkiralisteRest(id);
        String content = "{\"status\":\"AKTIVAN\"}";
        String odgovor = prk.putJson(content);
        prikaziGresku = true;
        porukaGreske = odgovor;
    }

    public void blokirajParkiraliste() {
        OdabranoParkiralisteRest prk = new OdabranoParkiralisteRest(id);
        String content = "{\"status\":\"BLOKIRAN\"}";
        String odgovor = prk.putJson(content);
        prikaziGresku = true;
        porukaGreske = odgovor;
    }

    public void dajListuVozila() {
        prikaziSljedecu = false;
        prikaziPrethodnu = false;
        prikaziSljedecuVozila = true;
        prikaziPrethodnuVozila = false;
        od = 0;
        doBroja = 4;
        listaVozila.clear();
        listaSvihVozila.clear();
        prikaziListuMeteo = false;
        prikaziListuParkiralista = false;
        prikaziListuVozila = true;
        OdabranoParkiralisteRest prk = new OdabranoParkiralisteRest(id);
        String odgovor = prk.postJson();
        if (odgovor.contains("OK") && !odgovor.contains("poruka")) {
            String jsonVozila = odgovor.substring(odgovor.indexOf('['), odgovor.indexOf(']')+1);
            System.out.println("odgovor: " + odgovor);
            System.out.println("json vozila: " + jsonVozila);
            JsonReader reader = Json.createReader(new StringReader(jsonVozila));
            JsonArray ja = reader.readArray();
            for (int i = 0; i < ja.size(); i++) {
                String redak = ja.get(i).toString();
                JsonReader readerRedka = Json.createReader(new StringReader(redak));
                JsonObject jo = readerRedka.readObject();
                Vozilo v = new Vozilo();
                if (redak.contains("akcija")) {
                    v.setAkcija(jo.getJsonString("akcija").toString());
                }
                if (redak.contains("registracija")) {
                    v.setRegistracija(jo.getJsonString("registracija").toString());
                }
                if (redak.contains("parkiraliste")) {
                    v.setParkiraliste(jo.getJsonNumber("parkiraliste").toString());
                }
                listaSvihVozila.add(v);
            }
            for(int i = 0; i<stranicenje; i++)
                if(i < listaSvihVozila.size())
                    listaVozila.add(listaSvihVozila.get(i));
        } else {
            prikaziGresku = true;
            porukaGreske = odgovor;
        }
    }
    
    public void dajPrethodnuVozila(){
        listaVozila.clear();
        od = od - stranicenje;
        doBroja = doBroja - stranicenje;
        for(int i = od; i<doBroja; i++){
            if(i < listaSvihVozila.size())
                listaVozila.add(listaSvihVozila.get(i));
        }
        if(od > 0)
            prikaziPrethodnuVozila = true;
        else
            prikaziPrethodnuVozila = false;
        if(doBroja < listaSvihVozila.size())
            prikaziSljedecuVozila = true;
        else
            prikaziSljedecuVozila = false;
    }
    
    public void dajSljedecuVozila(){
        listaVozila.clear();
        od = od + stranicenje;
        doBroja = doBroja + stranicenje;
        for(int i = od; i<doBroja; i++){
            if(i < listaSvihVozila.size())
                listaVozila.add(listaSvihVozila.get(i));
        }
        if(od > 0)
            prikaziPrethodnuVozila = true;
        else
            prikaziPrethodnuVozila = false;
        if(doBroja < listaSvihVozila.size())
            prikaziSljedecuVozila = true;
        else
            prikaziSljedecuVozila = false;
    }
}
