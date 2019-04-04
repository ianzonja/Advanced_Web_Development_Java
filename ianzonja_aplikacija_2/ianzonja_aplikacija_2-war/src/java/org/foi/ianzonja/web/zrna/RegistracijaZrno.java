/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.ianzonja.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.foi.ianzonja.web.podaci.Korisnik;
import org.foi.ianzonja.web.rest.klijenti.KorisniciRestKlijent;
import org.foi.ianzonja.web.rest.klijenti.KorisnikRestKlijent;

/**
 *
 * @author Ian
 */
@Named(value = "registracijaZrno")
@SessionScoped
public class RegistracijaZrno implements Serializable {
    private String korime;
    private KorisnikRestKlijent korisnikRest;
    private List<Korisnik> listaKorisnika = new ArrayList<>();
    private KorisniciRestKlijent korisniciRest;
    private boolean prikaziRezultat;
    private String rezultatText;
    private List<Korisnik> listaSvihKorisnik = new ArrayList<>();
    int stranicenje = 4;
    private int od = 0;
    private int doBroja = 4;
    private boolean prikaziSljedecu;

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
    private boolean prikaziPrethodnu;

    public boolean isPrikaziRezultat() {
        return prikaziRezultat;
    }

    public void setPrikaziRezultat(boolean prikaziRezultat) {
        this.prikaziRezultat = prikaziRezultat;
    }

    public String getRezultatText() {
        return rezultatText;
    }

    public void setRezultatText(String rezultatText) {
        this.rezultatText = rezultatText;
    }

    public List<Korisnik> getListaKorinika() {
        return listaKorisnika;
    }

    public void setListaKorinika(List<Korisnik> listaKorinika) {
        this.listaKorisnika = listaKorinika;
    }

    public String getKorime() {
        return korime;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    private String lozinka;
    private String ime;
    private String prezime;
    private boolean korisnikRegistriran;
    private boolean korisnikAzuriran;
    private String korisnikAzuriranText;

    public boolean isKorisnikAzuriran() {
        return korisnikAzuriran;
    }

    public void setKorisnikAzuriran(boolean korisnikAzuriran) {
        this.korisnikAzuriran = korisnikAzuriran;
    }

    public String getKorisnikAzuriranText() {
        return korisnikAzuriranText;
    }

    public void setKorisnikAzuriranText(String korisnikAzuriranText) {
        this.korisnikAzuriranText = korisnikAzuriranText;
    }

    public boolean isKorisnikRegistriran() {
        return korisnikRegistriran;
    }

    public void setKorisnikRegistriran(boolean korisnikRegistriran) {
        this.korisnikRegistriran = korisnikRegistriran;
    }

    public String getKorisnikRegistriranText() {
        return korisnikRegistriranText;
    }

    public void setKorisnikRegistriranText(String korisnikRegistriranText) {
        this.korisnikRegistriranText = korisnikRegistriranText;
    }
    private String korisnikRegistriranText;

    /**
     * Creates a new instance of RegistracijaZrno
     */
    public RegistracijaZrno() {
    }
    
    public void izvrsiRegistraciju(){
        prikaziRezultat = false;
        korisnikRest = new KorisnikRestKlijent(korime);
        String json = "{\"lozinka\": \""+lozinka+"\", \"ime\": \""+ime+"\", \"prezime\": \""+prezime+"\"}";
        String odgovor = korisnikRest.postJson(json);
        rezultatText = odgovor;
        System.out.println("Korisnik registriran: " + odgovor);
        prikaziRezultat = true;
        
    }
    
    public void dohvatiKorisnike(){
        if (!korime.equals("") && !lozinka.equals("")) {
            prikaziSljedecu = true;
            prikaziPrethodnu = false;
            prikaziRezultat = false;
            listaKorisnika.clear();
            korisniciRest = new KorisniciRestKlijent();
            String odgovor = korisniciRest.getJson(lozinka, korime);
            odgovor = odgovor.substring(odgovor.indexOf('['), odgovor.indexOf(']') + 1);
            JsonReader reader = Json.createReader(new StringReader(odgovor));
            System.out.println(odgovor);
            JsonArray ja = reader.readArray();
            for (int i = 0; i < ja.size(); i++) {
                String redak = ja.get(i).toString();
                JsonReader readerRedka = Json.createReader(new StringReader(redak));
                JsonObject jo = readerRedka.readObject();
                Korisnik korisnik = new Korisnik();
                korisnik.setIme(jo.getJsonString("ime").toString().substring(1, jo.getJsonString("ime").toString().length() - 1));
                korisnik.setPrezime(jo.getJsonString("prezime").toString().substring(1, jo.getJsonString("prezime").toString().length() - 1));
                korisnik.setKorime(jo.getJsonString("korime").toString().substring(1, jo.getJsonString("korime").toString().length() - 1));
                listaSvihKorisnik.add(korisnik);
            }
            for (int i = 0; i < stranicenje; i++) {
                listaKorisnika.add(listaSvihKorisnik.get(i));
            }
            if (listaKorisnika.isEmpty()) {
                prikaziRezultat = true;
                rezultatText = odgovor;
            }
        } else {
            prikaziRezultat = true;
            rezultatText = "Molimo, unesite korisnicko ime i lozinku!";
        }
    }
    
    public void azurirajKorisnika(){
        prikaziRezultat = false;
        korisnikRest = new KorisnikRestKlijent(korime);
        String json = "{\"imeVridnost\": \""+ime+"\", \"prezimeVrijednost\": \""+prezime+"\", \"lozinkaVrijednost\": \""+lozinka+"\"}";
        System.out.println(json);
        String odgovor = korisnikRest.putJson(json);
        rezultatText = odgovor;
        prikaziRezultat = true;
    }
    
    public void dajPrethodnu(){
        listaKorisnika.clear();
        od = od - stranicenje;
        doBroja = doBroja - stranicenje;
        for(int i = od; i<doBroja; i++){
            if(i < listaSvihKorisnik.size())
                listaKorisnika.add(listaSvihKorisnik.get(i));
        }
        if(od > 0)
            prikaziPrethodnu = true;
        else
            prikaziPrethodnu = false;
        if(doBroja < listaSvihKorisnik.size())
            prikaziSljedecu = true;
        else
            prikaziSljedecu = false;
    }
    
    public void dajSljedecu(){
        listaKorisnika.clear();
        od = od + stranicenje;
        doBroja = doBroja + stranicenje;
        for(int i = od; i<doBroja; i++){
            if(i < listaSvihKorisnik.size())
                listaKorisnika.add(listaSvihKorisnik.get(i));
        }
        if(od > 0)
            prikaziPrethodnu = true;
        else
            prikaziPrethodnu = false;
        if(doBroja < listaSvihKorisnik.size())
            prikaziSljedecu = true;
        else
            prikaziSljedecu = false;
    }
}
