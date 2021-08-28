package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotifikasiPesanActivity extends AppCompatActivity {
    ListView list_pesan;

    ArrayList<Pesan> daftar_pesan = new ArrayList<>();
    JSONArray daftarpesan = null;
    protected JSONParser jsonParser;
    int success;
    SessionManager session;
    String strktp;

    public NotifikasiPesanActivity() {
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi_pesan);

        list_pesan = (ListView) findViewById(R.id.listview_pesan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.session = new SessionManager(getApplication());
        this.session.checkLoginPerempuan();
        strktp = this.session.getUserDetails().get(SessionManager.KEY_KTP);

        Log.e("Error", "Pesan : "+strktp);

        ReadPesanTask m = (ReadPesanTask) new ReadPesanTask().execute();
        this.list_pesan.setTextFilterEnabled(true);

        list_pesan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_pesan = ((TextView) view.findViewById(R.id.id_pesan)).getText().toString();
                String tanggal = ((TextView) view.findViewById(R.id.tanggal_pesan)).getText().toString();
                String nama_anak = ((TextView) view.findViewById(R.id.anak_nama)).getText().toString();
                String nama_orangtua = ((TextView) view.findViewById(R.id.orangtua_nama)).getText().toString();
                Intent i = new Intent(NotifikasiPesanActivity.this, DetailPesanActivity.class);
                Bundle b = new Bundle();
                b.putString("id_pesan", id_pesan);
                b.putString("tanggal", tanggal);
                b.putString("nama_anak", nama_anak);
                b.putString("nama_orangtua", nama_orangtua);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

    class ReadPesanTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_pesan;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list_pesan.setAdapter(null);
            pDialog = new ProgressDialog(NotifikasiPesanActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_pesan= "http://rekammedis.gudangtechno.web.id/android/tampil_pesan.php?no_ktp="+NotifikasiPesanActivity.this.strktp;

            Pesan tempPesan = new Pesan();
            try {
                JSONObject json = NotifikasiPesanActivity.this.jsonParser.getJSONFromUrl(url_tampil_pesan);

                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarpesan = json.getJSONArray("pesan");
                    for (int i = 0; i < daftarpesan.length(); i++) {
                        JSONObject c = daftarpesan.getJSONObject(i);
                        tempPesan = new Pesan();
                        tempPesan.setId_pesan(c.getString("id_pesan"));
                        tempPesan.setTanggal(c.getString("tanggal_kirim"));
                        tempPesan.setNama_anak(c.getString("nama_anak"));
                        tempPesan.setNama_orangtua(c.getString("nama_orangtua"));
                        daftar_pesan.add(tempPesan);
                    }
                    return "OK";
                } else {
                    return "no results";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(NotifikasiPesanActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(NotifikasiPesanActivity.this, "Daftar Pesan Kosong", Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                list_pesan.setAdapter(new PesanAdapter(NotifikasiPesanActivity.this, NotifikasiPesanActivity.this.daftar_pesan));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPerempuanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}