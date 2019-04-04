/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.dretve;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.aplikacija_1.klijenti.OWMKlijent;
import org.foi.aplikacija_1.podaci.MeteoPodaci;
import org.foi.aplikacija_1.podaci.Parkiraliste;
import org.foi.aplikacija_1.slusaci.SlusacAplikacije;
import org.foi.aplikacija_1.utils.RadSaBazom;
import org.foi.nwtis.ianzonja.konfiguracije.Konfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ianzonja.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;



/**
 *
 * @author Ian
 */
public class DohvatMeteoPodataka extends Thread{
    
    private Konfiguracija konfiguracija;
    private int interval;
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        while(!this.isInterrupted()){
            System.out.println("nalazim se u beskonacnoj");
            String komanda = "Select * from parkiralista";
            List<Parkiraliste> listaParkiralista = new ArrayList<>();
            BP_Konfiguracija konfig = SlusacAplikacije.bpk;
            listaParkiralista = RadSaBazom.dohvatiParkiralista(komanda, konfig);
            String datoteka = SlusacAplikacije.sc.getInitParameter("konfiguracija");
            String putanja = SlusacAplikacije.sc.getRealPath("/WEB-INF") + java.io.File.separator;
            for (int i = 0; i < listaParkiralista.size(); i++) {
                OWMKlijent owm = new OWMKlijent(konfiguracija.dajPostavku("apikey"));
                MeteoPodaci mp = owm.getRealTimeWeather(listaParkiralista.get(i).getGeoloc().getLatitude(), listaParkiralista.get(i).getGeoloc().getLongitude());
                String naredba = "Insert into meteo values (default, " + listaParkiralista.get(i).getId() + ", '" + listaParkiralista.get(i).getAdresa() + "', " + listaParkiralista.get(i).getGeoloc().getLatitude() + ", " + listaParkiralista.get(i).getGeoloc().getLongitude() + ", '" + mp.getWeatherValue() + "', '" + mp.getWeatherIcon() + "', " + mp.getTemperatureValue() + ", " + mp.getTemperatureMin() + ", " + mp.getTemperatureMax() + ", " + mp.getHumidityValue() + ", " + mp.getPressureValue() + ", " + mp.getWindSpeedValue() + ", " + mp.getWindDirectionValue() + ", '" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "')";
                System.out.println("ovo bi trebalo uc u bazu: " + naredba);
                RadSaBazom.unesiMeteoPodatke(konfig, naredba, konfiguracija);
            }
            interval = Integer.parseInt(konfiguracija.dajPostavku("intervalDretveZaMeteoPodatke")) * 1000;
            System.out.println("interval: " + interval);
            try {
                sleep(interval);
            } catch (InterruptedException ex) {
                Logger.getLogger(DohvatMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
        String datoteka = SlusacAplikacije.sc.getInitParameter("konfiguracija");
        String putanja = SlusacAplikacije.sc.getRealPath("/WEB-INF") + java.io.File.separator;
        try {
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja + datoteka);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(DretvaZaSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
