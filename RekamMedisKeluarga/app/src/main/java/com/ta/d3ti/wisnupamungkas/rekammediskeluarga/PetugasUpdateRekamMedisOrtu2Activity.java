package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PetugasUpdateRekamMedisOrtu2Activity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_update_medis= "http://rekammedis.gudangtechno.web.id/android/update_rekammedis.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ID_REKAM = "id_periksa";
    protected static final String TAG_TANGGAL = "tanggal";
    protected static final String TAG_KELUHAN = "keluhan";
    protected static final String TAG_RIWAYAT = "riwayat_penyakit";
    protected static final String TAG_DIAGNOSA = "diagnosa";
    protected static final String TAG_TINDAKAN = "tindakan_medis";
    protected static final String TAG_OBAT = "obat";
    protected static final String TAG_KTP = "no_ktp";
    protected static final String TAG_PETUGAS = "id_petugas";

    EditText tanggal_pemeriksaan;
    EditText keluhan;
    EditText riwayat_penyakit;
    EditText diagnosa;
    EditText tindakan_medis;
    EditText obat;
    Button update_medis;
    Button batal_medis;

    protected SimpleDateFormat dateFormatter;
    protected DatePickerDialog tanggal_dialog;
    protected ProgressDialog pDialog;
    String strregister,strpetugas,stridrekam,strktp;

    public  void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_update_rekam_medis_ortu);
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

        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.tanggal_pemeriksaan = (EditText) findViewById(R.id.tanggal);
        this.tanggal_pemeriksaan.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tanggal_pemeriksaan.requestFocus();
        setDateTimeField();
        this.keluhan = (EditText) findViewById(R.id.keluhan);
        this.riwayat_penyakit = (EditText) findViewById(R.id.riwayat_penyakit);
        this.diagnosa = (EditText) findViewById(R.id.diagnosa);
        this.tindakan_medis = (EditText) findViewById(R.id.tindakan_medis);
        this.obat = (EditText) findViewById(R.id.obat);
        this.update_medis = (Button) findViewById(R.id.btn_update_medis);
        this.batal_medis = (Button) findViewById(R.id.btn_batal_medis);
        this.update_medis.setOnClickListener(new update_rekam());
        this.batal_medis.setOnClickListener(new batal_rekam());

        Bundle b = getIntent().getExtras();
        stridrekam = b.getString("id_periksa");
        String isi_tanggal = b.getString("tanggal");
        String isi_keluhan= b.getString("keluhan");
        String isi_penyakit= b.getString("riwayat_penyakit");
        String isi_diagnosa= b.getString("diagnosa");
        String isi_tindakan= b.getString("tindakan_medis");
        String isi_obat= b.getString("obat");
        strktp = b.getString("no_ktp");
        strregister = b.getString("no_register");
        strpetugas = b.getString("id_petugas");

        this.tanggal_pemeriksaan.setText(isi_tanggal);
        this.keluhan.setText(isi_keluhan);
        this.riwayat_penyakit.setText(isi_penyakit);
        this.diagnosa.setText(isi_diagnosa);
        this.tindakan_medis.setText(isi_tindakan);
        this.obat.setText(isi_obat);
    }

    class update_rekam implements View.OnClickListener {
        public void onClick(View v) {
            PetugasUpdateRekamMedisOrtu2Activity.this.validation();
        }
    }

    class batal_rekam implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void setDateTimeField() {
        this.tanggal_pemeriksaan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PetugasUpdateRekamMedisOrtu2Activity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                PetugasUpdateRekamMedisOrtu2Activity.this.tanggal_pemeriksaan.setText(PetugasUpdateRekamMedisOrtu2Activity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    public void validation() {
        String tanggal = this.tanggal_pemeriksaan.getText().toString();
        String keluhan = this.keluhan.getText().toString();
        String riwayat = this.riwayat_penyakit.getText().toString();
        String diagnosa = this.diagnosa.getText().toString();
        String medis = this.tindakan_medis.getText().toString();
        String obat = this.obat.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(tanggal)) {
            this.tanggal_pemeriksaan.setError("Harus diisi");
            focusView = this.tanggal_pemeriksaan;
            cancel = true;
        }
        if (TextUtils.isEmpty(keluhan)) {
            this.keluhan.setError("Harus diisi");
            focusView = this.keluhan;
            cancel = true;
        }
        if (TextUtils.isEmpty(riwayat)) {
            this.riwayat_penyakit.setError("Harus diisi");
            focusView = this.riwayat_penyakit;
            cancel = true;
        }
        if (TextUtils.isEmpty(diagnosa)) {
            this.diagnosa.setError("Harus diisi");
            focusView = this.diagnosa;
            cancel = true;
        }
        if (TextUtils.isEmpty(medis)) {
            this.tindakan_medis.setError("Harus diisi");
            focusView = this.tindakan_medis;
            cancel = true;
        }
        if (TextUtils.isEmpty(obat)) {
            this.obat.setError("Harus diisi");
            focusView = this.obat;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new updateMedisOrtu().execute();
    }
    public class updateMedisOrtu extends AsyncTask<String, String, String> {
        String strtanggal;
        String strkeluhan;
        String strriwayat;
        String strdiagnosa;
        String strmedis;
        String strobat;
        int success;

        public updateMedisOrtu() {
            this.strtanggal = PetugasUpdateRekamMedisOrtu2Activity.this.tanggal_pemeriksaan.getText().toString();
            this.strkeluhan = PetugasUpdateRekamMedisOrtu2Activity.this.keluhan.getText().toString();
            this.strriwayat = PetugasUpdateRekamMedisOrtu2Activity.this.riwayat_penyakit.getText().toString();
            this.strdiagnosa = PetugasUpdateRekamMedisOrtu2Activity.this.diagnosa.getText().toString();
            this.strmedis = PetugasUpdateRekamMedisOrtu2Activity.this.tindakan_medis.getText().toString();
            this.strobat = PetugasUpdateRekamMedisOrtu2Activity.this.obat.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PetugasUpdateRekamMedisOrtu2Activity.this.pDialog = new ProgressDialog(PetugasUpdateRekamMedisOrtu2Activity.this);
            PetugasUpdateRekamMedisOrtu2Activity.this.pDialog.setMessage("Loading...");
            PetugasUpdateRekamMedisOrtu2Activity.this.pDialog.setIndeterminate(false);
            PetugasUpdateRekamMedisOrtu2Activity.this.pDialog.show();
        }
        protected String doInBackground(String... params) {
            return getPertumbuhanList();
        }

        public String getPertumbuhanList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_ID_REKAM, stridrekam));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_TANGGAL, this.strtanggal));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_KELUHAN, this.strkeluhan));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_RIWAYAT, this.strriwayat));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_DIAGNOSA, this.strdiagnosa));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_TINDAKAN, this.strmedis));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_OBAT, this.strobat));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_KTP, strktp));
            params.add(new BasicNameValuePair(PetugasUpdateRekamMedisOrtu2Activity.TAG_PETUGAS, strpetugas));

            JSONObject json = jsonParser.makeHttpRequest(url_update_medis, "POST", params);

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
            PetugasUpdateRekamMedisOrtu2Activity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(PetugasUpdateRekamMedisOrtu2Activity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(PetugasUpdateRekamMedisOrtu2Activity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(PetugasUpdateRekamMedisOrtu2Activity.this);
                pDialog.setTitle("Update Rekam Medis Pria");
                pDialog.setMessage("Data Rekam Medis Berhasil Terupdate");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else if (success == 0) {
                Toast.makeText(PetugasUpdateRekamMedisOrtu2Activity.this.getApplicationContext(), "Gagal Update Data Rekam Medis...", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                Intent medis = new Intent(PetugasUpdateRekamMedisOrtu2Activity.this, PetugasMenuPriaActivity.class);
                startActivity(medis);
                PetugasUpdateRekamMedisOrtu2Activity.this.finish();
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


