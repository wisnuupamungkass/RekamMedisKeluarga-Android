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

public class PetugasListRekamMedisOrangtua2Activity extends AppCompatActivity {
    Button tambah_medis;
    ListView list_rekammedis;

    ArrayList<RekamMedis> daftar_rekammedis = new ArrayList<>();
    JSONArray daftarrekammedis = null;
    protected JSONParser jsonParser;

    int success;

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_MEDIS = "rekammedis";
    protected static final String TAG_PERIKSA = "id_periksa";
    protected static final String TAG_TANGGAL = "tanggal";
    protected static final String TAG_KELUHAN = "keluhan";
    protected static final String TAG_RIWAYAT = "riwayat_penyakit";
    protected static final String TAG_DIAGNOSA = "diagnosa";
    protected static final String TAG_TINDAKAN = "tindakan_medis";
    protected static final String TAG_OBAT = "obat";
    protected static final String TAG_KTP = "no_ktp";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_PETUGAS = "id_petugas";

    protected String strktp,strpetugas;

    public PetugasListRekamMedisOrangtua2Activity(){
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_list_rekam_medis_orangtua);
        tambah_medis = (Button) findViewById(R.id.button_tambah_medis);
        list_rekammedis = (ListView) findViewById(R.id.listview_rekammedis);

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
        strktp = b.getString("no_ktp");
        strpetugas = b.getString("id_petugas");

        ReadRekamMedisTask m = (ReadRekamMedisTask) new ReadRekamMedisTask().execute();

        list_rekammedis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_periksa = ((TextView) view.findViewById(R.id.id_periksa)).getText().toString();
                String tanggal = ((TextView) view.findViewById(R.id.tanggal)).getText().toString();
                String keluhan = ((TextView) view.findViewById(R.id.keluhan)).getText().toString();
                String riwayat_penyakit = ((TextView) view.findViewById(R.id.riwayat_penyakit)).getText().toString();
                String diagnosa = ((TextView) view.findViewById(R.id.diagnosa)).getText().toString();
                String tindakan_medis = ((TextView) view.findViewById(R.id.tindakan_medis)).getText().toString();
                String obat = ((TextView) view.findViewById(R.id.obat)).getText().toString();
                String no_register = ((TextView) view.findViewById(R.id.no_register)).getText().toString();
                String no_ktp = ((TextView) view.findViewById(R.id.no_ktp)).getText().toString();

                Intent i = null;
                i = new Intent(PetugasListRekamMedisOrangtua2Activity.this, PetugasDetailRekamMedisOrtu2Activity.class);
                Bundle b = new Bundle();
                b.putString("id_periksa", id_periksa);
                b.putString("tanggal", tanggal);
                b.putString("keluhan", keluhan);
                b.putString("riwayat_penyakit", riwayat_penyakit);
                b.putString("diagnosa", diagnosa);
                b.putString("tindakan_medis", tindakan_medis);
                b.putString("obat", obat);
                b.putString("no_register", no_register);
                b.putString("no_ktp", no_ktp);
                b.putString("id_petugas", strpetugas);
                i.putExtras(b);
                startActivity(i);
            }
        });

        this.tambah_medis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PetugasListRekamMedisOrangtua2Activity.this, PetugasTambahRekamMedisOrangtua2Activity.class);
                Bundle b = new Bundle();
                b.putString("no_ktp", strktp);
                b.putString("id_petugas", strpetugas);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    class ReadRekamMedisTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_medis;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PetugasListRekamMedisOrangtua2Activity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_medis= "http://rekammedis.gudangtechno.web.id/android/tampil_medis.php?no_ktp="+PetugasListRekamMedisOrangtua2Activity.this.strktp;
            RekamMedis tempMedis = new RekamMedis();
            try {
                JSONObject json = PetugasListRekamMedisOrangtua2Activity.this.jsonParser.getJSONFromUrl(url_tampil_medis);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarrekammedis = json.getJSONArray(TAG_MEDIS);
                    for (int i = 0; i < daftarrekammedis.length(); i++) {
                        JSONObject c = daftarrekammedis.getJSONObject(i);
                        tempMedis = new RekamMedis();
                        tempMedis.setId_periksa(c.getString(TAG_PERIKSA));
                        tempMedis.setTanggal(c.getString(TAG_TANGGAL));
                        tempMedis.setKeluhan(c.getString(TAG_KELUHAN));
                        tempMedis.setRiwayat_penyakit(c.getString(TAG_RIWAYAT));
                        tempMedis.setDiagnosa(c.getString(TAG_DIAGNOSA));
                        tempMedis.setTindakan_medis(c.getString(TAG_TINDAKAN));
                        tempMedis.setObat(c.getString(TAG_OBAT));
                        tempMedis.setNo_noregister(c.getString(TAG_REGISTER));
                        tempMedis.setNo_ktp(c.getString(TAG_KTP));
                        tempMedis.setId_petugas(c.getString(TAG_PETUGAS));
                        daftar_rekammedis.add(tempMedis);
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
                Toast.makeText(PetugasListRekamMedisOrangtua2Activity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(PetugasListRekamMedisOrangtua2Activity.this, "Data Rekam Medis Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                list_rekammedis.setAdapter(new RekamMedisAdapter(PetugasListRekamMedisOrangtua2Activity.this, PetugasListRekamMedisOrangtua2Activity.this.daftar_rekammedis));
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
        finish();
    }
}


