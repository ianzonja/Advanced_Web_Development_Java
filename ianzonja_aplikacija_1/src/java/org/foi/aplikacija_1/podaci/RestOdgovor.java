/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.podaci;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ian
 */
public class RestOdgovor {
    private List<Parkiraliste> listaPodataka = new ArrayList<>();

    public List<Parkiraliste> getListaPodataka() {
        return listaPodataka;
    }

    public void setListaPodataka(List<Parkiraliste> listaPodataka) {
        this.listaPodataka = listaPodataka;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
    private String status;
    private String poruka;
}
