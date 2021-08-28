package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetugasListImunisasiActivity extends AppCompatActivity {
    Button tambah_imunisasi;
    ListView list_imunisasi;

    ArrayList<Imunisasi> daftar_imunisasi = new ArrayList<>();
    JSONArray daftarimunisasi = null;
    protected JSONParser jsonParser;
    int success;

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_IMUNISASI = "imunisasi";
    protected static final String TAG_ID= "id_imunisasi";
    protected static final String TAG_TANGGAL = "tanggal";
    protected static final String TAG_NAMA_IMUN = "nama_imunisasi";
    protected static final String TAG_NAMA_PETUGAS = "nama_petugas";
    protected static final String TAG_REGISTER = "nama_anak";

    protected String strregister,strpetugas;

    public PetugasListImunisasiActivity() {
        this.jsonParser = new JSONParser();
    }

    public void onStart(){
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_list_imunisasi);
        tambah_imunisasi = (Button) findViewById(R.id.button_tambah_imunisasi);
        list_imunisasi = (ListView) findViewById(R.id.listview_imunisasi);

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
        Log.e("Error", "Token Anak=" + strregister);
        strpetugas = b.getString("id_petugas");
        Log.e("Error", "Token Petugas=" + strpetugas);

        ReadImunisasiTask m = (ReadImunisasiTask) new ReadImunisasiTask().execute();

        list_imunisasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_imunisasi = ((TextView) view.findViewById(R.id.id_imunisasi)).getText().toString();
                String tanggal = ((TextView) view.findViewById(R.id.tanggal)).getText().toString();
                String nama_imunisasi = ((TextView) view.findViewById(R.id.nama_imunisasi)).getText().toString();

                Intent i = new Intent(PetugasListImunisasiActivity.this, PetugasDetailImunisasiActivity.class);
                Bundle b = new Bundle();
                b.putString("id_imunisasi", id_imunisasi);
                b.putString("tanggal", tanggal);
                b.putString("nama_imunisasi", nama_imunisasi);
                b.putString("id_petugas", strpetugas);
                b.putString("no_register", strregister);
                i.putExtras(b);
                startActivity(i);
            }
        });

        tambah_imunisasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imunisasi = new Intent(PetugasListImunisasiActivity.this, PetugasTambahImunisasiActivity.class);
                Bundle b = new Bundle();
                b.putString("no_register", strregister);
                b.putString("id_petugas", strpetugas);
                imunisasi.putExtras(b);
                startActivity(imunisasi);
            }
        });
    }

    class ReadImunisasiTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_imunisasi;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PetugasListImunisasiActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_imunisasi= "http://rekammedis.gudangtechno.web.id/android/tampil_imunisasi.php?no_register="+PetugasListImunisasiActivity.this.strregister;

            Imunisasi tempImunisasi = new Imunisasi();
            try {
                JSONObject json = PetugasListImunisasiActivity.this.jsonParser.getJSONFromUrl(url_tampil_imunisasi);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarimunisasi = json.getJSONArray(TAG_IMUNISASI);
                    for (int i = 0; i < daftarimunisasi.length(); i++) {
                        JSONObject c = daftarimunisasi.getJSONObject(i);
                        tempImunisasi = new Imunisasi();
                        tempImunisasi.setId_imunisasi(c.getString(TAG_ID));
                        tempImunisasi.setTanggal(c.getString(TAG_TANGGAL));
                        tempImunisasi.setNama_imunisasi(c.getString(TAG_NAMA_IMUN));
                        tempImunisasi.setNama_petugas(c.getString(TAG_NAMA_PETUGAS));
                        tempImunisasi.setNo_register(c.getString(TAG_REGISTER));
                        daftar_imunisasi.add(tempImunisasi);
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
                Toast.makeText(PetugasListImunisasiActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(PetugasListImunisasiActivity.this, "Data Imunisasi Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                list_imunisasi.setAdapter(new ImunisasiAdapter(PetugasListImunisasiActivity.this, PetugasListImunisasiActivity.this.daftar_imunisasi));
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