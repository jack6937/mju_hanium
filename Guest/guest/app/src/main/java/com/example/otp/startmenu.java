package com.example.otp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class startmenu extends AppCompatActivity {
    static String OTP="";
    static String id="";
    static String pass="";
    private Handler mHandler;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    private String msg;
    private String mJsonString;
    private static String TAG = "phptest";

    ImageView imageview1 = null, imageview2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startmenu);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = new Intent(getIntent());
        id = intent.getStringExtra("id");
        pass = intent.getStringExtra("pass");

        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);

        mHandler = new Handler();

        imageview1 = (ImageView)findViewById(R.id.imageView);
        imageview2= (ImageView)findViewById(R.id.imageView2);
        imageview1.setImageResource(R.drawable.lockimage);
        imageview2.setImageResource(R.drawable.unlockiamge);

        imageview1.setVisibility(View.VISIBLE);
        imageview2.setVisibility(View.INVISIBLE);
        new BackgroundTask().execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        closeDoor();
                    }
                }).start();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://ubuntu@13.125.102.51/otpGen.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("ID", id));
                    nameValuePairs.add(new BasicNameValuePair("password", pass));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    new BackgroundTask().execute();
                    Log.d("MainActivity", "OTP 요청");
                    Toast.makeText(getApplicationContext(), "새 otp가 발급되었습니다", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
            }

        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "귀하의 OTP번호는 " + OTP + " 입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://ubuntu@13.125.102.51/getjson.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                String s= stringBuilder.toString().trim();
                return s;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null){

            }else{
                mJsonString = result;
                showResult();
            }
        }
    }
    private void showResult(){
        String TAG_JSON = "Client";
        String TAG_ID = "ID";
        String TAG_OTP = "OTP";
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            String temp = "";
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);
                temp = item.getString(TAG_ID);
                if(temp.equals(id)){
                    OTP = item.getString(TAG_OTP);
                }
            }
        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }
    void show(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("OTP");
        alert.setMessage("OTP 번호를 입력 하십시오");

        final EditText name = new EditText(this);
        alert.setView(name);



        alert.setNegativeButton("확인",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String username = name.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(username);
                    }
                }).start();

            }
        });

        alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                name.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) startmenu.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
                        name.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                });
            }
        });
        name.requestFocus();
        alert.show();
    }

    public void printClientLog(final String data){
        Log.d("MainActivity", data);
    }

    public void send(String data){ // data는 OTP 번호 일거임
        try{
            int portNumber = 5001;
            Socket sock = new Socket("172.20.10.4", portNumber);
            printClientLog("소켓 연결함");

            ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
            data = data.concat("|" + id);
            outstream.writeObject(data);
            outstream.flush();
            printClientLog("데이터 전송함. ");

            ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
            msg = instream.readObject().toString();
            Log.d("MainActivity", "msg : " + msg);
            mHandler.post(showUpdate);

            sock.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void closeDoor(){
        try{
            int portNumber = 5001;
            Socket sock = new Socket("172.20.10.4", portNumber);
            printClientLog("소켓 연결함");

            ObjectOutputStream oustream = new ObjectOutputStream(sock.getOutputStream());
            oustream.writeObject("문을 닫아주세요");
            oustream.flush();
            printClientLog("데이터 전송함. ");

            ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
            msg = instream.readObject().toString();
            mHandler.post(showUpdate);
            Log.d("MainActivity", "msg : " + msg);
            sock.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(startmenu.this, "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return false;
    }

    private Runnable showUpdate = new Runnable() {
        @Override
        public void run() {
            if(msg.equals("문이 열렸습니다")){
                imageview1.setVisibility(View.INVISIBLE);
                imageview2.setVisibility(View.VISIBLE);
            }
            else if(msg.equals("문이 닫혔습니다")){
                imageview1.setVisibility(View.VISIBLE);
                imageview2.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(startmenu.this, msg, Toast.LENGTH_LONG).show();
            //otp 맞게 입력하면
            //자물쇠가 열였다 메세지와 함께 아래코드로 로고 이미지 변환
        }
    };

}