package com.example.otp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class startmenu extends AppCompatActivity {
    static String OTP="";
    static String id="";
    static String pass="";
    ViewFlipper Vf;
    Button BtnSignIn, BtnSignUp;
    EditText inputID, inputPW;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    TextView tv;
    private String mJsonString;
    private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startmenu);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = new Intent(getIntent());
        id = intent.getStringExtra("id");
        pass = intent.getStringExtra("pass");

        //    Toast.makeText(getApplicationContext(),"내가입력한아이디=" +id +"내가입력한비번"+pass , Toast.LENGTH_SHORT).show();

        // tv = (TextView) findViewById(R.id.textView3);

        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);

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
                Toast.makeText(getApplicationContext(), "자물쇠가 닫혔습니다", Toast.LENGTH_SHORT).show();
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
                //  tv.setText(s);
                return s;
                //return stringBuilder.toString().trim();
                // return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함
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

        //원래 이게 취소버튼인데 취소버튼ㅇ이 왼쪽으로 오는 것 때문에

        //이걸 확인 버튼으로 쓸 예정!!!

        alert.setNegativeButton("확인",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String username = name.getText().toString();
                //String stringOTP = Integer.toString(OTP);
                if (username.equals(OTP))
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