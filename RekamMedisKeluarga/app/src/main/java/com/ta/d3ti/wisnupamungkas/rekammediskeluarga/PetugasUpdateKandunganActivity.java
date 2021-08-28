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

public class PetugasUpdateKandunganActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_update_detail_kandungan= "http://rekammedis.gudangtechno.web.id/android/update_kandungan.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_DETAIL = "id_detail";
    protected static final String TAG_TANGGAL = "tanggal_periksa";
    protected static final String TAG_BULAN = "bulan_hamil";
    protected static final String TAG_KONDISI = "kondisi_janin";

    EditText tanggal_periksa;
    EditText bulan_hamil;
    EditText kondisi_janin;
    Button update_detail_kandungan;
    Button batal_detail_kandungan;
    protected SimpleDateFormat dateFormatter;
    protected DatePickerDialog tanggal_dialog;
    protected ProgressDialog dialog;
    protected String strid_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_update_kandungan);
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
        this.tanggal_periksa = (EditText) findViewById(R.id.tgl_periksa);
        this.tanggal_periksa.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tanggal_periksa.requestFocus();
        setDateTimeField();
        this.bulan_hamil = (EditText) findViewById(R.id.bln_hamil);
        this.kondisi_janin = (EditText) findViewById(R.id.kondisi_janin);
        this.update_detail_kandungan = (Button) findViewById(R.id.btn_update_kandungan);
        this.batal_detail_kandungan = (Button) findViewById(R.id.btn_batal_kandungan);
        this.update_detail_kandungan.setOnClickListener(new update_detail());
        this.batal_detail_kandungan.setOnClickListener(new batal_detail());

        Bundle b = getIntent().getExtras();
        strid_detail = b.getString("id_detail");
        Log.e("Error","ID Detail = "+strid_detail);

        String isi_tanggal = b.getString("tanggal_periksa");
        String isi_bulan = b.getString("bulan_hamil");
        String isi_kondisi = b.getString("kondisi_janin");

        this.tanggal_periksa.setText(isi_tanggal);
        this.bulan_hamil.setText(isi_bulan);
        this.kondisi_janin.setText(isi_kondisi);
    }

    public void setDateTimeField() {
        this.tanggal_periksa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PetugasUpdateKandunganActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                PetugasUpdateKandunganActivity.this.tanggal_periksa.setText(PetugasUpdateKandunganActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    class update_detail implements View.OnClickListener {
        public void onClick(View v) {
            PetugasUpdateKandunganActivity.this.validation();
        }
    }

    class batal_detail implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void validation() {
        String tanggal_periksa = this.tanggal_periksa.getText().toString();
        String bulan_hamil = this.bulan_hamil.getText().toString();
        String kondisi_janin = this.kondisi_janin.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(tanggal_periksa)) {
            this.tanggal_periksa.setError("Harus diisi");
            focusView = this.tanggal_periksa;
            cancel = true;
        }
        if (TextUtils.isEmpty(bulan_hamil)) {
            this.bulan_hamil.setError("Harus diisi");
            focusView = this.bulan_hamil;
            cancel = true;
        }
        if (TextUtils.isEmpty(kondisi_janin)) {
            this.kondisi_janin.setError("Harus diisi");
            focusView = this.kondisi_janin;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new updateDetailKandungan().execute();
    }

    public class updateDetailKandungan extends AsyncTask<String, String, String> {
        String strtanggal;
        String strbulan;
        String strkondisi;
        int success;

        public updateDetailKandungan() {
            this.strtanggal = PetugasUpdateKandunganActivity.this.tanggal_periksa.getText().toString();
            this.strbulan = PetugasUpdateKandunganActivity.this.bulan_hamil.getText().toString();
            this.strkondisi = PetugasUpdateKandunganActivity.this.kondisi_janin.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PetugasUpdateKandunganActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... params) {
            return getDetailKandunganList();
        }

        public String getDetailKandunganList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(PetugasUpdateKandunganActivity.TAG_DETAIL, strid_detail));
            params.add(new BasicNameValuePair(PetugasUpdateKandunganActivity.TAG_TANGGAL, this.strtanggal));
            params.add(new BasicNameValuePair(PetugasUpdateKandunganActivity.TAG_BULAN, this.strbulan));
            params.add(new BasicNameValuePair(PetugasUpdateKandunganActivity.TAG_KONDISI, this.strkondisi));

            JSONObject json = jsonParser.makeHttpRequest(url_update_detail_kandungan, "POST", params);
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
            dialog.dismiss();
            if (result.equalsIgnoreCase("Error Converting")) {
                Toast.makeText(PetugasUpdateKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(PetugasUpdateKandunganActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (success == 1) {
                AlertDialog.Builder pDialog = new AlertDialog.Builder(PetugasUpdateKandunganActivity.this);
                pDialog.setTitle("Update Detail Kandungan");
                pDialog.setMessage("Data Detail Kandungan Berhasil Diupdate");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            } else if (success == 0) {
                Toast.makeText(PetugasUpdateKandunganActivity.this.getApplicationContext(), "Gagal Update Data Kandungan...", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                Intent kandungan = new Intent(PetugasUpdateKandunganActivity.this, PetugasMenuPerempuanActivity.class);
                startActivity(kandungan);
                PetugasUpdateKandunganActivity.this.finish();
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
