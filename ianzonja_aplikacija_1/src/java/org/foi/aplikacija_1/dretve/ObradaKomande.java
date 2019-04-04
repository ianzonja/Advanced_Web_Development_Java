/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.dretve;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.aplikacija_1.podaci.Korisnik;
import org.foi.aplikacija_1.servisi.Parkiranje;
import org.foi.aplikacija_1.servisi_client.StatusKorisnika;
import org.foi.aplikacija_1.slusaci.SlusacAplikacije;
import org.foi.aplikacija_1.utils.RadSaBazom;
import org.foi.nwtis.ianzonja.konfiguracije.Konfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author Ian
 */
public class ObradaKomande extends Thread{
    private Konfiguracija konfiguracija;
    private BP_Konfiguracija konfig;
    private StringBuffer sb;
    private String odgovor;
    private String korisnik;
    private String lozinka;
    private String korime;
    private String novoKorIme;
    private String novaLozinka;
    private String ime;
    private String prezime;
    private String json;
    private boolean prekiniRadZaOstaleKomande;
    private boolean stanjeAktivno;
    private boolean stanjePasivno;

    public String getOdgovor() {
        return odgovor;
    }

    ObradaKomande(Konfiguracija konfiguracija, BP_Konfiguracija konfig, StringBuffer sb) {
        this.konfiguracija = konfiguracija;
        this.konfig = konfig;
        this.sb = sb;
    }
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        System.out.println("zahtjev: " + sb.toString());
        odgovor = obradiZahtjev(sb);
    }

    @Override
    public synchronized void start() {
        super.start();
    }
    
    private String obradiZahtjev(StringBuffer sb) {
        korisnik = "";
        lozinka = "";
        ime = "";
        prezime = "";
        json = "";
        novoKorIme = "";
        novaLozinka = "";
        String komanda = sb.toString();
        if(komanda.contains("GRUPA")){
            if(prekiniRadZaOstaleKomande)
                return "Obrada nemoguca, posluzitelj u stanju pauze";
            else{
                if(komanda.contains("DODAJ")){
                    return provjeriKomanduGrupeDodaj(komanda);
                }
                else if(komanda.contains("PREKID")){
                    return provjeriKomanduGrupePrekid(komanda);
                }
                else if(komanda.contains("KRENI")){
                    return provjeriKomanduGrupeKreni(komanda);
                }
                else if(komanda.contains("PAUZA")){
                    return provjeriKomanduGrupePauza(komanda);
                }
                else if(komanda.contains("STANJE")){
                    return provjeriKomanduGrupeStanje(komanda);
                }
            }
        }
        if(komanda.contains("PAUZA"))
           return provjeriKomanduPauze(komanda);
        else if(komanda.contains("KRENI"))
            return provjeriKomanduKreni(komanda);
        else if(komanda.contains("PASIVNO"))
            return provjeriKomanduPasivno(komanda);
        else if(komanda.contains("AKTIVNO"))
            return provjeriKomanduAktivno(komanda);
        else if(komanda.contains("AZURIRAJ"))
            return provjeriKomanduAzuriraj(komanda);
        else if(komanda.contains("DODAJ")){
            return provjeriKomanduDodaj(komanda);
        }
        else if(komanda.contains("LISTAJ"))
            return provjeriKomanduListaj(komanda);
        else if(komanda.contains("STANI"))
            return provjeriKomanduStani(komanda);
        else if(komanda.contains("STANJE"))
            return provjeriKomanduStanje(komanda);
        else
            return provjeriKomanduAutentikacije(komanda);
    }

    private String provjeriKomanduPauze(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if (words.size() == 5) {
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length() - 1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length() - 1);
            if (autenticirajKorisnika()) {
                String zapis = "Korisnik "+korisnik+" šalje zahtjev PAUZA"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                System.out.println("korisnik autenticiran");
                if (prekiniRadZaOstaleKomande) {
                    return "ERR 12; Posluzitelj u stanju pauze";
                } else {
                    prekiniRadZaOstaleKomande = true;//implementirati kasnije logiku
                    return "OK 10; Posluzitelj postavljen u stanje pauze";
                }
            } else {
                return "ERROR; Korisnik nije autenticiran!";
            }
        } else {
            return "ERROR; Komanda krivo postavljena!";
        }
    }
    
    private boolean autenticirajKorisnika(){
        System.out.println("korisnik trenutno: " + korisnik);
        System.out.println("lozinka trenutno: " + lozinka);
        List<Korisnik> listaKorisnika = RadSaBazom.dohvatiKorisnike(konfiguracija, konfig);
        System.out.println("velicina liste korisnija: " + listaKorisnika.size());
        boolean postoji = false;
        for(Korisnik k : listaKorisnika){
            System.out.println("korisnicko ime iz baze: " + k.getKorime());
            System.out.println("korisnicko ime sa socketa: " + korisnik);
            System.out.println("lozinka sa socketa: "+ lozinka);
            System.out.println("lozinka iz baze: " + k.getKorime());
            if(k.getKorime().equals(korisnik) && k.getLozinka().equals(lozinka))
                postoji = true;
        }
        if(postoji)
            return true;
        else
            return false;
    }

    private String provjeriKomanduKreni(String komanda) {
//        if(komanda.contains("\\?")){
//            System.out.print("sadrzim upitnik");
//            komanda = komanda.replace("\\?", "");
//        }
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if (words.size() == 5) {
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if (autenticirajKorisnika()) {
                System.out.println("korisnik autenticiran");
                if (!prekiniRadZaOstaleKomande) {
                    return "ERR 13; Server u stanju kreni";
                } else {
                    prekiniRadZaOstaleKomande = false;//implementirati kasnije logiku
                    return "OK 10; Server postavljen u stanje kreni";
                }
            }else{
                return "ERROR; Korisnik nije autenticiran!";
            }
        }else{
            return "ERROR; Komanda krivo postavljena!";
        }
    }

    private String provjeriKomanduPasivno(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 5){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev PASIVNO"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                if(!stanjeAktivno)
                      return "ERR 14; Server u stanju pasivno";
                  else
                  {
                      stanjeAktivno = false; //implementirati kasnije logiku
                      return "OK 10; Server postavljen u stanje pasivno";
                  }
            }
            else{
                return "ERROR; Korisnik nije autenticiran!";
            }
        }else
            return "ERROR; Neispravna komanda!";
    }

    private String provjeriKomanduAktivno(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 5){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length() - 1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length() - 1);
            if (autenticirajKorisnika()) {
                String zapis = "Korisnik " + korisnik + " šalje zahtjev AKTIVNO";
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                if (stanjeAktivno) {
                    return "ERR 15; Server u stanju aktivno";
                } else {
                    stanjeAktivno = true; //implementirati kasnije logiku
                    return "OK 10; Server postavljen u stanje aktivno";
                }
            }else
                return "ERR 15; Korisnik nije autenticiran";
        }else
            return "ERR 15; Neispravna komanda";
    }

    private String provjeriKomanduAzuriraj(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        korisnik = words.get(1);
        korisnik = korisnik.substring(0, korisnik.length() - 1);
        lozinka = words.get(3);
        lozinka = lozinka.substring(0, lozinka.length() - 1);
        if (autenticirajKorisnika()) {
            String zapis = "Korisnik " + korisnik + " šalje zahtjev AZURIRAJ";
            RadSaBazom.upisiUDnevnik(zapis, konfig);
            if (words.size() == 9) {
                return izvrsiCijeloAzuriranje(words);
            } else if (words.size() == 7) {
                return izvrsiPolovicnoAzuriranje(words);
            } else {
                return "ERR 10; Pogrešno složena komanda";
            }
        }else
            return "ERR 10; Korisnik ne postoji";
    }
    
    private String izvrsiCijeloAzuriranje(List<String> words) {
        korisnik = words.get(1);
        korisnik = korisnik.substring(0, korisnik.length() - 1);
        lozinka = words.get(3);
        lozinka = lozinka.substring(0, lozinka.length() - 1);
        if (autenticirajKorisnika()) {
            prezime = words.get(5);
            prezime = prezime.substring(1, prezime.length() - 1);
            ime = words.get(6);
            ime = ime.substring(1, ime.length() - 1);
            novaLozinka = words.get(7);
            novaLozinka = novaLozinka.substring(1, novaLozinka.length() - 1);
            novoKorIme = words.get(8);
            novoKorIme = novoKorIme.substring(1, novoKorIme.length() - 3);
            if(RadSaBazom.azurirajKorisnika(konfiguracija, konfig, ime, prezime, korisnik, novoKorIme, novaLozinka))
                return "OK 10; Korisnik azuriran";
            else
                return "ERR 10; Korisnik nije azuriran";
        }else{
            return "ERROR; Korisnik nije autenticiran";
        }
    }

    private String provjeriKomanduDodaj(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 7){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length() - 1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length() - 1);
            korisnik = korisnik.replace("\"", "");
            lozinka = lozinka.replace("\"", "");
            
            
            prezime = words.get(5);
            prezime = prezime.substring(1, prezime.length() - 1);
            ime = words.get(6);
            ime = ime.substring(1, ime.length() - 3);
            prezime = prezime.replace("\"", "");
            ime = ime.replace("\"", "");
            if(!RadSaBazom.postojiKorisnikUBazi(konfiguracija, konfig, korisnik)){
                RadSaBazom.unesiPodatkeUBazu(konfiguracija, konfig, korisnik, lozinka, ime, prezime);
                return "OK 10;";
            }else{
                return "ER 10; Korisnik vec postoji u bazi";
            }
        }
        else{
            return "ERROR; Neispravna komanda!";
        }
    }

    private String provjeriKomanduGrupeDodaj(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 6){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev GRUPA DODAJ"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                Parkiranje p = new Parkiranje();
                if(p.registrirajGrupu(korisnik, lozinka))
                    return "OK 20;";
                else
                    return "ERROR; Grupa nije registrirana!";
            }else{
                return "ER 11; Korisnik ne postoji ili ne odgovara lozinka!";
            }
        }
        else{
            return "ERROR; Neispravna komanda!";
        }
    }

    private String provjeriKomanduGrupePrekid(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 6){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev GRUPA PREKID"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                Parkiranje p = new Parkiranje();
                if(p.deregistrirajGrupu(korisnik, lozinka))
                    return "OK 20;";
                else
                    return "ERROR; Grupa nije bila registrirana!";
            }else{
                return "ER 11; Korisnik ne postoji ili ne odgovara lozinka!";
            }
        }
        else{
            return "ERROR; Neispravna komanda!";
        }
    }

    private String provjeriKomanduGrupeKreni(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 6){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev GRUPA KRENI"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                Parkiranje p = new Parkiranje();
                String status = String.valueOf(p.dajStatusGrupe(korisnik, lozinka));
                if(status.equals("NEPOSTOJI"))
                {
                    return "ERR 21; Grupa ne postoji";
                }
                else if(status.equals("AKTIVAN"))
                    return "ERR 22; Grupa je aktivna";
                else if(p.aktivirajGrupu(korisnik, lozinka))
                    return "OK 10;";
                else
                    return "ERR 23; Grupa nije aktivirana";
            }else{
                return "ER 11; Korisnik ne postoji ili ne odgovara lozinka!";
            }
        }
        else{
            return "ERROR; Neispravna komanda!";
        }
    }

    private String provjeriKomanduGrupePauza(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 6){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev GRUPA PAUZA"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                Parkiranje p = new Parkiranje();
                String status = String.valueOf(p.dajStatusGrupe(korisnik, lozinka));
                if(status.equals("NEPOSTOJI"))
                {
                    return "ERR 21; Grupa ne postoji";
                }
                else if(status.equals("AKTIVAN")){
                    if(p.blokirajGrupu(korisnik, lozinka))
                        return "OK 20; Grupa blokirana.";
                }
                else if(status.equals("BLOKIRAN"))
                    return "ERR 21; Grupa je bila blokirana";
                else
                    return "ERR 22; Grupa nije bila aktivna";
                
            }else{
                return "ER 11; Korisnik ne postoji ili ne odgovara lozinka!";
            }
        }
        else{
            return "ERROR; Neispravna komanda!";
        }
        return "";
    }

    private String provjeriKomanduGrupeStanje(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        System.out.println("broj rici: " + words.size());
        if(words.size() == 6){
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length()-1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev GRUPA STANJE"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika stanje = Parkiranje.dajStatusGrupe(korisnik, lozinka);
                System.out.println("Korisnik: " + korisnik);
                System.out.println("Lozinka: " + lozinka);
                System.out.println("stanje: " + stanje.toString());
                System.out.println("stanje: " + stanje.value().toString());
                if(stanje.value().equals("AKTIVAN"))
                    return "OK 21 Grupa aktivna;";
                else if(stanje.value().equals("BLOKIRAN"))
                    return "OK 22; Grupa blokirana";
                else if(stanje.value().equals("DEREGISTRIRAN"))
                    return "OK 23; Grupa deregistrirana";
                else if(stanje.value().equals("REGISTRIRAN"))
                    return "OK 24; Grupa registrirana";
                else if(stanje.value().equals("NEPOSTOJI")){
                    return "ERR 21;";
                }
            }else{
                return "ER 11; Korisnik ne postoji ili ne odgovara lozinka!";
            }
        }else{
            return "ERROR; Neispravna komanda!";
        }
        return "";
    }

    private String provjeriKomanduListaj(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if (words.size() == 5) {
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length()-1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length() - 1);
            if (autenticirajKorisnika()) {
                String zapis = "Korisnik "+korisnik+" šalje zahtjev LISTAJ."; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                List<Korisnik> listaKorisnika = RadSaBazom.dohvatiKorisnike(konfiguracija, konfig);
                if(listaKorisnika != null)
                    if(listaKorisnika.size() > 0){
                        String odgovor = "";
                        odgovor = "OK 10; [";
                        for(int i = 0; i<listaKorisnika.size(); i++){
                            odgovor = odgovor+"{\"ki\": \""+listaKorisnika.get(i).getKorime()+"\", \"prezime\": \""+listaKorisnika.get(i).getPrezime()+"\", \"ime\": \""+listaKorisnika.get(i).getIme()+"\"}";
                            if(i != listaKorisnika.size()-1)
                                odgovor = odgovor + ",";
                        }
                        odgovor = odgovor+"];";
                        return odgovor;
                    }
                    else{
                        return "ERR 17; Nema korisnika u bazi";
                    }
           }else{
                return "ERROR; Korisnik nije autenticiran!";
            }
        } else {
            return "ERROR; Komanda krivo postavljena!";
        }
        return "";
    }

    private String izvrsiPolovicnoAzuriranje(List<String> words) {
        korisnik = words.get(1);
        korisnik = korisnik.substring(0, korisnik.length() - 1);
        lozinka = words.get(3);
        lozinka = lozinka.substring(0, lozinka.length() - 1);
        if (autenticirajKorisnika()) {
            prezime = words.get(5);
            prezime = prezime.substring(1, prezime.length() - 1);
            ime = words.get(6);
            ime = ime.substring(1, ime.length() - 1);
            if(RadSaBazom.azurirajKorisnika(konfiguracija, konfig, ime, prezime, korisnik))
                return "OK 10; Korisnik azuriran";
            else
                return "ERR 10; Korisnik nije azuriran";
        }
        else return "ERROR; Korisnik nije autenticiran";
    }

    private String provjeriKomanduAutentikacije(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 4)
        {
            korisnik = words.get(1);
            korisnik = korisnik.substring(1, korisnik.length()-2);
            lozinka = words.get(3);
            lozinka = lozinka.substring(1, lozinka.length()-3);
            if(autenticirajKorisnika())
            {
                String zapis = "Korisnik "+korisnik+" se autenticirao te nije poslao nikakav daljnji zahtjev."; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                return "OK 10; Korisnik autenticiran";
            }
            else
                return "ERR 11; Korisnik nije autenticiran";
        }
        else
            return "ERROR; Sintaksa komande neispravna";
    }

    private String provjeriKomanduStani(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if (words.size() == 4) {
            korisnik = words.get(1);
            korisnik = korisnik.substring(0, korisnik.length() - 1);
            lozinka = words.get(3);
            lozinka = lozinka.substring(0, lozinka.length() - 1);
            if(autenticirajKorisnika()){
                String zapis = "Korisnik "+korisnik+" šalje zahtjev STANI"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
            }
        }
        return "";
    }

    private String provjeriKomanduStanje(String komanda) {
        Scanner s = new Scanner(komanda);
        List<String> words = new ArrayList<>();
        while (s.hasNext()) {
            String word = s.next();
            words.add(word);
        }
        if(words.size() == 4)
        {
            korisnik = words.get(1);
            korisnik = korisnik.substring(1, korisnik.length()-2);
            lozinka = words.get(3);
            lozinka = lozinka.substring(1, lozinka.length()-3);
            if(autenticirajKorisnika())
            {
                String zapis = "Korisnik "+korisnik+" je poslao zahtjev STANJE"; 
                RadSaBazom.upisiUDnevnik(zapis, konfig);
                return "OK 11; Implement do kraja plz";
            }
            else
                return "ERR 11; Korisnik nije autenticiran";
        }
        else
            return "ERROR; Sintaksa komande neispravna";
    }
}
