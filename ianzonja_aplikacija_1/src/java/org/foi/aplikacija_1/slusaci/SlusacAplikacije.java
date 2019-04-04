package org.foi.aplikacija_1.slusaci;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.aplikacija_1.dretve.DretvaZaSocket;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.aplikacija_1.dretve.DohvatMeteoPodataka;
import org.foi.aplikacija_1.dretve.DretvaZaSocket;
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
@WebListener
public class SlusacAplikacije implements ServletContextListener {
    
    public DretvaZaSocket dretvaZaSocket;
    public DohvatMeteoPodataka dmp;
    public static ServletContext sc;
    public static BP_Konfiguracija bpk;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sc = sce.getServletContext();
        String datoteka = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF") + java.io.File.separator;
        bpk = new BP_Konfiguracija(putanja + datoteka);
        sc.setAttribute("BP_Konfig", bpk);
        dretvaZaSocket = new DretvaZaSocket(sc);
        dretvaZaSocket.start();
        dmp = new DohvatMeteoPodataka();
        dmp.start();
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dretvaZaSocket != null && dretvaZaSocket.isAlive()) {
            dretvaZaSocket.interrupt();
        }
        ServletContext sc = sce.getServletContext();
        sc.removeAttribute("BP_Konfig");
    }
}
