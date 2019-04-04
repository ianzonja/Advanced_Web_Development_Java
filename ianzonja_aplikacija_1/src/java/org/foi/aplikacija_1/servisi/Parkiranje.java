/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.servisi;

import javax.jws.WebService;
import org.foi.aplikacija_1.servisi_client.StatusKorisnika;

/**
 *
 * @author Ian
 */
@WebService(serviceName = "Parkiranje", portName = "ParkiranjePort", endpointInterface = "org.foi.nwtis.dkermek.ws.serveri.Parkiranje", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", wsdlLocation = "WEB-INF/wsdl/Parkiranje/nwtis.foi.hr_8080/NWTiS_2018/Parkiranje.wsdl")
public class Parkiranje {

    public int dajBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void promjeniBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int brojPoruka) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int dajTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void promjeniTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int trajanjeCiklusa) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.Boolean dodajParkiralisteGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.dkermek.ws.serveri.Parkiraliste idParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.Boolean autenticirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.Boolean dodajNovoParkiralisteGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste, java.lang.String nazivParkiraliste, java.lang.String adresaParkiraliste, int kapacitetParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.Boolean obrisiSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean ucitajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.foi.nwtis.dkermek.ws.serveri.StatusParkiralista dajStatusParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean blokirajParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean aktivirajParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean obrisiParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean aktivirajOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean blokirajOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

//    public java.util.List<org.foi.nwtis.dkermek.ws.serveri.Parkiraliste> dajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
//        //TODO implement this method
//        throw new UnsupportedOperationException("Not implemented yet.");
//    }

    public java.util.List<org.foi.nwtis.dkermek.ws.serveri.Vozilo> dajSvaVozilaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean obrisiOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public static Boolean deregistrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.aplikacija_1.servisi_client.Parkiranje_Service service = new org.foi.aplikacija_1.servisi_client.Parkiranje_Service();
        org.foi.aplikacija_1.servisi_client.Parkiranje port = service.getParkiranjePort();
        return port.deregistrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean blokirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.aplikacija_1.servisi_client.Parkiranje_Service service = new org.foi.aplikacija_1.servisi_client.Parkiranje_Service();
        org.foi.aplikacija_1.servisi_client.Parkiranje port = service.getParkiranjePort();
        return port.blokirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean aktivirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.aplikacija_1.servisi_client.Parkiranje_Service service = new org.foi.aplikacija_1.servisi_client.Parkiranje_Service();
        org.foi.aplikacija_1.servisi_client.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
    }

//    public static StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
//        org.foi.aplikacija_1.servisi_client.Parkiranje_Service service = new org.foi.aplikacija_1.servisi_client.Parkiranje_Service();
//        org.foi.aplikacija_1.servisi_client.Parkiranje port = service.getParkiranjePort();
//        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
//    }
    
    public static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka){
        org.foi.nwtis.dkermek.ws.serveri.Parkiranje_Service service = new org.foi.nwtis.dkermek.ws.serveri.Parkiranje_Service();
        org.foi.nwtis.dkermek.ws.serveri.Parkiranje port = service.getParkiranjePort();
        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
    }
    

    public static Boolean registrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.aplikacija_1.servisi_client.Parkiranje_Service service = new org.foi.aplikacija_1.servisi_client.Parkiranje_Service();
        org.foi.aplikacija_1.servisi_client.Parkiranje port = service.getParkiranjePort();
        return port.registrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static java.util.List<org.foi.aplikacija_1.servisi_client.Parkiraliste> dajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.aplikacija_1.servisi_client.Parkiranje_Service service = new org.foi.aplikacija_1.servisi_client.Parkiranje_Service();
        org.foi.aplikacija_1.servisi_client.Parkiranje port = service.getParkiranjePort();
        return port.dajSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }
    
}
