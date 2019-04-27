package com.example.smartball;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.example.smartball.DATAGRAM.Command;
import com.example.smartball.DATAGRAM.DataGramBuilder;
import com.example.smartball.UDP.UDPclient;
import com.example.smartball.UDP.UDPserver;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private final String MULTCAST="192.168.150.25";

    private RadioGroup command ;
    private CheckBox mode_random;
    private EditText ip_ball;
    private NumberPicker nbr_ball;
    private Button send_command;
    private ImageView imageView;
    private HSLColorPicker hslColorPicker;
    private UDPclient udPclient;
    private DataGramBuilder dataGramBuilder;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        command= findViewById(R.id.command);
        mode_random = findViewById(R.id.mode_random);
        ip_ball = findViewById(R.id.ip_ball);
        nbr_ball = findViewById(R.id.stream_balls);
        nbr_ball.setMinValue(1);
        nbr_ball.setMaxValue(30);
        send_command = findViewById(R.id.send_command);
        imageView = findViewById(R.id.imageView3);
        dataGramBuilder = new DataGramBuilder();
//
//        UDPserver server = new UDPserver();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        executorService.submit(server);

        hslColorPicker = findViewById(R.id.colorPicker);



        setListner();
        command.check(R.id.stream);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setListner(){

        mode_random.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    setRandomVisibility();
                }else{
                    setPickerVisibility();
                }

            }
        });

        command.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checked = group.getCheckedRadioButtonId();

                if(checked == R.id.foreground){

                    ip_ball.setVisibility(View.VISIBLE);
                    nbr_ball.setVisibility(View.INVISIBLE);


                    if(mode_random.isChecked()){
                        setRandomVisibility();



                    }else{

                        setPickerVisibility();
                    }

                }else if(checked == R.id.background){

                    ip_ball.setVisibility(View.VISIBLE);
                    nbr_ball.setVisibility(View.INVISIBLE);

                    if(mode_random.isChecked()){
                        setRandomVisibility();

                    }else{

                        setPickerVisibility();
                    }

                }else if(checked == R.id.stream){

                    ip_ball.setVisibility(View.INVISIBLE);
                    nbr_ball.setVisibility(View.VISIBLE);

                    if(mode_random.isChecked()){

                        setRandomVisibility();

                        send_command.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int nbr = nbr_ball.getValue();



                                try {

                                        byte[] data = dataGramBuilder.streamRandomData(nbr);
                                        udPclient = new UDPclient("239.0.0.50", true);
                                        ExecutorService executorService = Executors.newFixedThreadPool(1);
                                        executorService.submit(udPclient);
                                        Log.i("beug", "DATA RANDOM OK");

                                }catch (Exception e){

                                    Log.i("beug",e.toString());

                                }
                            }
                        });

                    }else{

                        setPickerVisibility();
                        hslColorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
                            @Override
                            public void onColorSelected(int color) {

                                imageView.setBackgroundColor(color);
                                int nbr = nbr_ball.getValue();

                                int red =Color.red(color);
                                int green =Color.green(color);
                                int blue =Color.blue(color);

                                //byte[] data= dataGramBuilder.streamData(nbr,red,green,blue);
                                byte[] data= dataGramBuilder.streamDataConstruct(nbr, Command.MODE_OPPOSITE,Command.MODE_OPPOSITE,false,false);

                                try {

                                    udPclient= new UDPclient("239.0.0.50",data);
                                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                                    executorService.submit(udPclient);
                                    Log.i("beug","DATA SEND OK");

                                }catch (Exception e){
                                    Log.i("beug",e.toString());
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    private void setRandomVisibility(){

        hslColorPicker.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        send_command.setVisibility(View.VISIBLE);

    }

    private  void setPickerVisibility(){

        hslColorPicker.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        send_command.setVisibility(View.INVISIBLE);
    }

}
