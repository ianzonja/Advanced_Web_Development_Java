/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.aplikacija_3_web.klijenti;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ian
 */
public class SocketKlijent {
    public static String posaljiZahtjevNaSocket(String komanda){
        try {
            String ipAdress = "127.0.0.1";
            int portZaSpajanje = 446;
            Socket socket;
            try {
                socket = new Socket(ipAdress, portZaSpajanje);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                os.write(komanda.getBytes());
                os.flush();
                socket.shutdownOutput();
                StringBuffer sb = new StringBuffer();
                int bajt = 0;
                while (bajt != -1) {
                    bajt = is.read();
                    sb.append((char) bajt);
                }
                System.out.println("Odgovor: " + sb.toString());
                return sb.toString();
            } catch (IOException ex) {
                Logger.getLogger(SocketKlijent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "Doslo je do greske sa socketom, pokusajte ponovo";
    }
}
