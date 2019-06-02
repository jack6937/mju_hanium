package com.example.myapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    String address = null;
    private ProgressDialog progress;
    public static BluetoothAdapter myBluetooth = null;
    public static BluetoothSocket btSocket = null;

    private boolean isBTConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static String data;
    String socket_ID;
    String socket_OTP;
    int a1;

    public static boolean lock; // 1이면 open 0이면 close
    public static boolean know;

    private static String IP_ADDRESS = "ubuntu@13.125.102.51";
    private static String TAG = "phptest";

    public static ArrayList<PersonalData> mArrayList;
    public static UsersAdapter mAdapter;
    public static RecyclerView mRecyclerView;
    public static String mJsonString;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
        setContentView(R.layout.activity_main);

        new ConnectBT().execute();
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        lock = false;
        mHandler = new Handler();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                        return true;
                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                        return true;
                }
                return false;
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run() {
                startServer();
            }
        }).start();

    }

    public void startServer() {
        try {
            int portNumber = 5001;

            ServerSocket server = new ServerSocket(portNumber);
            printServerLog("서버 시작함: " + portNumber);
            while (true) {
                Socket sock = server.accept();
                InetAddress clientHost = sock.getLocalAddress();
                int clientPort = sock.getPort();
                printServerLog("LocalAddress: " + sock.getLocalAddress());
                printServerLog("inetAddress: " + sock.getInetAddress());
                printServerLog("클라이언트 연결됨 : " + clientHost + " : " + clientPort);

                ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
                Object obj = instream.readObject();

                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());

                printServerLog("데이터 받음: " + obj);

                data = obj.toString();

                if(data.equals("문을 닫아주세요")){
                    fragment1.closeDoor();
                    mHandler.post(showUpdate);
                    lock = false;
                    printServerLog("문이 닫히는 if문");
                    outstream.writeObject("문이 닫혔습니다");
                }
                else{
                    printServerLog("문이 열리는 else 문");
                    a1 = data.indexOf('|');
                    socket_ID = data.substring(a1 + 1);
                    socket_OTP = data.substring(0, a1);

                    printServerLog("a1 : " + a1 + " socket_ID : " + socket_ID + ", socket_OTP : " + socket_OTP);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GetData task = new GetData();
                            task.execute( "http://" + IP_ADDRESS + "/getjson.php", "");
                        }
                    }).start();

                    sleep(1500);

                    printServerLog("know : " + know);

                    if(know){
                        outstream.writeObject("문이 열렸습니다");
                        fragment1.openDoor();
                        mHandler.post(openUpdate);
                        lock = true;
                    }
                    else{
                        outstream.writeObject("OTP 번호가 틀렸습니다");
                    }
                    know = false;
                }
                outstream.flush();

                sock.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printServerLog(final String data){
        Log.d("MainActivity", data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Disconnect();
            return true;
        }
        return false;
    }
    private void Disconnect(){
        if(btSocket != null){
            try{
                btSocket.close();
            }
            catch(IOException e){
                msg("Error");
            }
        }
        Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override

        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please Wait!!!");
        }
        @Override
        protected Void doInBackground(Void... devices) {
            try{
                if(btSocket == null || !isBTConnected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch(IOException e){
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(!ConnectSuccess){
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else{
                msg("Connected");
                isBTConnected = true;
            }
            progress.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "환경설정 버튼 클릭됨", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.disConnect){
            Disconnect();
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetData extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null){

            }
            else {
                mJsonString = result;
                know = showResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }
    private boolean showResult(){
        String TAG_JSON="Client";
        String TAG_ID = "ID";
        String TAG_OTP = "OTP";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String ID = item.getString(TAG_ID);
                String OTP = item.getString(TAG_OTP);

                if(ID.equals(socket_ID) && OTP.equals(socket_OTP)){
                    return true;
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return false;
    }


    private Runnable showUpdate = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "문이 닫혔습니다", Toast.LENGTH_SHORT).show();
        }
    };

    private Runnable openUpdate = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "문이 열렸습니다", Toast.LENGTH_SHORT).show();
        }
    };
}
