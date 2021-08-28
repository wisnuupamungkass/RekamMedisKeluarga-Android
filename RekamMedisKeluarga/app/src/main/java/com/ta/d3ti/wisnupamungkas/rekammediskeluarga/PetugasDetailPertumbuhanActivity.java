package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetugasDetailPertumbuhanActivity extends AppCompatActivity {
    protected static final String TAG_SUCCESS = "success";
    EditText tanggal_pertumbuhan;
    EditText lingkar_kepala;
    EditText berat_badan;
    EditText tinggi_badan;
    Button update_pertumbuhan;
    Button delete_pertumbuhan;
    String isi_register, isi_petugas,isi_pertumbuhan;

    protected JSONParser jsonParser;
    protected int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_detail_pertumbuhan);
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

        this.tanggal_pertumbuhan = (EditText) findViewById(R.id.tanggal);
        this.lingkar_kepala = (EditText) findViewById(R.id.lingkar_kepala);
        this.berat_badan = (EditText) findViewById(R.id.berat_badan);
        this.tinggi_badan = (EditText) findViewById(R.id.tinggi_badan);
        this.update_pertumbuhan = (Button) findViewById(R.id.btn_update_tumbuh);
        this.delete_pertumbuhan = (Button) findViewById(R.id.btn_delete_tumbuh);
        this.update_pertumbuhan.setOnClickListener(new update());
        this.delete_pertumbuhan.setOnClickListener(new delete());

        Bundle b = getIntent().getExtras();
        isi_pertumbuhan = b.getString("id_pertumbuhan");
        String isi_tanggal = b.getString("tanggal");
        String isi_lingkar = b.getString("lingkar_kepala");
        String isi_berat = b.getString("berat_badan");
        String isi_tinggi = b.getString("tinggi_badan");
        isi_register = b.getString("no_register");
        isi_petugas = b.getString("id_petugas");

        this.tanggal_pertumbuhan.setText(isi_tanggal);
        this.lingkar_kepala.setText(isi_lingkar);
        this.berat_badan.setText(isi_berat);
        this.tinggi_badan.setText(isi_tinggi);
    }

    class update implements View.OnClickListener {
        public void onClick(View v) {
            String tanggal = ((EditText) findViewById(R.id.tanggal)).getText().toString();
            String lingkar_kepala = ((EditText) findViewById(R.id.lingkar_kepala)).getText().toString();
            String berat_badan = ((EditText) findViewById(R.id.berat_badan)).getText().toString();
            String tinggi_badan = ((EditText) findViewById(R.id.tinggi_badan)).getText().toString();
            Intent pertumbuhan = new Intent(PetugasDetailPertumbuhanActivity.this, PetugasUpdatePertumbuhanActivity.class);
            Bundle b = new Bundle();
            b.putString("id_pertumbuhan", isi_pertumbuhan);
            b.putString("tanggal", tanggal);
            b.putString("lingkar_kepala", lingkar_kepala);
            b.putString("berat_badan", berat_badan);
            b.putString("tinggi_badan", tinggi_badan);
            b.putString("no_register", isi_register);
            b.putString("id_petugas", isi_petugas);
            pertumbuhan.putExtras(b);
            startActivity(pertumbuhan);
        }
    }

    class delete implements View.OnClickListener {
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PetugasDetailPertumbuhanActivity.this);
            builder.setMessage("Are you sure want to delete ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new hapus());
            builder.setNegativeButton("No", new batal());
            builder.create().show();
        }
    }

    class hapus implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            new hapusPertumbuhan().execute();
        }
    }

    class batal implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public PetugasDetailPertumbuhanActivity() {
        this.jsonParser = new JSONParser();
    }

    public class hapusPertumbuhan extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        String url_hapus_pertumbuhan;

        public hapusPertumbuhan() {
            this.url_hapus_pertumbuhan = "http://rekammedis.gudangtechno.web.id/android/hapus_pertumbuhan.php?id_pertumbuhan=" + PetugasDetailPertumbuhanActivity.this.isi_pertumbuhan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasDetailPertumbuhanActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = PetugasDetailPertumbuhanActivity.this.jsonParser.getJSONFromUrl(url_hapus_pertumbuhan);
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
                Intent tumbuh = new Intent(PetugasDetailPertumbuhanActivity.this, PetugasMenuAnakActivity.class);
                startActivity(tumbuh);
                PetugasDetailPertumbuhanActivity.this.finish();
            } else {
                Toast.makeText(PetugasDetailPertumbuhanActivity.this.getApplicationContext(), "Mohon Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}