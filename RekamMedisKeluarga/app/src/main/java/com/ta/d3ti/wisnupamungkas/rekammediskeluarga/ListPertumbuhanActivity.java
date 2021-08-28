package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListPertumbuhanActivity extends AppCompatActivity {
    ListView list_pertumbuhan;

    ArrayList<Pertumbuhan> daftar_pertumbuhan = new ArrayList<>();
    JSONArray daftarpertumbuhan = null;
    protected JSONParser jsonParser;
    int success;

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_PERTUMBUHAN = "pertumbuhan";
    protected static final String TAG_ID= "id_pertumbuhan";
    protected static final String TAG_TANGGAL = "tanggal";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_TINGGI = "tinggi_badan";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_PETUGAS = "nama_petugas";

    protected String strregister;

    public ListPertumbuhanActivity() {
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pertumbuhan);
        list_pertumbuhan = (ListView) findViewById(R.id.listview_pertumbuhan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle b = getIntent().getExtras();
        strregister = b.getString("no_register");
        ReadPertumbuhanTask m = (ReadPertumbuhanTask) new ReadPertumbuhanTask().execute();

        list_pertumbuhan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_pertumbuhan = ((TextView) view.findViewById(R.id.id_pertumbuhan)).getText().toString();
                String tanggal = ((TextView) view.findViewById(R.id.tanggal)).getText().toString();
                String lingkar_kepala = ((TextView) view.findViewById(R.id.lingkar)).getText().toString();
                String berat_badan = ((TextView) view.findViewById(R.id.berat)).getText().toString();
                String tinggi_badan = ((TextView) view.findViewById(R.id.tinggi)).getText().toString();
                String no_register = ((TextView) view.findViewById(R.id.no_register)).getText().toString();
                String nama_petugas = ((TextView) view.findViewById(R.id.nama_petugas)).getText().toString();

                Intent i = null;
                i = new Intent(ListPertumbuhanActivity.this, DetailPertumbuhanActivity.class);
                Bundle b = new Bundle();
                b.putString("id_pertumbuhan", id_pertumbuhan);
                b.putString("tanggal", tanggal);
                b.putString("lingkar_kepala", lingkar_kepala);
                b.putString("berat_badan", berat_badan);
                b.putString("tinggi_badan", tinggi_badan);
                b.putString("no_register", no_register);
                b.putString("nama_petugas", nama_petugas);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    class ReadPertumbuhanTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_pertumbuhan;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list_pertumbuhan.setAdapter(null);
            pDialog = new ProgressDialog(ListPertumbuhanActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_pertumbuhan= "http://rekammedis.gudangtechno.web.id/android/tampil_pertumbuhan.php?no_register="+ListPertumbuhanActivity.this.strregister;

            Pertumbuhan tempPertumbuhan = new Pertumbuhan();
            try {
                JSONObject json = ListPertumbuhanActivity.this.jsonParser.getJSONFromUrl(url_tampil_pertumbuhan);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarpertumbuhan = json.getJSONArray(TAG_PERTUMBUHAN);
                    for (int i = 0; i < daftarpertumbuhan.length(); i++) {
                        JSONObject c = daftarpertumbuhan.getJSONObject(i);
                        tempPertumbuhan = new Pertumbuhan();
                        tempPertumbuhan.setId_pertumbuhan(c.getString(TAG_ID));
                        tempPertumbuhan.setTanggal(c.getString(TAG_TANGGAL));
                        tempPertumbuhan.setLingkar_kepala(c.getString(TAG_LINGKAR));
                        tempPertumbuhan.setBerat_badan(c.getString(TAG_BERAT));
                        tempPertumbuhan.setTinggi_badan(c.getString(TAG_TINGGI));
                        tempPertumbuhan.setNo_register(c.getString(TAG_REGISTER));
                        tempPertumbuhan.setNama_petugas(c.getString(TAG_PETUGAS));
                        daftar_pertumbuhan.add(tempPertumbuhan);
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
                Toast.makeText(ListPertumbuhanActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(ListPertumbuhanActivity.this, "Data Pertumbuhan Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                list_pertumbuhan.setAdapter(new PertumbuhanAdapter(ListPertumbuhanActivity.this, ListPertumbuhanActivity.this.daftar_pertumbuhan));
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}