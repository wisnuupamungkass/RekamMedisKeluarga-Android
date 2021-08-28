package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePerempuanFragment extends Fragment {
    SessionManager session;
    protected String stremail;
    protected ImageButton btn_anak;
    protected ImageButton btn_profil;
    protected ImageButton btn_kandungan;
    protected ImageButton btn_medis;
    protected ImageButton btn_pesan;
    protected TextView jumlah_pesan;

    protected static final String TAG_KTP = "no_ktp";
    protected String strktp;
    protected JSONParser jsonParser;
    protected int success;


    @Override
    public void onStart(){
        super.onStart();
        this.session = new SessionManager(getActivity());
        this.session.checkLoginPerempuan();

        stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);
        this.strktp = this.session.getUserDetails().get(SessionManager.KEY_KTP);
        session = new SessionManager(getActivity().getApplicationContext());

        Log.e("Error", "KTP : "+strktp);

        if(strktp!=null){
            new tampil_pesan().execute();
        }

    }

    public HomePerempuanFragment(){
        this.jsonParser = new JSONParser();
    }

    public class tampil_pesan extends AsyncTask<String, String, String> {
        String strjumlah;
        String url_jumlah_pesan;

        public tampil_pesan(){
            this.url_jumlah_pesan= "http://rekammedis.gudangtechno.web.id/android/tampil_jumlah_pesan.php?no_ktp="+HomePerempuanFragment.this.strktp;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = HomePerempuanFragment.this.jsonParser.getJSONFromUrl(url_jumlah_pesan);

            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("pesan");
                if (success == 1) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        this.strjumlah = c.getString("jumlah").trim();
                        Log.e("OK", " Ambil Data");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (success == 1) {
                jumlah_pesan.setText(this.strjumlah);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_home_perempuan, container, false);

        btn_anak =(ImageButton) myView.findViewById(R.id.menu_anak);
        btn_profil =(ImageButton) myView.findViewById(R.id.profil_perempuan);
        btn_kandungan =(ImageButton) myView.findViewById(R.id.menu_kandungan);
        btn_medis = (ImageButton) myView.findViewById(R.id.menu_rekam_medis);
        btn_pesan = (ImageButton) myView.findViewById(R.id.menu_pesan);
        jumlah_pesan = (TextView) myView.findViewById(R.id.badge_textView);

        this.btn_profil.setOnClickListener(new menu_profil());
        this.btn_kandungan.setOnClickListener(new menu_kandungan());
        this.btn_medis.setOnClickListener(new menu_medis());
        this.btn_pesan.setOnClickListener(new menu_pesan());


        btn_anak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction;
                HomeAnakFragment fragment = new HomeAnakFragment();
                fragmentTransaction = HomePerempuanFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                getActivity().setTitle("Home Daftar Anak");
            }
        });

        return myView;
    }

    private class menu_profil implements View.OnClickListener {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction;
            ProfilPerempuanFragment fragment = new ProfilPerempuanFragment();
            fragmentTransaction = HomePerempuanFragment.this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            getActivity().setTitle("Profil Pengguna");
        }
    }

    private class menu_kandungan implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(HomePerempuanFragment.this.getActivity(), ListKandunganActivity.class);
            startActivity(intent);
        }
    }

    private class menu_pesan implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(HomePerempuanFragment.this.getActivity(), NotifikasiPesanActivity.class);
            startActivity(intent);
        }
    }

    private class menu_medis implements View.OnClickListener {
        public void onClick(View v) {
            HomePerempuanFragment.this.startActivity(new Intent(HomePerempuanFragment.this.getActivity(), ListRekamMedisPerempuanActivity.class));
        }
    }
}