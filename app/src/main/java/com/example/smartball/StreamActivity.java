package com.example.smartball;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.smartball.DATAGRAM.Command;
import com.example.smartball.DATAGRAM.DataGramBuilder;
import com.example.smartball.UDP.UDPclient;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamActivity extends AppCompatActivity {

    private NumberPicker foreground;
    private NumberPicker background;
    private NumberPicker nbr_balle;
    private NumberPicker speed_strobe;
    private LinearLayout speed;
    private CheckBox strobe_checkbox;
    private CheckBox moteur_checkbox;
    private HSLColorPicker hslColorPicker;
    private ImageView imgColor;
    private final String[] MODE_COLOR = {"ONE", "HEMISPHERE", "OPPOSITE", "INDIVIDUAL"};
    private byte[] streamData;
    private UDPclient udPclient;
    private DataGramBuilder dataGramBuilder;
    private final String MULTCAST = "239.0.0.50";
    private ExecutorService executorService = Executors.newFixedThreadPool(2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        dataGramBuilder = new DataGramBuilder();
        streamData = dataGramBuilder.streamDataConstruct(1, Command.MODE_ONE, Command.MODE_ONE, false, false);
        getViewElements();
        setViewListener();

    }


    private void getViewElements() {
        foreground = findViewById(R.id.foreground);
        foreground.setMinValue(0);
        foreground.setMaxValue(MODE_COLOR.length - 1);
        foreground.setDisplayedValues(MODE_COLOR);
        background = findViewById(R.id.background);
        background.setMinValue(0);
        background.setMaxValue(MODE_COLOR.length - 1);
        background.setDisplayedValues(MODE_COLOR);


        nbr_balle = findViewById(R.id.stream_ball);
        nbr_balle.setMinValue(1);
        nbr_balle.setMaxValue(12);

        speed_strobe = findViewById(R.id.strobe_speed);
        speed_strobe.setMinValue(1);
        speed_strobe.setMaxValue(255);

        speed = findViewById(R.id.speed);

        strobe_checkbox = findViewById(R.id.strobe);
        moteur_checkbox = findViewById(R.id.moteur);
        hslColorPicker = findViewById(R.id.pikercolor);
        imgColor = findViewById(R.id.imgcolor);


    }

    private int fin = 0;

    private void setViewListener() {


        foreground.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    sendAdvanceStream();

            }
        });

        background.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sendAdvanceStream();
            }
        });

        strobe_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    sendAdvanceStream();
                    udPclient = new UDPclient(MULTCAST, dataGramBuilder.strobeRandomSetting());
                    executorService.submit(udPclient);
                    speed.setVisibility(View.VISIBLE);

                }else{

                    udPclient = new UDPclient(MULTCAST, dataGramBuilder.strobeSetting(0));
                    executorService.submit(udPclient);
                    speed.setVisibility(View.INVISIBLE);
                }
            }
        });


        speed_strobe.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                udPclient = new UDPclient(MULTCAST,dataGramBuilder.strobeSetting(newVal));
                executorService.submit(udPclient);
            }
        });

        moteur_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendAdvanceStream();
            }
        });

        nbr_balle.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sendAdvanceStream();
            }
        });

        hslColorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {

                imgColor.setBackgroundColor(color);
                int nbr = nbr_balle.getValue();
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                byte[] data = dataGramBuilder.streamData(nbr, red, green, blue);

                try {

                    udPclient = new UDPclient(MULTCAST, data);
                    executorService.submit(udPclient);
                    Log.i("beug", "DATA SEND OK");

                } catch (Exception e) {
                    Log.i("beug", e.toString());
                }
            }
        });

    }

    private void sendAdvanceStream() {

        int number_balle = nbr_balle.getValue();
        int back = pickerValue(MODE_COLOR[background.getValue()]);
        int fore = pickerValue(MODE_COLOR[foreground.getValue()]);
        boolean moteur = strobe_checkbox.isChecked();
        boolean strobo = moteur_checkbox.isChecked();
        streamData = dataGramBuilder.streamDataConstruct(number_balle, fore, back, strobo, moteur);
        try {
            udPclient = new UDPclient(MULTCAST, streamData);

            executorService.submit(udPclient);
            Log.i("beug", "DATA SEND OK");

        } catch (Exception e) {
            Log.i("beug", e.toString());
        }
    }

    private int pickerValue(String value) {

        switch (value) {
            case "ONE":
                return Command.MODE_ONE;
            case "HEMISPHERE":
                return Command.MODE_HEMISPHERE;
            case "OPPOSITE":
                return Command.MODE_OPPOSITE;
            case "INDIVIDUAL":
                return Command.MODE_INDIVIDUAL;
            default:
                return Command.MODE_ONE;
        }

    }

}
