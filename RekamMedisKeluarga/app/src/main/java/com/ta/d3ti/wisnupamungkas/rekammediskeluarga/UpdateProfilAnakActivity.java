package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateProfilAnakActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    protected String url_update_anak= "http://rekammedis.gudangtechno.web.id/android/update_anak.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tanggal_lahir";
    protected static final String TAG_JAM = "jam_lahir";
    protected static final String TAG_JK = "gender";
    protected static final String TAG_BERAT = "berat_badan";
    protected static final String TAG_PANJANG = "panjang_badan";
    protected static final String TAG_LINGKAR = "lingkar_kepala";
    protected static final String TAG_KTP = "ktp_no";

    private SimpleDateFormat dateFormatter;
    protected String strjk;
    private EditText nama;
    protected EditText tanggal_lahir;
    protected EditText berat_badan;
    protected EditText panjang_badan;
    protected EditText lingkar_kepala;
    protected EditText jam_lahir;
    private Button update;
    private Button cancel;
    private ProgressDialog pDialog;
    protected RadioGroup rGroup;
    private DatePickerDialog tanggal_dialog;

    SessionManager session;

    private String isi_no_register;
    private String strktp;

    public void onStart() {
        super.onStart();
        this.session = new SessionManager(getApplication());
        this.session.checkLoginPerempuan();

        strktp = this.session.getUserDetails().get(SessionManager.KEY_KTP);

        Bundle b = getIntent().getExtras();
        isi_no_register = b.getString("no_register");
    }

    class update implements View.OnClickListener {
        public void onClick(View v) {
            UpdateProfilAnakActivity.this.validation();
        }
    }

    class cancel implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil_anak);

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
        UpdateProfilAnakActivity.this.nama = (EditText) findViewById(R.id.update_nama_anak);
        UpdateProfilAnakActivity.this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        UpdateProfilAnakActivity.this.tanggal_lahir = (EditText) findViewById(R.id.update_tanggal_lahir_anak);
        UpdateProfilAnakActivity.this.tanggal_lahir.setInputType(InputType.TYPE_CLASS_DATETIME);
        UpdateProfilAnakActivity.this.tanggal_lahir.requestFocus();
        setDateTimeField();
        UpdateProfilAnakActivity.this.rGroup = (RadioGroup) findViewById(R.id.radioGender);
        UpdateProfilAnakActivity.this.berat_badan = (EditText) findViewById(R.id.update_bb_anak);
        UpdateProfilAnakActivity.this.panjang_badan = (EditText) findViewById(R.id.update_panjang_anak);
        UpdateProfilAnakActivity.this.lingkar_kepala = (EditText) findViewById(R.id.update_lingkar_kepala);
        UpdateProfilAnakActivity.this.jam_lahir = (EditText) findViewById(R.id.update_jam_lahir);
        UpdateProfilAnakActivity.this.update = (Button) findViewById(R.id.btn_update_anak);
        UpdateProfilAnakActivity.this.cancel = (Button) findViewById(R.id.btn_cancel_anak);

        UpdateProfilAnakActivity.this.rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
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

        Bundle b = getIntent().getExtras();
        String isi_nama = b.getString("nama");
        String isi_tanggal = b.getString("tanggal");
        String isi_jam = b.getString("jam");
        String isi_berat = b.getString("berat");
        String isi_panjang = b.getString("panjang");
        String isi_lingkar = b.getString("lingkar");

        UpdateProfilAnakActivity.this.nama.setText(isi_nama);
        UpdateProfilAnakActivity.this.tanggal_lahir.setText(isi_tanggal);
        UpdateProfilAnakActivity.this.jam_lahir.setText(isi_jam);
        UpdateProfilAnakActivity.this.berat_badan.setText(isi_berat);
        UpdateProfilAnakActivity.this.panjang_badan.setText(isi_panjang);
        UpdateProfilAnakActivity.this.lingkar_kepala.setText(isi_lingkar);

        UpdateProfilAnakActivity.this.update.setOnClickListener(new update());
        UpdateProfilAnakActivity.this.cancel.setOnClickListener(new cancel());
    }

    public void setDateTimeField() {
        this.tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateProfilAnakActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                UpdateProfilAnakActivity.this.tanggal_lahir.setText(UpdateProfilAnakActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
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
            Toast.makeText(UpdateProfilAnakActivity.this, "Jenis Kelamin Belum Dipilih...",  Toast.LENGTH_SHORT).show();
            focusView = rGroup;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new updateAnak().execute();
    }

    public class updateAnak extends AsyncTask<String, String, String> {
        String strnama;
        String strtanggal_lahir;
        String strjam;
        String strbb;
        String strpanjang;
        String strlingkar;

        int success;

        public updateAnak() {
            this.strnama = UpdateProfilAnakActivity.this.nama.getText().toString();
            this.strtanggal_lahir = UpdateProfilAnakActivity.this.tanggal_lahir.getText().toString();
            this.strbb = UpdateProfilAnakActivity.this.berat_badan.getText().toString();
            this.strjam = UpdateProfilAnakActivity.this.jam_lahir.getText().toString();
            this.strpanjang = UpdateProfilAnakActivity.this.panjang_badan.getText().toString();
            this.strlingkar = UpdateProfilAnakActivity.this.lingkar_kepala.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            UpdateProfilAnakActivity.this.pDialog = new ProgressDialog(UpdateProfilAnakActivity.this);
            UpdateProfilAnakActivity.this.pDialog.setMessage("Loading...");
            UpdateProfilAnakActivity.this.pDialog.setIndeterminate(false);
            UpdateProfilAnakActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getAnakList();
        }

        public String getAnakList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_REGISTER, isi_no_register));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_NAMA, this.strnama));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_TANGGAL, this.strtanggal_lahir));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_JAM, this.strjam));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_JK, strjk));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_BERAT, this.strbb));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_PANJANG, this.strpanjang));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_LINGKAR, this.strlingkar));
            params.add(new BasicNameValuePair(UpdateProfilAnakActivity.TAG_KTP, strktp));

            JSONObject json = jsonParser.makeHttpRequest(url_update_anak, "POST", params);

            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("Error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            UpdateProfilAnakActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(UpdateProfilAnakActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(UpdateProfilAnakActivity.this);
                pDialog.setTitle("Informasi");
                pDialog.setMessage("Data Profil Berhasil Di Update");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else {
                Toast.makeText(UpdateProfilAnakActivity.this.getApplicationContext(), "Periksa Koneksi Internet Anda... ", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                UpdateProfilAnakActivity.this.finish();
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