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

public class PetugasDetailImunisasiActivity extends AppCompatActivity {
    protected static final String TAG_SUCCESS = "success";
    EditText tanggal;
    EditText nama_imunisasi;
    Button update_imunisasi;
    Button delete_imunisasi;
    String isi_id_imun,isi_register,isi_petugas;
    protected JSONParser jsonParser;
    protected int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_detail_imunisasi);
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

        this.tanggal = (EditText) findViewById(R.id.tanggal);
        this.nama_imunisasi = (EditText) findViewById(R.id.jenis_imunisasi);
        this.update_imunisasi = (Button) findViewById(R.id.btn_update_imun);
        this.delete_imunisasi = (Button) findViewById(R.id.btn_delete_imun);
        this.update_imunisasi.setOnClickListener(new update());
        this.delete_imunisasi.setOnClickListener(new delete());

        Bundle b = getIntent().getExtras();
        isi_id_imun = b.getString("id_imunisasi");
        String isi_tanggal = b.getString("tanggal");
        String isi_imunisasi = b.getString("nama_imunisasi");
        isi_register = b.getString("no_register");

        Log.e("Error","No Register"+isi_register);

        isi_petugas = b.getString("id_petugas");

        this.tanggal.setText(isi_tanggal);
        this.nama_imunisasi.setText(isi_imunisasi);
    }

    class update implements View.OnClickListener {
        public void onClick(View v) {
            String tanggal = ((EditText) findViewById(R.id.tanggal)).getText().toString();
            String nama_imunisasi = ((EditText) findViewById(R.id.jenis_imunisasi)).getText().toString();
            Intent imunisasi = new Intent(PetugasDetailImunisasiActivity.this, PetugasUpdateImunisasiActivity.class);
            Bundle b = new Bundle();
            b.putString("id_imunisasi", isi_id_imun);
            b.putString("tanggal", tanggal);
            b.putString("nama_imunisasi", nama_imunisasi);
            b.putString("no_register", isi_register);
            b.putString("id_petugas", isi_petugas);
            imunisasi.putExtras(b);
            startActivity(imunisasi);
        }
    }

    class delete implements View.OnClickListener {
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PetugasDetailImunisasiActivity.this);
            builder.setMessage("Are you sure want to delete ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new hapus());
            builder.setNegativeButton("No", new batal());
            builder.create().show();
        }
    }

    class hapus implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            new hapusImunisasi().execute();
        }
    }

    class batal implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public PetugasDetailImunisasiActivity() {
        this.jsonParser = new JSONParser();
    }

    public class hapusImunisasi extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        String url_hapus_imunisasi;

        public hapusImunisasi() {
            this.url_hapus_imunisasi = "http://rekammedis.gudangtechno.web.id/android/hapus_imunisasi.php?id_imunisasi=" + PetugasDetailImunisasiActivity.this.isi_id_imun;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasDetailImunisasiActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = PetugasDetailImunisasiActivity.this.jsonParser.getJSONFromUrl(url_hapus_imunisasi);
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
                Intent imunisasi = new Intent(PetugasDetailImunisasiActivity.this, PetugasMenuAnakActivity.class);
                startActivity(imunisasi);
                PetugasDetailImunisasiActivity.this.finish();
            } else {
                Toast.makeText(PetugasDetailImunisasiActivity.this.getApplicationContext(), "Mohon Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
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
