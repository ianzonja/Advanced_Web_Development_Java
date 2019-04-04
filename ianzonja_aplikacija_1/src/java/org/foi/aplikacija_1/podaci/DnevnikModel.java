/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.podaci;

import java.time.LocalDateTime;

/**
 *
 * @author Ian
 */
public class DnevnikModel {
    private int id;
    private String sadrzaj;
    private LocalDateTime vrijeme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public LocalDateTime getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(LocalDateTime vrijeme) {
        this.vrijeme = vrijeme;
    }
}
