/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.ianzonja.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.foi.ianzonja.web.rest.klijenti.KorisniciRestKlijent;
import org.foi.ianzonja.web.rest.klijenti.KorisnikRestKlijent;

/**
 *
 * @author Ian
 */
@Named(value = "autentikacijaZrno")
@SessionScoped
public class AutentikacijaZrno implements Serializable {
    private KorisnikRestKlijent korisnikRest;
    private KorisniciRestKlijent korisniciRest;
    private String korime;
    private boolean prikaziPoruku = false;
    private String rezultat;

    public String getRezultat() {
        return rezultat;
    }

    public void setRezultat(String rezultat) {
        this.rezultat = rezultat;
    }

    public boolean isPrikaziPoruku() {
        return prikaziPoruku;
    }

    public void setPrikaziPoruku(boolean prikaziPoruku) {
        this.prikaziPoruku = prikaziPoruku;
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
    private String lozinka;

    /**
     * Creates a new instance of autentikacijaZrno
     */
    public AutentikacijaZrno() {
    }
    
    public void izvrsiAutentikaciju(){
        korisniciRest = new KorisniciRestKlijent();
        String json = "{\"korime\": \""+korime+"\", \"lozinka\": \""+lozinka+"\"}";
        String odgovor = korisniciRest.putJson(json);
        rezultat = odgovor;
        if (odgovor.contains("OK")) {
            String url = "http://localhost:8080/ianzonja_aplikacija_2-war/faces/pocetna_prijavljen.xhtml";
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            try {
                ec.redirect(url);
            } catch (IOException ex) {
                Logger.getLogger(AutentikacijaZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
           prikaziPoruku = true;
           rezultat = odgovor;
        }
//        System.out.println("Odgovor: " + odgovor);
    }
}
