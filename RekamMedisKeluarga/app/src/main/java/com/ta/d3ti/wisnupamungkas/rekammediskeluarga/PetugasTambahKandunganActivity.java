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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetugasTambahKandunganActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_tambah_kandungan= "http://rekammedis.gudangtechno.web.id/android/tambah_kandungan.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_KANDUNGAN = "kandungan_ke";
    protected static final String TAG_KTP = "no_ktp";

    protected EditText kandungan_ke;
    protected String strktp;
    protected Button tambah_kandungan;
    protected Button batal_kandungan;
    protected ProgressDialog pDialog;

    public  void onStart(){
        super.onStart();
        Bundle b = getIntent().getExtras();
        strktp = b.getString("no_ktp");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_tambah_kandungan);
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
        this.kandungan_ke = (EditText) findViewById(R.id.kandungan_ke);
        this.tambah_kandungan = (Button) findViewById(R.id.btn_tambah_kandungan);
        this.batal_kandungan = (Button) findViewById(R.id.btn_batal_kandungan);
        this.tambah_kandungan.setOnClickListener(new tambah_hamil());
        this.batal_kandungan.setOnClickListener(new batal_hamil());
    }

    class tambah_hamil implements View.OnClickListener {
        public void onClick(View v) {
            PetugasTambahKandunganActivity.this.validation();
        }
    }

    class batal_hamil implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void validation() {
        String anak_ke = this.kandungan_ke.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(anak_ke)) {
            this.kandungan_ke.setError("Harus diisi");
            focusView = this.kandungan_ke;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new tambahKandungan().execute();
    }

    public class tambahKandungan extends AsyncTask<String, String, String> {
        String strkandungan_ke;
        int success;

        public tambahKandungan() {
            this.strkandungan_ke = PetugasTambahKandunganActivity.this.kandungan_ke.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PetugasTambahKandunganActivity.this.pDialog = new ProgressDialog(PetugasTambahKandunganActivity.this);
            PetugasTambahKandunganActivity.this.pDialog.setMessage("Loading...");
            PetugasTambahKandunganActivity.this.pDialog.setIndeterminate(false);
            PetugasTambahKandunganActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getKandunganList();
        }

        public String getKandunganList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(PetugasTambahKandunganActivity.TAG_KANDUNGAN, this.strkandungan_ke));
            params.add(new BasicNameValuePair(PetugasTambahKandunganActivity.TAG_KTP, strktp));

            JSONObject json = jsonParser.makeHttpRequest(url_tambah_kandungan, "POST", params);
            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PetugasTambahKandunganActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")) {
                Toast.makeText(PetugasTambahKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(PetugasTambahKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (success == 1) {
                AlertDialog.Builder pDialog = new AlertDialog.Builder(PetugasTambahKandunganActivity.this);
                pDialog.setTitle("Tambah Kandungan");
                pDialog.setMessage("Data Kandungan Berhasil Tersimpan");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            } else if (success == 0) {
                Toast.makeText(PetugasTambahKandunganActivity.this.getApplicationContext(), "Gagal Tambah Data Kandungan...", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                Intent kandungan = new Intent(PetugasTambahKandunganActivity.this, PetugasMenuPerempuanActivity.class);
                startActivity(kandungan);
                PetugasTambahKandunganActivity.this.finish();
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
