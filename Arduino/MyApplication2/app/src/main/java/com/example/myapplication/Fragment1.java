package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import static com.example.myapplication.MainActivity.btSocket;

public class Fragment1  extends Fragment {
    Button openBtn;
    Button closeBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        openBtn = view.findViewById(R.id.button4);
        closeBtn = view.findViewById(R.id.button5);
        openBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDoor();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closeDoor();
            }
        });

        return view;
    }

    private void msg(String s) {
        Toast.makeText(getContext() ,s,Toast.LENGTH_SHORT).show();
    }
    private void openDoor(){
        if(btSocket!=null){
            try{
                btSocket.getOutputStream().write("1".getBytes());
            }
            catch(IOException e){
                msg("Error");
            }
        }
    }
    private void closeDoor(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("0".getBytes());
            }
            catch(IOException e){
                msg("Error");
            }
        }
    }
}
