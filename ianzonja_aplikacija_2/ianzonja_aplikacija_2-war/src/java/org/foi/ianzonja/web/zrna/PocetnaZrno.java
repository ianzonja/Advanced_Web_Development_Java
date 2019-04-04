/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.ianzonja.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Ian
 */
@Named(value = "pocetnaZrno")
@SessionScoped
public class PocetnaZrno implements Serializable {

    /**
     * Creates a new instance of PocetnaZrno
     */
    public PocetnaZrno() {
    }
    
}
