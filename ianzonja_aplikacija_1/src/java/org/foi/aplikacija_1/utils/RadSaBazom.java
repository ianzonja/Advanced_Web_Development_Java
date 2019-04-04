/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_1.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import jdk.nashorn.internal.runtime.ListAdapter;
import org.foi.aplikacija_1.podaci.DnevnikModel;
import org.foi.aplikacija_1.podaci.Korisnik;
import org.foi.aplikacija_1.podaci.Lokacija;
import org.foi.aplikacija_1.podaci.MeteoPodaci;
import org.foi.aplikacija_1.podaci.Parkiraliste;
import org.foi.aplikacija_1.servisi.ParkiralistaREST;
import org.foi.nwtis.ianzonja.konfiguracije.Konfiguracija;
import org.foi.nwtis.ianzonja.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author Ian
 */
public class RadSaBazom{
    
    public static List<Korisnik> dohvatiKorisnike(Konfiguracija konfiguracija, BP_Konfiguracija konfig){
        List<Korisnik> listaKorisnika = new ArrayList<>();
        String upit = "Select * from KORISNICI";
        System.out.println("Baza: " + konfig.getServerDatabase() + konfig.getUserDatabase());
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        System.out.println("username: " + korisnikBaze);
        System.out.println("password: " + lozinka);
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            System.out.println("spojio sam se");
            ResultSet rs = stm.executeQuery(upit);
            while (rs.next()) {
                System.out.println("ima next");
                Korisnik korisnik = new Korisnik();
                korisnik.setKorime(rs.getString("korime"));
                korisnik.setLozinka(rs.getString("LOZINKA"));
                korisnik.setIme(rs.getString("IME"));
                korisnik.setPrezime(rs.getString("PREZIME"));
                listaKorisnika.add(korisnik);
            }
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
        }
        System.out.println("lista korisnika:" + String.valueOf(listaKorisnika.size()));
        return listaKorisnika;
    }
    
    public static void unesiPodatkeUBazu(Konfiguracija konfiguracija, BP_Konfiguracija konfig, String korisnik, String lozinka, String ime, String prezime) {
        String upit = "Insert into KORISNICI values (default, '"+korisnik+"', '"+lozinka+"', '"+ime+"', '"+prezime+"')";
        System.out.println("upit: " + upit);
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinkaBaze = konfig.getAdminPassword();
        try (Connection con = (Connection) DriverManager.getConnection(url, korisnikBaze, lozinkaBaze);
                Statement stmt = (Statement) con.createStatement();
                ) {
                stmt.execute(upit);
                System.out.println("izvrsio upit");
                stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(RadSaBazom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean postojiKorisnikUBazi(Konfiguracija konfiguracija, BP_Konfiguracija konfig, String korisnickoIme){
        Korisnik korisnik = new Korisnik();
        String upit = "Select * from KORISNICI where korime='"+korisnickoIme+"'";
        System.out.println("Baza: " + konfig.getServerDatabase() + konfig.getUserDatabase());
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        boolean postoji = false;
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            System.out.println("spojio sam se");
            ResultSet rs = stm.executeQuery(upit);
            while (rs.next()) {
                postoji = true;
            }
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
        }
        return postoji;
    }
    
    public static boolean azurirajKorisnika(Konfiguracija konfiguracija, BP_Konfiguracija konfig, String ime, String prezime, String korime){
        String upit = "Update Korisnici set ";
        if(ime != null && !ime.equals("")){
            ime = ime.replace("\"", "");
            ime = ime.replace(";", "");
            upit = upit + "ime='"+ime+"'";
        }
            
        if(prezime != null && !prezime.equals(""))
            if(ime == null || ime.equals(""))
                upit = upit + "prezime='" + prezime + "'";
            else
                upit = upit + ", prezime='"+prezime+"'";
        upit = upit + " where korime='"+korime+"'";
        System.out.println("upit za azuriranje: " + upit);
        try {
            Class.forName(konfiguracija.dajPostavku("driver.database.derby"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            stm.executeUpdate(upit);
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
            return false;
        }
        return true;
    }
    
    public static boolean azurirajKorisnika(Konfiguracija konfiguracija, BP_Konfiguracija konfig, String ime, String prezime, String korime, String novoKorIme, String novaLozinka){
        String upit = "Update Korisnici set ";
        if(novoKorIme != null && !novoKorIme.equals("")){
            upit = upit + "korime='"+novoKorIme+"'";
        }
        if(prezime != null && !prezime.equals("")){
            if(novoKorIme == null || novoKorIme.equals("")){
                System.out.println(novoKorIme);
                upit = upit + "prezime='"+prezime+"'";
            }
            else
                upit = upit + ", prezime='"+prezime+"'";
        }
        if(ime != null && !ime.equals(""))
            if((novoKorIme == null || novoKorIme.equals("")) && (prezime == null || prezime.equals("")))
                upit = upit + "ime='"+ime+"'";
            else
                upit = upit + ", ime='" + ime + "'";
        if (novaLozinka != null && !novaLozinka.equals("")) {
            if ((novoKorIme == null || novoKorIme.equals("")) && (prezime == null || prezime.equals("")) && (ime == null || ime.equals(""))) {
                upit = upit + "lozinka='" + novaLozinka + "'";
            }
            else
                upit = upit + ", lozinka='" + novaLozinka + "'";
        }
        upit = upit + " where korime='"+korime+"'";
        System.out.println("FInalno upit:" + upit);
        try {
            Class.forName(konfiguracija.dajPostavku("driver.database.derby"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            stm.executeUpdate(upit);
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
            return false;
        }
        return true;
    }
    
    public static List<Parkiraliste> dohvatiParkiralista(String naredba, BP_Konfiguracija konfig){
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        List<Parkiraliste> listaParkiralista = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection con = (Connection) DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stmt = (Statement) con.createStatement();
                ) {
                ResultSet rs = stmt.executeQuery(naredba);
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String naziv = rs.getString("NAZIV");
                    String adresa = rs.getString("ADRESA");
                    String longitude = rs.getString("LONGITUDE");
                    String latitude = rs.getString("LATITUDE");
                    Parkiraliste p = new Parkiraliste(Integer.parseInt(id), naziv, adresa, new Lokacija(longitude, latitude));
                    listaParkiralista.add(p);
                };
                stmt.close();
                con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ParkiralistaREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaParkiralista;
    }
    
    public static boolean unesiParkiraliste(String upit, BP_Konfiguracija konfig){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = (Statement) connect.createStatement();) {
            stm.execute(upit);
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
            return false;
        }
        return true;
    }
    
    public static boolean azurirajParkiraliste(String upit, BP_Konfiguracija konfig){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze,  lozinka);
                Statement stm = connect.createStatement();) {
            stm.executeUpdate(upit);
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
            return false;
        }
        return true;
    }
    
    public static boolean obrisiParkiraliste(String upit, BP_Konfiguracija konfig){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        boolean obrisano;
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        try (
                Connection con = DriverManager.getConnection(url, korisnikBaze, lozinka);
                java.sql.Statement stmt = con.createStatement();) {
            obrisano = stmt.execute(upit);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex);
            return false;
        }
        return obrisano;
    }
    
    public static boolean unesiMeteoPodatke(BP_Konfiguracija bpk, String naredba, Konfiguracija konfiguracija){
         try {
            Class.forName(konfiguracija.dajPostavku("driver.database.mysql"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String korisnikBaze = bpk.getAdminUsername();
        String lozinkaBaze = bpk.getAdminPassword();
        try (Connection con = (Connection) DriverManager.getConnection(url, korisnikBaze, lozinkaBaze);
                Statement stmt = (Statement) con.createStatement();
                ) {
                stmt.execute(naredba);
                System.out.println("izvrsio upit");
                stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(RadSaBazom.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static MeteoPodaci preuzmiMeteoPodatkeZaParkiraliste(BP_Konfiguracija konfig, String naredba){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        System.out.println("naredba iz baze: " + naredba);
        System.out.println("username: " + korisnikBaze);
        System.out.println("password: " + lozinka);
        MeteoPodaci mp = new MeteoPodaci();
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            System.out.println("spojio sam se");
            ResultSet rs = stm.executeQuery(naredba);
            while (rs.next()) {
                System.out.println("ima next");
                mp.setWeatherValue(rs.getString("VRIJEME"));
                mp.setWeatherIcon(rs.getString("VRIJEMEOPIS"));
                mp.setTemperatureValue(rs.getFloat("TEMP"));
                mp.setTemperatureMin(rs.getFloat("TEMPMIN"));
                mp.setTemperatureMax(rs.getFloat("TEMPMAX"));
                mp.setHumidityValue(rs.getFloat("VLAGA"));
                mp.setPressureValue(rs.getFloat("TLAK"));
                mp.setWindSpeedValue(rs.getFloat("VJETAR"));
                mp.setWindDirectionValue(rs.getFloat("VJETARSMJER"));
            }
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
        }
        return mp;
    }
    
    public static List<MeteoPodaci> preuzmiNMeteoPodataka(BP_Konfiguracija konfig, String naredba){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        System.out.println("iz baze: " + naredba);
        System.out.println("username: " + korisnikBaze);
        System.out.println("password: " + lozinka);
        List<MeteoPodaci> lista = new ArrayList<>();
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            System.out.println("spojio sam se");
            ResultSet rs = stm.executeQuery(naredba);
            while (rs.next()) {
                System.out.println("ima next");
                MeteoPodaci mp = new MeteoPodaci();
                mp.setWeatherValue(rs.getString("VRIJEME"));
                mp.setWeatherIcon(rs.getString("VRIJEMEOPIS"));
                mp.setTemperatureValue(rs.getFloat("TEMP"));
                mp.setTemperatureMin(rs.getFloat("TEMPMIN"));
                mp.setTemperatureMax(rs.getFloat("TEMPMAX"));
                mp.setHumidityValue(rs.getFloat("VLAGA"));
                mp.setPressureValue(rs.getFloat("TLAK"));
                mp.setWindSpeedValue(rs.getFloat("VJETAR"));
                mp.setWindDirectionValue(rs.getFloat("VJETARSMJER"));
                lista.add(mp);
            }
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
        }
        return lista;
    }

    public static void upisiUDnevnik(String zapis, BP_Konfiguracija konfig) {
        String upit = "Insert into Dnevnik values (default, '"+zapis+"', '"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"')";
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinkaBaze = konfig.getAdminPassword();
        try (Connection con = (Connection) DriverManager.getConnection(url, korisnikBaze, lozinkaBaze);
                Statement stmt = (Statement) con.createStatement();
                ) {
                stmt.execute(upit);
                System.out.println("izvrsio upit");
                stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(RadSaBazom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static List<DnevnikModel> dohvatiDnevnik(BP_Konfiguracija konfig){
        String upit = "Select * from dnevnik";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<DnevnikModel> listaDnevnika = new ArrayList<>();
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String korisnikBaze = konfig.getAdminUsername();
        String lozinka = konfig.getAdminPassword();
        System.out.println("username: " + korisnikBaze);
        System.out.println("password: " + lozinka);
        try (Connection connect = DriverManager.getConnection(url, korisnikBaze, lozinka);
                Statement stm = connect.createStatement();) {
            System.out.println("spojio sam se");
            ResultSet rs = stm.executeQuery(upit);
            while (rs.next()) {
                DnevnikModel dnevnik = new DnevnikModel();
                dnevnik.setId(rs.getInt("ID"));
                dnevnik.setSadrzaj(rs.getString("SADRZAJ"));
                String vrijeme = rs.getString("VRIJEME");
                dnevnik.setVrijeme(LocalDateTime.parse(vrijeme));
                listaDnevnika.add(dnevnik);
            }
            stm.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println("SQL Error exception" + e.getMessage());
        }
        System.out.println("lista korisnika:" + String.valueOf(listaDnevnika.size()));
        return listaDnevnika;
    }
}
