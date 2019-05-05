package com.example.otp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText idText = (EditText) findViewById(R.id.emailInput);
        final EditText passwordText = (EditText) findViewById(R.id.passwordInput);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4=(Button) findViewById(R.id.button4);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), startmenu.class);
                String id = idText.getText().toString();
                String pass = passwordText.getText().toString();

                intent.putExtra("id", id);
                intent.putExtra("pass", pass);


                MainActivity.this.startActivity(intent);
            }
        });
           button4.setOnClickListener(new View.OnClickListener(){


                public void onClick (View v){
                Intent registerintent = new Intent(getApplicationContext(), registerActtivity.class);
                startActivity(registerintent);
            }

             });
    }



}
