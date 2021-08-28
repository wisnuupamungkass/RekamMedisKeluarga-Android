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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PetugasTambahImunisasiActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String url_tambah_imunisasi= "http://rekammedis.gudangtechno.web.id/android/tambah_imunisasi.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_TANGGAL = "tanggal";
    protected static final String TAG_ID_IMUN = "kode_imunisasi";
    protected static final String TAG_REGISTER = "no_register";
    protected static final String TAG_PETUGAS = "id_petugas";

    protected EditText tanggal_imunisasi;
    protected Spinner imunisasi;
    protected Button tambah_imunisasi;
    protected Button batal_imunisasi;
    protected SimpleDateFormat dateFormatter;
    protected DatePickerDialog tanggal_dialog;
    protected ProgressDialog pDialog;
    protected String strregister,strpetugas,strid_imunisasi;

    public  void onStart(){
        super.onStart();
        Bundle b = getIntent().getExtras();
        strregister = b.getString("no_register");
        strpetugas = b.getString("id_petugas");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_tambah_imunisasi);
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
        this.tanggal_imunisasi = (EditText) findViewById(R.id.tanggal);
        this.tanggal_imunisasi.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tanggal_imunisasi.requestFocus();
        setDateTimeField();
        this.imunisasi = (Spinner) findViewById(R.id.imunisasi);
        this.tambah_imunisasi = (Button) findViewById(R.id.btn_tambah_imunisasi);
        this.batal_imunisasi = (Button) findViewById(R.id.btn_batal_imunisasi);
        this.tambah_imunisasi.setOnClickListener(new tambah_imun());
        this.batal_imunisasi.setOnClickListener(new batal_imun());

        try {

            JSONArray data = new JSONArray(getJSONUrl("http://rekammedis.gudangtechno.web.id/android/getImunisasi.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_jenis", c.getString("id_jenis"));
                map.put("nama_imunisasi", c.getString("nama_imunisasi"));
                map.put("umur", c.getString("umur"));
                MyArrList.add(map);
            }

            this.imunisasi.setAdapter(new SimpleAdapter(this, MyArrList, R.layout.activity_spinner_imunisasi, new String[] {"id_jenis", "nama_imunisasi", "bulan"}, new int[] {R.id.id_jenis, R.id.nama_imunisasi, R.id.umur}));

            this.imunisasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_imunisasi = MyArrList.get(position).get("id_jenis");
                    Log.e("Error",""+strid_imunisasi);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(PetugasTambahImunisasiActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    class tambah_imun implements View.OnClickListener {
        public void onClick(View v) {
            PetugasTambahImunisasiActivity.this.validation();
        }
    }

    class batal_imun implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public void setDateTimeField() {
        this.tanggal_imunisasi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PetugasTambahImunisasiActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                PetugasTambahImunisasiActivity.this.tanggal_imunisasi.setText(PetugasTambahImunisasiActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    public void validation() {
        String tanggal = this.tanggal_imunisasi.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(tanggal)) {
            this.tanggal_imunisasi.setError("Harus diisi");
            focusView = this.tanggal_imunisasi;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new tambahImunisasi().execute();
    }

    public class tambahImunisasi extends AsyncTask<String, String, String> {
        String strtanggal;
        int success;

        public tambahImunisasi() {
            this.strtanggal = PetugasTambahImunisasiActivity.this.tanggal_imunisasi.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PetugasTambahImunisasiActivity.this.pDialog = new ProgressDialog(PetugasTambahImunisasiActivity.this);
            PetugasTambahImunisasiActivity.this.pDialog.setMessage("Loading...");
            PetugasTambahImunisasiActivity.this.pDialog.setIndeterminate(false);
            PetugasTambahImunisasiActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getImunisasiList();
        }

        public String getImunisasiList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(PetugasTambahImunisasiActivity.TAG_TANGGAL, this.strtanggal));
            params.add(new BasicNameValuePair(PetugasTambahImunisasiActivity.TAG_ID_IMUN, strid_imunisasi));
            params.add(new BasicNameValuePair(PetugasTambahImunisasiActivity.TAG_REGISTER, strregister));
            params.add(new BasicNameValuePair(PetugasTambahImunisasiActivity.TAG_PETUGAS, strpetugas));

            JSONObject json = jsonParser.makeHttpRequest(url_tambah_imunisasi, "POST", params);

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
            PetugasTambahImunisasiActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(PetugasTambahImunisasiActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(PetugasTambahImunisasiActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(PetugasTambahImunisasiActivity.this);
                pDialog.setTitle("Tambah Imunisasi");
                pDialog.setMessage("Data Imunisasi Berhasil Tersimpan");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else if (success == 0) {
                Toast.makeText(PetugasTambahImunisasiActivity.this.getApplicationContext(), "Gagal Tambah Data Imunisasi...", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                Intent imunisasi = new Intent(PetugasTambahImunisasiActivity.this, PetugasMenuAnakActivity.class);
                startActivity(imunisasi);
                PetugasTambahImunisasiActivity.this.finish();
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