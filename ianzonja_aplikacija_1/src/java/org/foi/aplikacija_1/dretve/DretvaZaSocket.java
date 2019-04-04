package org.foi.aplikacija_1.dretve;


import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import org.foi.aplikacija_1.podaci.Korisnik;
import org.foi.aplikacija_1.servisi.Parkiranje;
import org.foi.aplikacija_1.slusaci.SlusacAplikacije;
import org.foi.aplikacija_1.utils.RadSaBazom;
import org.foi.nwtis.ianzonja.konfiguracije.Konfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ianzonja.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian
 */
public class DretvaZaSocket extends Thread{
    private String korisnik;
    private String lozinka;
    private BP_Konfiguracija konfig;
    private Konfiguracija konfiguracija;
    private ServletContext sc;
    private String ime;
    private String prezime;
    private String json;
    private String korime;
    private String novoKorIme;
    private String novaLozinka;
    private static boolean prekiniRadZaOstaleKomande = false;
    private AtomicBoolean iskljuciDretvuZaSocket;

    public static boolean isPrekiniRadZaOstaleKomande() {
        return prekiniRadZaOstaleKomande;
    }

    public static void setPrekiniRadZaOstaleKomande(boolean prekiniRadZaOstaleKomande) {
        DretvaZaSocket.prekiniRadZaOstaleKomande = prekiniRadZaOstaleKomande;
    }

    public static boolean isStanjeAktivno() {
        return stanjeAktivno;
    }

    public static void setStanjeAktivno(boolean stanjeAktivno) {
        DretvaZaSocket.stanjeAktivno = stanjeAktivno;
    }
    private static boolean stanjeAktivno = true;
    
    public DretvaZaSocket(ServletContext sc) {
        this.sc = sc;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {

        try {
            dohvatiKonfiguraciju();
            ServerSocket serverSocket;
            int port = Integer.valueOf(konfiguracija.dajPostavku("socket.port"));
            serverSocket = new ServerSocket(port, 100);
            while (!this.isInterrupted()) {
                Socket socket = serverSocket.accept();
                long pocetnoVrijeme = new Date().getTime();
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                StringBuffer sb = new StringBuffer();
                int bajt = 0;
                while (bajt != -1) {
                    bajt = is.read();
                    sb.append((char) bajt);
                }
                ObradaKomande obradaKomande = new ObradaKomande(konfiguracija, konfig, sb);
                obradaKomande.start();
                try {
                    obradaKomande.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(DretvaZaSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
                String odgovor = obradaKomande.getOdgovor();
                os.write(odgovor.getBytes());
                os.flush();
                socket.shutdownOutput();
                long zavrsnoVrijeme = new Date().getTime();
                long razlika = zavrsnoVrijeme - pocetnoVrijeme;
            }
        } catch (IOException ex) {
            Logger.getLogger(DretvaZaSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void dohvatiKonfiguraciju() {
        String datoteka = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF") + java.io.File.separator;
        try {
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja + datoteka);
            konfig = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(DretvaZaSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
}
