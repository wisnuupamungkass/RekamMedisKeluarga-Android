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

public class PetugasUpdatePertumbuhanActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_update_pertumbuhan= "http://rekammedis.gudangtechno.web.id/android/update_pertumbuhan.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_PERTUMBUHAN = "id_pertumbuhan";
    protected static final String TAG_TANGGAL = "tanggal";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_TINGGI = "tinggi_badan";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_PETUGAS = "id_petugas";

    EditText tanggal_pertumbuhan;
    EditText lingkar_kepala;
    EditText berat_badan;
    EditText tinggi_badan;
    Button update_pertumbuhan;
    Button batal_pertumbuhan;
    protected SimpleDateFormat dateFormatter;
    protected DatePickerDialog tanggal_dialog;
    protected ProgressDialog pDialog;
    String strregister,strpetugas,strpertumbuhan;

    public  void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_update_pertumbuhan);
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
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.tanggal_pertumbuhan = (EditText) findViewById(R.id.tanggal);
        this.tanggal_pertumbuhan.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tanggal_pertumbuhan.requestFocus();
        setDateTimeField();
        this.lingkar_kepala = (EditText) findViewById(R.id.lingkar_kepala);
        this.berat_badan = (EditText) findViewById(R.id.berat_badan);
        this.tinggi_badan = (EditText) findViewById(R.id.tinggi_badan);
        this.update_pertumbuhan = (Button) findViewById(R.id.btn_update_pertumbuhan);
        this.batal_pertumbuhan = (Button) findViewById(R.id.btn_batal);
        this.update_pertumbuhan.setOnClickListener(new update_tumbuh());
        this.batal_pertumbuhan.setOnClickListener(new batal_tumbuh());

        Bundle b = getIntent().getExtras();
        strpertumbuhan = b.getString("id_pertumbuhan");
        String isi_tanggal = b.getString("tanggal");
        String isi_lingkar = b.getString("lingkar_kepala");
        String isi_berat = b.getString("berat_badan");
        String isi_tinggi = b.getString("lingkar_kepala");
        strregister = b.getString("no_register");
        strpetugas = b.getString("id_petugas");

        PetugasUpdatePertumbuhanActivity.this.tanggal_pertumbuhan.setText(isi_tanggal);
        PetugasUpdatePertumbuhanActivity.this.lingkar_kepala.setText(isi_lingkar);
        PetugasUpdatePertumbuhanActivity.this.berat_badan.setText(isi_berat);
        PetugasUpdatePertumbuhanActivity.this.tinggi_badan.setText(isi_tinggi);
    }

    class update_tumbuh implements View.OnClickListener {
        public void onClick(View v) {
            PetugasUpdatePertumbuhanActivity.this.validation();
        }
    }

    class batal_tumbuh implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void setDateTimeField() {
        this.tanggal_pertumbuhan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PetugasUpdatePertumbuhanActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                PetugasUpdatePertumbuhanActivity.this.tanggal_pertumbuhan.setText(PetugasUpdatePertumbuhanActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    public void validation() {
        String tanggal = this.tanggal_pertumbuhan.getText().toString();
        String lingkar = this.lingkar_kepala.getText().toString();
        String berat = this.berat_badan.getText().toString();
        String tinggi = this.tinggi_badan.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(tanggal)) {
            this.tanggal_pertumbuhan.setError("Harus diisi");
            focusView = this.tanggal_pertumbuhan;
            cancel = true;
        }
        if (TextUtils.isEmpty(lingkar)) {
            this.lingkar_kepala.setError("Harus diisi");
            focusView = this.lingkar_kepala;
            cancel = true;
        }
        if (TextUtils.isEmpty(berat)) {
            this.berat_badan.setError("Harus diisi");
            focusView = this.berat_badan;
            cancel = true;
        }
        if (TextUtils.isEmpty(tinggi)) {
            this.tinggi_badan.setError("Harus diisi");
            focusView = this.tinggi_badan;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new tambahPertumbuhan().execute();
    }
    public class tambahPertumbuhan extends AsyncTask<String, String, String> {
        String strtanggal;
        String strlingkar;
        String strberat;
        String strtinggi;
        int success;

        public tambahPertumbuhan() {
            this.strtanggal = PetugasUpdatePertumbuhanActivity.this.tanggal_pertumbuhan.getText().toString();
            this.strlingkar = PetugasUpdatePertumbuhanActivity.this.lingkar_kepala.getText().toString();
            this.strberat = PetugasUpdatePertumbuhanActivity.this.berat_badan.getText().toString();
            this.strtinggi = PetugasUpdatePertumbuhanActivity.this.tinggi_badan.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PetugasUpdatePertumbuhanActivity.this.pDialog = new ProgressDialog(PetugasUpdatePertumbuhanActivity.this);
            PetugasUpdatePertumbuhanActivity.this.pDialog.setMessage("Loading...");
            PetugasUpdatePertumbuhanActivity.this.pDialog.setIndeterminate(false);
            PetugasUpdatePertumbuhanActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getPertumbuhanList();
        }

        public String getPertumbuhanList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_PERTUMBUHAN, strpertumbuhan));
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_TANGGAL, this.strtanggal));
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_LINGKAR, this.strlingkar));
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_BERAT, this.strberat));
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_TINGGI, this.strtinggi));
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_REGISTER, strregister));
            params.add(new BasicNameValuePair(PetugasUpdatePertumbuhanActivity.TAG_PETUGAS, strpetugas));

            JSONObject json = jsonParser.makeHttpRequest(url_update_pertumbuhan, "POST", params);

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
            PetugasUpdatePertumbuhanActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(PetugasUpdatePertumbuhanActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(PetugasUpdatePertumbuhanActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(PetugasUpdatePertumbuhanActivity.this);
                pDialog.setTitle("Update Pertumbuhan");
                pDialog.setMessage("Data Pertumbuhan Berhasil Diupdate");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else if (success == 0) {
                Toast.makeText(PetugasUpdatePertumbuhanActivity.this.getApplicationContext(), "Gagal Update Data Pertumbuhan...", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                Intent tumbuh = new Intent(PetugasUpdatePertumbuhanActivity.this, PetugasMenuAnakActivity.class);
                startActivity(tumbuh);
                PetugasUpdatePertumbuhanActivity.this.finish();
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
