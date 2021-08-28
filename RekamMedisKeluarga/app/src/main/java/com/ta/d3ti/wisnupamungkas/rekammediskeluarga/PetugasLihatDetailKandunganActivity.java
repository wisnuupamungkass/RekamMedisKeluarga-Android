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

public class PetugasLihatDetailKandunganActivity extends AppCompatActivity {
    protected static final String TAG_SUCCESS = "success";
    protected JSONParser jsonParser;
    protected int success;
    protected EditText tanggal_periksa;
    protected EditText bulan_hamil;
    protected EditText kondisi_janin;
    protected Button update_detail_kandungan;
    protected Button delete_detail_kandungan;
    protected String isi_id_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_lihat_detail_kandungan);
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

        this.tanggal_periksa = (EditText) findViewById(R.id.tgl_periksa);
        this.bulan_hamil = (EditText) findViewById(R.id.bln_hamil);
        this.kondisi_janin = (EditText) findViewById(R.id.kondisi);
        this.update_detail_kandungan = (Button) findViewById(R.id.btn_update_kandungan);
        this.delete_detail_kandungan = (Button) findViewById(R.id.btn_delete_kandungan);
        this.update_detail_kandungan.setOnClickListener(new edit_kandungan());
        this.delete_detail_kandungan.setOnClickListener(new hapus_kandungan());

        Bundle b = getIntent().getExtras();
        isi_id_detail = b.getString("id_detail");
        String isi_tanggal = b.getString("tanggal_periksa");
        String isi_bulan = b.getString("bulan_hamil");
        String isi_kondisi = b.getString("kondisi_janin");

        this.tanggal_periksa.setText(isi_tanggal);
        this.bulan_hamil.setText(isi_bulan);
        this.kondisi_janin.setText(isi_kondisi);
    }

    class edit_kandungan implements View.OnClickListener {
        public void onClick(View v) {
            String tanggal = ((EditText) findViewById(R.id.tgl_periksa)).getText().toString();
            String bulan = ((EditText) findViewById(R.id.bln_hamil)).getText().toString();
            String kondisi = ((EditText) findViewById(R.id.kondisi)).getText().toString();

            Intent kandungan = new Intent(PetugasLihatDetailKandunganActivity.this, PetugasUpdateKandunganActivity.class);
            Bundle b = new Bundle();
            b.putString("id_detail", isi_id_detail);
            b.putString("tanggal_periksa", tanggal);
            b.putString("bulan_hamil", bulan);
            b.putString("kondisi_janin", kondisi);
            kandungan.putExtras(b);
            startActivity(kandungan);
        }
    }

    class hapus_kandungan implements View.OnClickListener {
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PetugasLihatDetailKandunganActivity.this);
            builder.setMessage("Are you sure want to delete ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new hapus());
            builder.setNegativeButton("No", new batal());
            builder.create().show();
        }
    }

    class hapus implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            new hapusKandungan().execute();
        }
    }

    class batal implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public PetugasLihatDetailKandunganActivity() {
        this.jsonParser = new JSONParser();
    }

    public class hapusKandungan extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        String url_hapus_kandungan;

        public hapusKandungan() {
            this.url_hapus_kandungan = "http://rekammedis.gudangtechno.web.id/android/hapus_kandungan.php?id_detail=" + PetugasLihatDetailKandunganActivity.this.isi_id_detail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasLihatDetailKandunganActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = PetugasLihatDetailKandunganActivity.this.jsonParser.getJSONFromUrl(url_hapus_kandungan);
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
                Intent kandungan = new Intent(PetugasLihatDetailKandunganActivity.this, PetugasMenuPerempuanActivity.class);
                startActivity(kandungan);
                PetugasLihatDetailKandunganActivity.this.finish();
            } else {
                Toast.makeText(PetugasLihatDetailKandunganActivity.this.getApplicationContext(), "Mohon Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
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
