package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    JSONParser jsonParser = new JSONParser();
    String url_login_user= "http://rekammedis.gudangtechno.web.id/android/login_user.php";

    // JSON Node names, ini harus sesuai yang di API
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_UID = "uid";

    String jenis_kelamin;
    int success;
    String name,email,uid;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplication());

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            name  = user.getDisplayName();
            email = user.getEmail();
            uid   = user.getUid();

            new LoginUser().execute();

        }
        else{
            goLoginScreen();
        }
    }

    private void goLoginScreen(){
        Intent intent =new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public class LoginUser extends AsyncTask<String,String,String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            params.add(new BasicNameValuePair(TAG_UID, uid));

            // Note that create Post url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login_user, "GET", params);
            if(json == null){
                return "Error Converting";
            }

            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                if(success == 1) {
                    jenis_kelamin = json.getString("jenis_kelamin");
                    Log.e("Error", "Jenis Kelamin=" + jenis_kelamin);
                }
                JSONArray hasil = json.getJSONArray("orangtua");

                for (int i = 0; i < hasil.length(); i++) {
                    JSONObject c = hasil.getJSONObject(i);

                    String email = c.getString("email").trim();
                    String no_ktp = c.getString("no_ktp").trim();
                    String jk = c.getString("jenis_kelamin").trim();

                    session.createLoginUser(email,no_ktp,jk);
                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(MainActivity.this, "Connection To Server Trouble During Login...",  Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
            else if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(MainActivity.this, "Can't Connect To Server During Login...",  Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
            else if (success == 0){
                Intent i = null;
                i = new Intent(MainActivity.this, RegisterUserActivity.class);
                Bundle b = new Bundle();
                b.putString("uid", uid);
                b.putString("nama", name);
                b.putString("email", email);
                i.putExtras(b);
                startActivity(i);
            }
            else if ((success == 1) && (jenis_kelamin.equalsIgnoreCase("L"))){
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainPriaActivity.class));
                MainActivity.this.finish();
            }
            else if ((success == 1) && (jenis_kelamin.equalsIgnoreCase("P"))){
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainPerempuanActivity.class));
                MainActivity.this.finish();
            }
        }
    }
}
