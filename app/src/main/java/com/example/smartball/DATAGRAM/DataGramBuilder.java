package com.example.smartball.DATAGRAM;

import android.util.Log;

import java.util.Random;

public class DataGramBuilder {


    private Random random ;

    public DataGramBuilder() {
        random = new Random();
    }

    private byte colorValues(){

        return (byte) random.nextInt(255+1);
    }

    /**
     *
     * @param command pour la CMD_COLOR1 ET 2
     * @param mode
     * @return
     */
    public byte[] getRandomColorData(int command,int mode){

        int taille=mode*3+1;

        byte[] colorData=new byte[taille];
        colorData[0]=(byte) command;

        for(int i = 1; i<taille; i=i+3){

            colorData[i]=colorValues();
            colorData[i+1]=colorValues();
            colorData[i+2]=colorValues();
        }

        return colorData;
    }

    public byte[] streamData(int nbrBall, int red , int green , int blue){
        byte[] data= new byte[nbrBall*3+5];
        data[0]=Command.CMD_STREAM;
        //flag
        data[1]=0x00;
        data[2]=0x01;
        //offset
        data[3]=0x00;
        data[4]=0x00;

        for(int i =5;i<data.length;i=i+3){
            data[i]=(byte)red;
            data[i+1]=(byte)green;
            data[i+2]=(byte)blue;
        }
        return data;
    }

    public byte[] streamRandomData(int nbrBall){
        byte[] data= new byte[nbrBall*3+5];
        data[0]=Command.CMD_STREAM;
        //flag
        data[1]=0x00;
        data[2]=0x01;
        //offset
        data[3]=0x00;
        data[3]=0x00;


        for(int i=5;i<data.length;i=i+3){

            data[i]=colorValues();
            data[i+1]=colorValues();
            data[i+2]=colorValues();
        }

        return data;
    }

    public byte[] streamDataConstruct(int nbr_balle,int nbr_color1, int nbr_color2,boolean strobe, boolean moteur){

        int taille = (nbr_balle *(3*nbr_color2 + 3*nbr_color1)+5);
        Log.i("stream","taile du date: " + Integer.toString(taille));
        int act_strobe=0;
        int act_moteur=0;

        if(strobe){
            act_strobe=1;
        }
        if (moteur){
            act_moteur=1;
        }

        byte[] data= new byte[taille];

        data[0]=Command.CMD_STREAM;
        byte flag_left_bite = (byte) (512*act_moteur);
        byte flag_right_bite = (byte) ((128*act_strobe)+(8*nbr_color2)+(nbr_color1));
        //flag
        data[1]=flag_left_bite;
        data[2]=flag_right_bite;
        //offset
        data[3]=0x00;
        data[4]=0x01;

        int indixData=5;

        int nbr_rgb1= 3*nbr_color1;

        Log.i("stream","nombre de couelur 1: " + Integer.toString(nbr_rgb1));
        for(int i=0;i<nbr_balle;i++){//pour chaque balle

            for(int c = 0; c<nbr_rgb1; c=c+3){//Ajoute tout le rgb en fonction du nombre de coleurs voulue pour la couleur 1

                data[indixData+c] = colorValues();
                data[indixData+c+1]=colorValues();
                data[indixData+c+2]=colorValues();
            }
            Log.i("stream","index avant : " + Integer.toString(indixData));
            indixData= indixData +nbr_rgb1; //augement l'indice sur le tableau data
            Log.i("stream","index apres: " + Integer.toString(indixData));
        }


        int nbr_rgb2 = 3*nbr_color2;
        Log.i("stream","nombre de couelur 2: " + Integer.toString(nbr_rgb2));
        for(int i=0; i<nbr_balle;i++){ // pour

            for(int c = 0; c<nbr_rgb2; c=c+3){

                data[indixData+c]=colorValues();
                data[indixData+c+1]=colorValues();
                data[indixData+c+2]=colorValues();
            }
            Log.i("stream","index avant : " + Integer.toString(indixData));


            indixData= indixData +nbr_rgb2;
            Log.i("stream","index apres: " + Integer.toString(indixData));

        }

        return data;
    }



    public byte[] accRangeData(){

        int taille=2;
        byte[] data= new byte[taille];
        data[0]=Command.CMD_ACCRANGE;
        data[1]=0x00;
        return data;
    }

    public byte[] gyrRangeData(){

        int taille=2;
        byte[] data= new byte[taille];
        data[0]=Command.CMD_GYRRANGE;
        data[1]=0x00;
        return data;
    }

    public byte[] IMUSettings(int setting){

        byte[] settingDate = new byte[2];
        settingDate[0]=Command.SAVE_IMU;
        settingDate[1]=(byte) setting;

        return settingDate;
    }


    public byte[] MotorData(){
        byte[] motorData = new byte[3];

        motorData[0]=Command.CMD_MOT;

        return motorData;
    }

    public byte[] strobeRandomSetting(){
        byte[] strobeSetting = new byte[2];
        strobeSetting[0]=Command.CMD_STB;
        strobeSetting[1]= (byte)(random.nextInt((255-10)+1)-10);
        return strobeSetting;
    }

    public byte[] strobeSetting(int speed){
        byte[] strobeSetting = new byte[2];
        strobeSetting[0]=Command.CMD_STB;
        strobeSetting[1]= (byte)speed;
        return strobeSetting;
    }



}
