package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TambahAnakActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_tambah_anak = "http://rekammedis.gudangtechno.web.id/android/register_anak.php";
    String url_registrasi_firebase= "http://rekammedis.gudangtechno.web.id/android/register_firebase.php";

    private static final String TAG = TambahAnakActivity.class.getSimpleName();
    protected static final String TAG_FIREBASE = "id_firebase";
    protected static final String TAG_KTP_NO = "no_ktp";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tanggal_lahir";
    protected static final String TAG_JAM = "jam_lahir";
    protected static final String TAG_JK = "gender";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_PANJANG = "panjang_badan";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_KTP = "ktp_no";

    protected SimpleDateFormat dateFormatter;
    protected String strjk;
    protected EditText nama;
    protected EditText tanggal_lahir;
    protected EditText berat_badan;
    protected EditText panjang_badan;
    protected EditText lingkar_kepala;
    protected EditText jam_lahir;
    protected Button tambah;
    protected Button cancel;
    protected ProgressDialog pDialog;
    protected RadioGroup rGroup;
    protected DatePickerDialog tanggal_dialog;

    SessionManager session;

    protected String strktp;
    protected String regId;

    public void onStart() {
        super.onStart();
        this.session = new SessionManager(getApplication());
        this.session.checkLoginPerempuan();

        strktp = this.session.getUserDetails().get(SessionManager.KEY_KTP);
        Log.e("Error : ",strktp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_anak);

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

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
        Log.e(TAG, "ID Reg Firebase : " + regId);

        this.nama = (EditText) findViewById(R.id.nama);
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.tanggal_lahir = (EditText) findViewById(R.id.tanggal_lahir);
        this.tanggal_lahir.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tanggal_lahir.requestFocus();
        setDateTimeField();
        this.berat_badan = (EditText) findViewById(R.id.berat);
        this.panjang_badan = (EditText) findViewById(R.id.panjang_badan);
        this.lingkar_kepala = (EditText) findViewById(R.id.lingkar);
        this.jam_lahir = (EditText) findViewById(R.id.jam_lahir);
        this.tambah = (Button) findViewById(R.id.tambah_anak);
        this.cancel = (Button) findViewById(R.id.button_batal);
        this.rGroup = (RadioGroup) findViewById(R.id.radioGender);

        this.tambah.setOnClickListener(new simpan());
        this.cancel.setOnClickListener(new batal());

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                RadioButton rb = (RadioButton) rGroup.findViewById(i);

                switch (i){
                    case R.id.radioPria :
                        strjk = "L";
                        break;
                    case R.id.radioPerempuan :
                        strjk = "P";
                        break;
                    default:
                        strjk = null;
                        break;
                }

            }
        });
    }

    public void setDateTimeField() {
        this.tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TambahAnakActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();

        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                TambahAnakActivity.this.tanggal_lahir.setText(TambahAnakActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    class simpan implements View.OnClickListener {
        public void onClick(View v) {
            TambahAnakActivity.this.validation();
        }
    }

    class batal implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void validation() {
        String name = this.nama.getText().toString();
        String tanggal = this.tanggal_lahir.getText().toString();
        String bb = this.berat_badan.getText().toString();
        String panjangbb = this.panjang_badan.getText().toString();
        String lingkar = this.lingkar_kepala.getText().toString();
        String jm_lahir = this.jam_lahir.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            this.nama.setError("Harus diisi");
            focusView = this.nama;
            cancel = true;
        }
        if (TextUtils.isEmpty(tanggal)) {
            this.tanggal_lahir.setError("Harus diisi");
            focusView = this.tanggal_lahir;
            cancel = true;
        }
        if (TextUtils.isEmpty(bb)) {
            this.berat_badan.setError("Harus diisi");
            focusView = this.berat_badan;
            cancel = true;
        }
        if (TextUtils.isEmpty(panjangbb)) {
            this.panjang_badan.setError("Harus diisi");
            focusView = this.panjang_badan;
            cancel = true;
        }
        if (TextUtils.isEmpty(lingkar)) {
            this.lingkar_kepala.setError("Harus diisi");
            focusView = this.lingkar_kepala;
            cancel = true;
        }
        if (TextUtils.isEmpty(jm_lahir)) {
            this.jam_lahir.setError("Harus diisi");
            focusView = this.jam_lahir;
            cancel = true;
        }
        if(strjk == null){
            Toast.makeText(TambahAnakActivity.this, "Jenis Kelamin Belum Dipilih...",  Toast.LENGTH_SHORT).show();
            focusView = rGroup;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new tambahAnak().execute();
    }

    public class tambahAnak extends AsyncTask<String, String, String> {
        String strnama;
        String strtanggal_lahir;
        String strbb;
        String strpanjang;
        String strlingkar;
        String strjmlahir;
        int success;

        public tambahAnak() {
            this.strnama = TambahAnakActivity.this.nama.getText().toString();
            this.strtanggal_lahir = TambahAnakActivity.this.tanggal_lahir.getText().toString();
            this.strbb = TambahAnakActivity.this.berat_badan.getText().toString();
            this.strpanjang = TambahAnakActivity.this.panjang_badan.getText().toString();
            this.strlingkar = TambahAnakActivity.this.lingkar_kepala.getText().toString();
            this.strjmlahir = TambahAnakActivity.this.jam_lahir.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            TambahAnakActivity.this.pDialog = new ProgressDialog(TambahAnakActivity.this);
            TambahAnakActivity.this.pDialog.setMessage("Loading...");
            TambahAnakActivity.this.pDialog.setIndeterminate(false);
            TambahAnakActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getAnakList();
        }

        public String getAnakList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_NAMA, this.strnama));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_TANGGAL, this.strtanggal_lahir));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_JK, strjk));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_JAM, this.strjmlahir));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_BERAT, this.strbb));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_PANJANG, this.strpanjang));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_LINGKAR, this.strlingkar));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_KTP, strktp));

            JSONObject json = jsonParser.makeHttpRequest(url_tambah_anak, "POST", params);

            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses =" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TambahAnakActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(TambahAnakActivity.this, "Periksa Koneksi Internet Anda...", Toast.LENGTH_SHORT).show();
            } else if (success == 1) {
                new registrasi_idfirebase().execute();
                AlertDialog.Builder pDialog = new AlertDialog.Builder(TambahAnakActivity.this);
                pDialog.setTitle("Informasi");
                pDialog.setMessage("Data Anak Berhasil Dibuat");
                pDialog.setNeutralButton("OK", new sukses()).show();
            } else {
                Toast.makeText(TambahAnakActivity.this.getApplicationContext(), "Gagal Tambah Data Anak... ", Toast.LENGTH_SHORT).show();
            }
        }

        protected class sukses implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                finish();
            }
        }
    }

    public class registrasi_idfirebase extends AsyncTask<String, String, String> {
        int success;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            return getIdFirebaseList();
        }

        public String getIdFirebaseList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_FIREBASE, regId));
            params.add(new BasicNameValuePair(TambahAnakActivity.TAG_KTP_NO, strktp));

            JSONObject json = jsonParser.makeHttpRequest(url_registrasi_firebase, "POST", params);

            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses =" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (success == 1) {
                Toast.makeText(TambahAnakActivity.this.getApplicationContext(), "ID Firebase Stored...", Toast.LENGTH_SHORT);
            } else if (success == 0) {
                Toast.makeText(TambahAnakActivity.this.getApplicationContext(), "Gagal Registrasi ID Firebase...", Toast.LENGTH_SHORT).show();
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