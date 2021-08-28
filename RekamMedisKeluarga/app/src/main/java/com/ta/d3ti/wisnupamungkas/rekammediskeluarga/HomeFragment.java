package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    JSONParser jsonParser = new JSONParser();
    protected String url_login_petugas= "http://rekammedis.gudangtechno.web.id/android/login_petugas.php";

    protected static final String TAG_EMAIL = "email";
    protected static final String TAG_PASS = "password";

    protected String email,password,status;
    protected int success;
    protected SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_home_petugas, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        return myView;
    }

    public void onStart() {
        super.onStart();

        session = new SessionManager(getActivity().getApplicationContext());
        session.isLoggedInPetugas();
        session.checkLoginPetugas();

        HashMap <String,String> petugas = session.getPetugasDetails();
        email = petugas.get(SessionManager.KEY_EMAIL);
        password = petugas.get(SessionManager.KEY_PASS);

        new CekFragment().execute();
    }

    public class CekFragment extends AsyncTask<String,String,String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HomeFragment.this.getActivity());
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

            try {
                success = json.getInt("success");
                status = json.getString("status");

                Log.e("Error", "status=" + status);

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
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            FragmentTransaction fragmentTransaction;
            if (status.equalsIgnoreCase("Belum")) {
                KlinikFragment fragment = new KlinikFragment();
                fragmentTransaction = HomeFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            } else if (status.equalsIgnoreCase("Sudah")) {
                TokenFragment fragment = new TokenFragment();
                fragmentTransaction = HomeFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            } else if (status.equalsIgnoreCase("Tunggu")) {
                TungguFragment fragment = new TungguFragment();
                fragmentTransaction = HomeFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(HomeFragment.this.getActivity(), "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
                HomeFragment.this.getActivity().finish();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }
}
