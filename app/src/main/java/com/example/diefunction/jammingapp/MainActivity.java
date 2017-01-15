package com.example.diefunction.jammingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient client;
    private EditText editText;
    private EditText editText2;
    private Button startBtn;
    private Button stopBtn;
    private TextView err;
    private TextView err2;
    private final String defaultFreq = "433000";
    private String frequency;
    private String time;
    private final String host = ""; // your server ip
    private final int port = 0; // your server port number
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        startBtn = (Button) findViewById(R.id.button1);
        err = (TextView) findViewById(R.id.textView3);
        err2 = (TextView) findViewById(R.id.textView9);
        startBtn.setOnClickListener(btnListner);
        initTextFields();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                .setUrl(Uri.parse("http://www.diefunction.com/"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public void initTextFields(){
        editText.setText(defaultFreq);
    }

    public boolean isValidFreqTime(){

        frequency = editText.getText().toString();
        time = editText2.getText().toString();
        if(!frequency.equals("") && isNumeric(frequency) && Integer.parseInt(frequency) > 0){
            if(!time.equals("") && isNumeric(time) && Integer.parseInt(time) > 0) {
                for (int i = frequency.length(); i < 6; i++) {
                    frequency += "0";
                }
                return true;
            }
        }
        return false;
    }

    private View.OnClickListener btnListner = new View.OnClickListener() {
        @Override
        public void onClick(View v){

            if(v.getId() == startBtn.getId()){
                if(isValidFreqTime()){
                    startBtn.setEnabled(false);
                    err.setVisibility(View.INVISIBLE);
                    err2.setVisibility(View.INVISIBLE);
                    Client myClient = new Client(host, port, frequency);
                    myClient.execute();
                    Client myClient2 = new Client(host, port, time);
                    myClient2.execute();
                    try {
                        Thread.sleep(Integer.parseInt(time)*1000);
                        startBtn.setEnabled(true);
                    } catch (InterruptedException e) {
                    }
                }
                else{
                    err.setVisibility(View.VISIBLE);
                    err2.setVisibility(View.VISIBLE);
                }
            }
            return;
        }
    };

    public boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
