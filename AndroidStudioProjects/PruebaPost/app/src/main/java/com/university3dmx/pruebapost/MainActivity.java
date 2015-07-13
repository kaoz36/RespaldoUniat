package com.university3dmx.pruebapost;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends Activity{

    httpHandler handler;
    TextView t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String json = "{\"password\":\"password123\",\"user\":\"juan\"}";


        handler = new httpHandler();


        try {
            handler.postCall("http://192.168.2.170/www/basura/allUsers.php", json, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    t.setText(e.getMessage());
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        t.setText(responseStr);
                    } else {
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        t = (TextView) findViewById(R.id.textview1);
        //t.setText( handler.post("http://192.168.2.170/www/basura/allUsers.php", "{\"password\":\"fskdlfsd\",\"user\":\"juan\"}") );
    }


}
