package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.myapplication.MainActivity.btSocket;
import static com.example.myapplication.MainActivity.lock;

public class Fragment1  extends Fragment {
    Button openBtn;
    Button closeBtn;

    @SuppressLint("StaticFieldLeak")
    public static ImageView imageview1 = null;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageview2 = null;

    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        openBtn = view.findViewById(R.id.button4);
        closeBtn = view.findViewById(R.id.button5);
        openBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "문이 열렸습니다", Toast.LENGTH_SHORT).show();
                openDoor();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "문이 닫혔습니다", Toast.LENGTH_SHORT).show();
                closeDoor();
            }
        });

        imageview1 = view.findViewById(R.id.imageView1); // 잠긴거
        imageview2 = view.findViewById(R.id.imageView2); // 열린거

        if(!lock){
            imageview1.setVisibility(View.VISIBLE);
            imageview2.setVisibility(View.INVISIBLE);
        }
        else{
            imageview1.setVisibility(View.INVISIBLE);
            imageview2.setVisibility(View.VISIBLE);
        }

        mHandler = new Handler();

        return view;
    }

    private void msg(String s) {
        Toast.makeText(getContext(), s,Toast.LENGTH_SHORT).show();
    }
    public void openDoor(){
        if(btSocket!=null){
            try{
                btSocket.getOutputStream().write("1".getBytes());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageview1.setVisibility(View.INVISIBLE);
                        imageview2.setVisibility(View.VISIBLE);
                    }
                });
            }
            catch(IOException e){
                msg("Error");
            }
        }
    }
    public void closeDoor(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("0".getBytes());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageview1.setVisibility(View.VISIBLE);
                        imageview2.setVisibility(View.INVISIBLE);
                    }
                });
            }
            catch(IOException e){
                msg("Error");
            }
        }
    }
}
