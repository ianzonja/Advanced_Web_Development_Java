/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.servisi;

import java.util.ArrayList;
import java.util.List;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.derby.client.am.DateTime;
import org.foi.aplikacija_1.klijenti.OWMKlijent;
import org.foi.aplikacija_1.podaci.MeteoPodaci;
import org.foi.aplikacija_1.podaci.Parkiraliste;
import org.foi.aplikacija_1.slusaci.SlusacAplikacije;
import org.foi.aplikacija_1.utils.RadSaBazom;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author Ian
 */
@WebService(serviceName = "Meteo")
public class Meteo {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "zadnjePreuzetiPodaci")
    public MeteoPodaci zadnjePreuzetiPodaci(@WebParam(name = "naziv") String naziv) {
        BP_Konfiguracija konfig = SlusacAplikacije.bpk;
        String upit = "Select * from parkiralista";
        List<Parkiraliste> listaParkiralista = RadSaBazom.dohvatiParkiralista(upit, konfig);
        int id = 0;
        for(Parkiraliste p : listaParkiralista){
            if(p.getNaziv().equals(naziv)){
                id = p.getId();
            }
        }
        MeteoPodaci mp = null;
        if (id != 0) {
            String upitZaMeteo = "Select * from Meteo where id=" + id + " order by preuzeto desc limit 1";
            mp = RadSaBazom.preuzmiMeteoPodatkeZaParkiraliste(konfig, upitZaMeteo);
        }
        String zapis = "Primljen SOAP zahtjev: Zadnje preuzeti podaci za odabrano parkiraliste";
        RadSaBazom.upisiUDnevnik(zapis, konfig);

        return mp;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "posljednjihNMeteoPodataka")
    public List<MeteoPodaci> posljednjihNMeteoPodataka(@WebParam(name = "naziv") String naziv, @WebParam(name="n") int n) {
        BP_Konfiguracija konfig = SlusacAplikacije.bpk;
        String upit = "Select * from parkiralista";
        List<Parkiraliste> listaParkiralista = RadSaBazom.dohvatiParkiralista(upit, konfig);
        int id = 0;
        for (Parkiraliste p : listaParkiralista) {
            if (p.getNaziv().equals(naziv)) {
                id = p.getId();
            }
        }
        List<MeteoPodaci> listaMeteoPodataka = new ArrayList<>();
        if (id != 0) {
            String upitzaBazu = "Select * from Meteo where id=" + id + " order by preuzeto desc limit " + n;
            BP_Konfiguracija bpk = SlusacAplikacije.bpk;
            listaMeteoPodataka = RadSaBazom.preuzmiNMeteoPodataka(bpk, upitzaBazu);
        }
        String zapis = "Primljen SOAP zahtjev: zadnjih n preuzetih meteo podataka za dato parkiraliste";
        RadSaBazom.upisiUDnevnik(zapis, konfig);

        return listaMeteoPodataka;
    }

    /**
     * Web service operation
     * @param naziv
     * @param od
     */
    @WebMethod(operationName = "podaciUIntervalu")
    public List<MeteoPodaci> podaciUIntervalu(@WebParam(name="naziv") String naziv, @WebParam(name = "od") String od, @WebParam(name = "do") String doDatuma) {
        System.out.println(naziv);
        System.out.println(od);
        System.out.println(doDatuma);
        BP_Konfiguracija konfig = SlusacAplikacije.bpk;
        String upit = "Select * from parkiralista";
        List<Parkiraliste> listaParkiralista = RadSaBazom.dohvatiParkiralista(upit, konfig);
        int id = 0;
        for (Parkiraliste p : listaParkiralista) {
            if (p.getNaziv().equals(naziv)) {
                id = p.getId();
            }
        }
        System.out.println("ID: " +id);
        List<MeteoPodaci> listaMeteoPodataka = new ArrayList<>();
        if (id != 0) {
            String upitzaBazu = "Select * from Meteo where id=" + id + " and preuzeto > '"+od+"' and preuzeto < '"+doDatuma+"'";
            System.out.println(upitzaBazu);
            BP_Konfiguracija bpk = SlusacAplikacije.bpk;
            listaMeteoPodataka = RadSaBazom.preuzmiNMeteoPodataka(bpk, upitzaBazu);
        }
        
        String zapis = "Primljen SOAP zahtjev: preuzeti podaci meteo podataka u intervalu";
        RadSaBazom.upisiUDnevnik(zapis, konfig);

        return listaMeteoPodataka;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "vezeciPodaciZaParkiraliste")
    public MeteoPodaci vezeciPodaciZaParkiraliste(@WebParam(name="naziv") String naziv) {
        BP_Konfiguracija konfig = SlusacAplikacije.bpk;
        String upit = "Select * from parkiralista";
        List<Parkiraliste> listaParkiralista = RadSaBazom.dohvatiParkiralista(upit, konfig);
        String latitude = "";
        String longitude = "";
        for (Parkiraliste p : listaParkiralista) {
            if (p.getNaziv().equals(naziv)) {
                latitude = p.getGeoloc().getLatitude();
                longitude = p.getGeoloc().getLongitude();
            }
        }
        MeteoPodaci mp = null;
        if (!latitude.equals("") && !longitude.equals("")) {
            OWMKlijent owm = new OWMKlijent("2cc311aa54c97e9621ae88e123bbc67d");
            mp = owm.getRealTimeWeather(latitude, longitude);
        }
        
        String zapis = "Primljen SOAP zahtjev: vazeci podaci za dato parkiraliste.";
        RadSaBazom.upisiUDnevnik(zapis, konfig);

        return mp;
    }
    
}
