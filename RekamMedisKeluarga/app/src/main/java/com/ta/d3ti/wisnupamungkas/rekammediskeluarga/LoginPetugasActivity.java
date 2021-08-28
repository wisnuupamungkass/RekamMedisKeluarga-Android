package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginPetugasActivity extends Activity {
    JSONParser jsonParser = new JSONParser();
    protected String url_login_petugas= "http://rekammedis.gudangtechno.web.id/android/login_petugas.php";

    protected static final String TAG_EMAIL = "email";
    protected static final String TAG_PASS = "password";

    protected int success;

    protected Button button_login, button_register;
    protected String email, password;
    protected EditText EdTxtemail, EdTxtpassword;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_petugas);

        session = new SessionManager(getApplication());

        EdTxtemail = (EditText) findViewById(R.id.email);
        EdTxtpassword = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.button_login);
        button_register = (Button) findViewById(R.id.button_register);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = EdTxtemail.getText().toString();
                password = EdTxtpassword.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(email)) {
                    EdTxtemail.setError("Harus diisi");
                    focusView = EdTxtemail;
                    cancel = true;
                }
                if (TextUtils.isEmpty(password)) {
                    EdTxtpassword.setError("Harus diisi");
                    focusView = EdTxtpassword;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    new LoginPetugas().execute();
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginPetugasActivity.this.startActivity(new Intent(LoginPetugasActivity.this, RegisterPetugasActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public class LoginPetugas extends AsyncTask<String,String,String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginPetugasActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            params.add(new BasicNameValuePair(TAG_PASS, password));

            JSONObject json = jsonParser.makeHttpRequest(url_login_petugas, "GET", params);

            if(json == null){
                return "Error Converting";
            }

            try {
                success = json.getInt("success");

                Log.e("error", "nilai sukses=" + success);

                JSONArray hasil = json.getJSONArray("petugas");

                for (int i = 0; i < hasil.length(); i++) {
                    JSONObject c = hasil.getJSONObject(i);

                    String email = c.getString("email").trim();
                    String password = c.getString("password").trim();
                    String id_petugas = c.getString("id_petugas").trim();
                    String status = c.getString("status").trim();

                    session.createLoginPetugas(email, password, id_petugas, status);
                }
                return "OK";
            } catch (JSONException e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(LoginPetugasActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 0){
                Toast.makeText(LoginPetugasActivity.this, "Username or Password Tidak Ditemukan",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                LoginPetugasActivity.this.startActivity(new Intent(LoginPetugasActivity.this, MainPetugasActivity.class));
                LoginPetugasActivity.this.finish();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(LoginPetugasActivity.this, "Koneksi Internet Anda Bermasalah...",  Toast.LENGTH_SHORT).show();
            }
        }
    }
}