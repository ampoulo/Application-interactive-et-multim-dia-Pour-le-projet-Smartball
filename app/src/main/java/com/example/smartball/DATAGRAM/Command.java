package com.example.smartball.DATAGRAM;

public class Command {


    public static final int SOP      =    0xE7;
    public static final int CMD_BAT    =  0x00;
    public static final int CMD_PING   =  0x01;
    public static final int CMD_REBOOT  = 0x02;
    public static final int CMD_SLEEP  =  0x03;
    public static final int CMD_FACTORY=  0x10;
    public static final int SAVE_FACTORY= 0x11;
    public static final int CMD_GENERAL = 0x12;
    public static final int SAVE_GENERAL =0x13;
    public static final int CMD_COLOR1 	= 0x20;
    public static final int CMD_STREAM  = 0x21;
    public static final int CMD_COLOR2  = 0x22;
    public static final int CMD_IMU     = 0x30;
    public static final int CMD_ACCRANGE= 0x32;
    public static final int CMD_GYRRANGE =0x33;
    public static final int SAVE_IMU   =  0x31;
    public static final int CMD_IRL   =   0x40;
    public static final int CMD_MOT   =   0x50;
    public static final int CMD_STB	  =   0x60;
    public static final int CMD_MST		= 0x70;

    public final static int  MODE_ONE=1;
    public final  static int MODE_HEMISPHERE=2;
    public final  static int MODE_OPPOSITE=3;
    public final  static  int MODE_INDIVIDUAL=6;
}