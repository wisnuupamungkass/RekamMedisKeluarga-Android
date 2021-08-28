package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PetugasDetailRekamMedisActivity extends AppCompatActivity {
    protected static final String TAG_SUCCESS = "success";
    protected EditText tanggal_pemeriksaan;
    protected EditText keluhan;
    protected EditText riwayat_penyakit;
    protected EditText diagnosa;
    protected EditText tindakan_medis;
    protected EditText obat;
    protected Button update_medis;
    protected Button delete_medis;
    protected String isi_periksa,isi_ktp,isi_register,isi_petugas;

    protected JSONParser jsonParser;
    protected int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_detail_rekam_medis);
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

        this.tanggal_pemeriksaan = (EditText) findViewById(R.id.tanggal);
        this.keluhan = (EditText) findViewById(R.id.keluhan);
        this.riwayat_penyakit = (EditText) findViewById(R.id.riwayat_penyakit);
        this.diagnosa = (EditText) findViewById(R.id.diagnosa);
        this.tindakan_medis = (EditText) findViewById(R.id.tindakan_medis);
        this.obat = (EditText) findViewById(R.id.obat);
        this.update_medis = (Button) findViewById(R.id.btn_update_medis);
        this.delete_medis = (Button) findViewById(R.id.btn_delete_medis);
        this.update_medis.setOnClickListener(new edit_medis());
        this.delete_medis.setOnClickListener(new hapus_medis());

        Bundle b = getIntent().getExtras();
        isi_periksa = b.getString("id_periksa");
        String isi_tanggal = b.getString("tanggal");
        String isi_keluhan= b.getString("keluhan");
        String isi_penyakit= b.getString("riwayat_penyakit");
        String isi_diagnosa= b.getString("diagnosa");
        String isi_tindakan= b.getString("tindakan_medis");
        String isi_obat= b.getString("obat");
        isi_ktp = b.getString("no_ktp");
        isi_register = b.getString("no_register");
        isi_petugas = b.getString("id_petugas");

        this.tanggal_pemeriksaan.setText(isi_tanggal);
        this.keluhan.setText(isi_keluhan);
        this.riwayat_penyakit.setText(isi_penyakit);
        this.diagnosa.setText(isi_diagnosa);
        this.tindakan_medis.setText(isi_tindakan);
        this.obat.setText(isi_obat);
    }

    class edit_medis implements View.OnClickListener{
        public void onClick(View v) {
            String tanggal = ((EditText) findViewById(R.id.tanggal)).getText().toString();
            String keluhan = ((EditText) findViewById(R.id.keluhan)).getText().toString();
            String riwayat = ((EditText) findViewById(R.id.riwayat_penyakit)).getText().toString();
            String diagnosa = ((EditText) findViewById(R.id.diagnosa)).getText().toString();
            String medis = ((EditText) findViewById(R.id.tindakan_medis)).getText().toString();
            String obat = ((EditText) findViewById(R.id.obat)).getText().toString();
            Intent i = new Intent(PetugasDetailRekamMedisActivity.this, PetugasUpdateRekamMedisAnakActivity.class);
            Bundle b = new Bundle();
            b.putString("id_periksa", isi_periksa);
            b.putString("tanggal", tanggal);
            b.putString("keluhan", keluhan);
            b.putString("riwayat_penyakit", riwayat);
            b.putString("diagnosa", diagnosa);
            b.putString("tindakan_medis", medis);
            b.putString("obat", obat);
            b.putString("no_register", isi_register);
            b.putString("no_ktp", isi_ktp);
            b.putString("id_petugas", isi_petugas);
            i.putExtras(b);
            startActivity(i);
        }
    }

    class hapus_medis implements View.OnClickListener{
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(PetugasDetailRekamMedisActivity.this);
            builder.setMessage("Are you sure want to delete ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new hapus());
            builder.setNegativeButton("No", new batal());
            builder.create().show();
        }

        class hapus implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int id) {
                new hapusMedis().execute();
            }
        }

        class batal implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }
    }

    public PetugasDetailRekamMedisActivity() {
        this.jsonParser = new JSONParser();
    }

    public class hapusMedis extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        String url_hapus_medis;

        public hapusMedis() {
            this.url_hapus_medis = "http://rekammedis.gudangtechno.web.id/android/hapus_rekammedis.php?id_periksa=" + PetugasDetailRekamMedisActivity.this.isi_periksa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasDetailRekamMedisActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = PetugasDetailRekamMedisActivity.this.jsonParser.getJSONFromUrl(url_hapus_medis);
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    Log.e("OK", " Ambil Data");
                }
            } catch (JSONException e) {
                Log.e("Error", " Tidak Bisa Ambil Data");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (success == 1) {
                Intent medis = new Intent(PetugasDetailRekamMedisActivity.this, PetugasMenuAnakActivity.class);
                startActivity(medis);
                PetugasDetailRekamMedisActivity.this.finish();
            } else {
                Toast.makeText(PetugasDetailRekamMedisActivity.this.getApplicationContext(), "Mohon Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            }
        }
    }

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