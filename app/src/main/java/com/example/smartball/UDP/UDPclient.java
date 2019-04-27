package com.example.smartball.UDP;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.smartball.DATAGRAM.Command;
import com.example.smartball.DATAGRAM.DataGramBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class UDPclient implements Runnable {

    private String inetAddress;
    private boolean random;
    byte[] data;
    private int infint;

    public UDPclient(String inetAddress, byte[] data) {
        this.inetAddress = inetAddress;
        this.data = data;
        this.random = false;
    }

    public UDPclient(String inetAddress, boolean random) {

        data = null;
        this.inetAddress = inetAddress;
        this.random = random;
    }

    public UDPclient(String inetAddress, int infinit) {

        this.inetAddress = inetAddress;
        this.infint = infinit;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendData(String string, byte[] date) {


        try (DatagramSocket clientSocket = new DatagramSocket()) {

            InetAddress serveur = InetAddress.getByName(string);
            DatagramPacket emises = new DatagramPacket(date, date.length, serveur, 8000);
            clientSocket.send(emises);

        } catch (SocketException e) {

            Log.i("beug", e.toString());

        } catch (IOException e) {
            Log.i("beug", "erreur2");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendRandom(String string) {

        DataGramBuilder dataGramBuilder = new DataGramBuilder();

        try (DatagramSocket clientSocket = new DatagramSocket()) {

            while (true) {

                byte[] data = dataGramBuilder.streamRandomData(2);
                InetAddress serveur = InetAddress.getByName(string);
                DatagramPacket emises = new DatagramPacket(data, data.length, serveur, 8000);
                clientSocket.send(emises);
            }

        } catch (SocketException e) {


            Log.i("beug", e.toString());

        } catch (IOException e) {

            Log.i("beug", "erreur2");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendInfinit(String string) {

        Random random = new Random();
        DataGramBuilder dataGramBuilder = new DataGramBuilder();

        final int[] MODE_COLOR = {Command.MODE_ONE, Command.MODE_HEMISPHERE, Command.MODE_OPPOSITE, Command.MODE_INDIVIDUAL};
        try (DatagramSocket clientSocket = new DatagramSocket()) {

            while (true) {

                int fore = MODE_COLOR[random.nextInt(MODE_COLOR.length)];
                int back = MODE_COLOR[random.nextInt(MODE_COLOR.length)];
                byte[] data = dataGramBuilder.streamDataConstruct(3, fore, back, false, false);
                InetAddress serveur = InetAddress.getByName(string);
                DatagramPacket emises = new DatagramPacket(data, data.length, serveur, 8000);
                clientSocket.send(emises);
                try {
                    Thread.sleep(10);
                }catch (Exception e){

                }
            }

        } catch (SocketException e) {


            Log.i("beug", e.toString());

        } catch (IOException e) {

            Log.i("beug", "erreur2");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {

        if (infint == 1) {

            sendInfinit(inetAddress);

        } else {

            if (random) {

                sendRandom(inetAddress);

            } else {

                sendData(inetAddress, data);
            }
        }
    }
}
