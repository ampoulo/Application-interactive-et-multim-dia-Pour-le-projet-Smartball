package com.example.smartball.UDP;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPserver implements Runnable {
    /**
     * The port where the client is listening.
     */

    public UDPserver() {


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        /**
         * Create a new server socket and bind it to a free port. I have chosen
         * one in the 49152 - 65535 range, which are allocated for internal applications
         */
        while(true){

            try (DatagramSocket serverSocket = new DatagramSocket(9000)) {

                byte[] buffer= new byte[1024];
                buffer[0]=(byte) 0xFF;
                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length);
                serverSocket.receive(datagramPacket);
                if(buffer[0]!=(byte)0xFF){
                    Log.i("MESSAGE","C'EST BON LA BALLE À ENVOYER UN TRUC REGARDE BEIN");
                }

            } catch (SocketException e) {
                e.printStackTrace();

            } catch (UnknownHostException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}
