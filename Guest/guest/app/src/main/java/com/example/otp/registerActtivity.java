package com.example.otp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class registerActtivity extends AppCompatActivity {

    private static String IP_ADDRESS = "ubuntu@13.125.102.51";
    private static String TAG = "phptest";

    private EditText mEditTextID;
    private EditText mEditTextname;
    private EditText mEditTextpassword;
   private EditText mEditTextHP;
   // private EditText mEditTextOTP;
  //  private EditText mEditTextAuth;
    private TextView mTextViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acttivity);

        mEditTextID = (EditText)findViewById(R.id.editText_main_ID);
        mEditTextname = (EditText)findViewById(R.id.editText_main_name);
        mEditTextpassword = (EditText)findViewById(R.id.editText_main_password);
        mEditTextHP = (EditText)findViewById(R.id.editText_main_HP);
     //   mEditTextOTP = (EditText)findViewById(R.id.editText_main_OTP);
      //  mEditTextAuth = (EditText)findViewById(R.id.editText_main_Auth);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID = mEditTextID.getText().toString();
                String name = mEditTextname.getText().toString();
                String password = mEditTextpassword.getText().toString();
                String HP = mEditTextHP.getText().toString();
             //   String OTP = mEditTextOTP.getText().toString();
             //   String Auth = mEditTextAuth.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert2.php", ID,name,password,HP/*,OTP,Auth*/);


                mEditTextID.setText("");
                mEditTextname.setText("");
                mEditTextpassword.setText("");
                mEditTextHP.setText("");
              //  mEditTextOTP.setText("");
          //      mEditTextAuth.setText("");


            }
        });

    }



    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(registerActtivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);


            if (result.contains("사용자 추가함")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(registerActtivity.this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String)params[1];
            String name = (String)params[2];
            String password = (String)params[3];
            String HP = (String)params[4];
          //  String OTP = (String)params[5];
         //   String Auth = (String)params[6];

            String serverURL = (String)params[0];
            String postParameters = "ID=" + ID + "&name=" + name+"&password=" + password + "&HP=" + HP/*+"&OTP=" + OTP + "&Auth=" + Auth*/;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


}
