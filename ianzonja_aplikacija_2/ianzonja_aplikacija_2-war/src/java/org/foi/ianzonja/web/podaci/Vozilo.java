/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.ianzonja.web.podaci;

/**
 *
 * @author Ian
 */
public class Vozilo {
    private String akcija;

    public String getAkcija() {
        return akcija;
    }

    public void setAkcija(String akcija) {
        this.akcija = akcija;
    }

    public String getParkiraliste() {
        return parkiraliste;
    }

    public void setParkiraliste(String parkiraliste) {
        this.parkiraliste = parkiraliste;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }
    private String parkiraliste;
    private String registracija;
}
