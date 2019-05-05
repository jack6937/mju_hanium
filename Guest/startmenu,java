package com.example.otp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import java.util.Random;


public class startmenu extends AppCompatActivity {

       static Random rnd = new Random();
        static int OTP = rnd.nextInt(9999);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startmenu);


    Intent intent= new Intent(getIntent());
        String Id =intent.getStringExtra("id");
        String Pass =intent.getStringExtra("pass");
        Toast.makeText(getApplicationContext(),"내가입력한아이디=" +Id +"내가입력한비번"+Pass , Toast.LENGTH_SHORT).show();

        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"자물쇠가 닫혔습니다" , Toast.LENGTH_SHORT).show();

            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 rnd = new Random();
                 OTP = rnd.nextInt(9999);
                Toast.makeText(getApplicationContext(),"새 OTP번호가 발급되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"(임시)귀하의 OTP번호는 "+OTP+" 입니다.", Toast.LENGTH_SHORT).show();

            }
        });



    }
    void show(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("OTP");
        alert.setMessage("OTP 번호를 입력 하십시오");


        final EditText name = new EditText(this);
        alert.setView(name);



        //원래 이게 취소버튼인데 취소버튼ㅇ이 왼쪽으로 오는 것 때문에
        //이걸 확인 버튼으로 쓸 예정!!!
        alert.setNegativeButton("확인",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String username = name.getText().toString();
                String stringOTP = Integer.toString(OTP);
                if (username.equals(stringOTP))
                Toast.makeText(getApplicationContext(),"자물쇠가 열였습니다", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"OTP 번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });

        alert.show();
    }


    }
