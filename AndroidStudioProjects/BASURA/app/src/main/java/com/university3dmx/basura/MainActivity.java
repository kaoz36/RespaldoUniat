package com.university3dmx.basura;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class MainActivity extends Activity {

    private EditText edit1;
    private EditText edit2;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    ArrayList<Comment> comentarios;
    ArrayList<String> comentariosString;
    Set<String> set;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shared = getSharedPreferences("shared", MODE_PRIVATE);
        edit = shared.edit();
        comentarios = new ArrayList<>();
        comentariosString = new ArrayList<>();
        init();

        set = new HashSet<>();
        set.addAll(comentariosString);

        edit.putStringSet("comentarios", set);
        edit.commit();

        edit1 = (EditText) findViewById(R.id.editText);
        edit2 = (EditText) findViewById(R.id.editText2);
        onclick(new View(getApplicationContext()));
    }

    private void init(){
        ArrayList<String> tem = new ArrayList<>();
        tem.add("1dfs321");
        tem.add("1321");
        tem.add("13tre21");
        tem.add("1lk321");
        tem.add("13qw21");
        tem.add("13zxkjh21");

        for( int i = 0; i < 10; i++){
            Comment comment = new Comment();
            comment.setId(i);
            comment.setComment("Comentario " + i);
            comment.setAlgo(tem);
            comentariosString.add(comment.toString());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onclick(View pview){
        Set<String> get = shared.getStringSet("comentarios", null);
        Object arrayGet = get.toArray();


        ArrayList<String> array;

    }


}
