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

public class UpdateProfilPerempuanActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    protected String url_update_perempuan= "http://rekammedis.gudangtechno.web.id/android/update_orangtua.php";

    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tgl_lahir";
    protected static final String TAG_ID_PROV = "id_provinsi";
    protected static final String TAG_ID_KAB = "id_kabupaten";
    protected static final String TAG_HP = "no_hp";
    protected static final String TAG_ALAMAT = "alamat";
    protected static final String TAG_EMAIL = "email";

    protected Spinner spinnerProvinsi;
    protected Spinner spinnerKabupaten;
    protected String strid_provinsi;
    protected String strid_kabupaten;
    protected SimpleDateFormat dateFormatter;
    protected EditText nama;
    protected EditText tgl_lahir;
    protected EditText no_hp;
    protected EditText alamat;
    protected Button update;
    protected Button cancel;
    protected ProgressDialog pDialog;
    protected DatePickerDialog tanggal_dialog;
    protected String isi_email;

    class update implements View.OnClickListener {
        public void onClick(View v) {
            UpdateProfilPerempuanActivity.this.validation();
        }
    }

    class cancel implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    public class updatePerempuan extends AsyncTask<String, String, String> {
        String strnama;
        String strtgl_lahir;
        String strno_hp;
        String stralamat;
        int success;

        public updatePerempuan() {
            this.strnama = UpdateProfilPerempuanActivity.this.nama.getText().toString();
            this.strtgl_lahir = UpdateProfilPerempuanActivity.this.tgl_lahir.getText().toString();
            this.strno_hp = UpdateProfilPerempuanActivity.this.no_hp.getText().toString();
            this.stralamat = UpdateProfilPerempuanActivity.this.alamat.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            UpdateProfilPerempuanActivity.this.pDialog = new ProgressDialog(UpdateProfilPerempuanActivity.this);
            UpdateProfilPerempuanActivity.this.pDialog.setMessage("Loading...");
            UpdateProfilPerempuanActivity.this.pDialog.setIndeterminate(false);
            UpdateProfilPerempuanActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getPetugasList();
        }

        public String getPetugasList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_NAMA, this.strnama));
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_TANGGAL, this.strtgl_lahir));
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_HP, this.strno_hp));
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_ID_PROV, strid_provinsi));
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_ID_KAB, strid_kabupaten));
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_ALAMAT, this.stralamat));
            params.add(new BasicNameValuePair(UpdateProfilPerempuanActivity.TAG_EMAIL, isi_email));

            JSONObject json = jsonParser.makeHttpRequest(url_update_perempuan, "POST", params);

            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            UpdateProfilPerempuanActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(UpdateProfilPerempuanActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(UpdateProfilPerempuanActivity.this);
                pDialog.setTitle("Informasi");
                pDialog.setMessage("Data Profil Berhasil Di Update");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else {
                Toast.makeText(UpdateProfilPerempuanActivity.this.getApplicationContext(), "Periksa Koneksi Internet Anda... ", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                UpdateProfilPerempuanActivity.this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil_perempuan);

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
        UpdateProfilPerempuanActivity.this.nama = (EditText) findViewById(R.id.nama_perempuan);
        UpdateProfilPerempuanActivity.this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        UpdateProfilPerempuanActivity.this.tgl_lahir = (EditText) findViewById(R.id.tgl_lahir_perempuan);
        UpdateProfilPerempuanActivity.this.tgl_lahir.setInputType(InputType.TYPE_CLASS_DATETIME);
        UpdateProfilPerempuanActivity.this.tgl_lahir.requestFocus();
        setDateTimeField();

        UpdateProfilPerempuanActivity.this.no_hp = (EditText) findViewById(R.id.no_hp_perempuan);
        UpdateProfilPerempuanActivity.this.spinnerProvinsi = (Spinner) findViewById(R.id.provinsi);
        UpdateProfilPerempuanActivity.this.spinnerKabupaten = (Spinner) findViewById(R.id.kabupaten);
        UpdateProfilPerempuanActivity.this.alamat = (EditText) findViewById(R.id.alamat_perempuan);
        UpdateProfilPerempuanActivity.this.update = (Button) findViewById(R.id.btn_update_perempuan);
        UpdateProfilPerempuanActivity.this.cancel = (Button) findViewById(R.id.btn_cancel_perempuan);

        Bundle b = getIntent().getExtras();
        String isi_nik = b.getString("no_ktp");
        String isi_nama= b.getString("nama");
        String isi_tanggal= b.getString("tanggal");
        String isi_hp= b.getString("no_hp");
        String isi_alamat= b.getString("alamat");
        UpdateProfilPerempuanActivity.this.isi_email= b.getString("email");

        UpdateProfilPerempuanActivity.this.nama.setText(isi_nama);
        UpdateProfilPerempuanActivity.this.tgl_lahir.setText(isi_tanggal);
        UpdateProfilPerempuanActivity.this.no_hp.setText(isi_hp);
        UpdateProfilPerempuanActivity.this.alamat.setText(isi_alamat);

        try {

            JSONArray data = new JSONArray(getJSONUrl("http://rekammediskeluarga.hol.es/android/getProvinsi.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_provinsi", c.getString("id_provinsi"));
                map.put("nama_provinsi", c.getString("nama_provinsi"));
                MyArrList.add(map);

            }
            UpdateProfilPerempuanActivity.this.spinnerProvinsi.setAdapter(new SimpleAdapter(this, MyArrList, R.layout.activity_spinner_provinsi, new String[] {"id_provinsi", "nama_provinsi"}, new int[] {R.id.id_provinsi, R.id.nama_provinsi}));

            UpdateProfilPerempuanActivity.this.spinnerProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    UpdateProfilPerempuanActivity.this.strid_provinsi = MyArrList.get(position).get("id_provinsi");
                    Log.e("Error",""+UpdateProfilPerempuanActivity.this.strid_provinsi);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(UpdateProfilPerempuanActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UpdateProfilPerempuanActivity.this.update.setOnClickListener(new update());
        UpdateProfilPerempuanActivity.this.cancel.setOnClickListener(new cancel());

        try {
            JSONArray data = new JSONArray(getJSONUrl("http://rekammediskeluarga.hol.es/android/getKabupaten.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_kabupaten", c.getString("id_kabupaten"));
                map.put("nama_kabupaten", c.getString("nama_kabupaten"));
                MyArrList.add(map);

            }
            spinnerKabupaten.setAdapter(new SimpleAdapter(this, MyArrList, R.layout.activity_spinner_kabupaten, new String[] {"id_kabupaten", "nama_kabupaten"}, new int[] {R.id.id_kabupaten, R.id.nama_kabupaten}));

            spinnerKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_kabupaten = MyArrList.get(position).get("id_kabupaten");
                    Log.e("Error",""+strid_kabupaten);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(UpdateProfilPerempuanActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
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

    public void setDateTimeField() {
        this.tgl_lahir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateProfilPerempuanActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                UpdateProfilPerempuanActivity.this.tgl_lahir.setText(UpdateProfilPerempuanActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
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

    public void validation() {
        String name = UpdateProfilPerempuanActivity.this.nama.getText().toString();
        String tanggal = UpdateProfilPerempuanActivity.this.tgl_lahir.getText().toString();
        String hp = UpdateProfilPerempuanActivity.this.no_hp.getText().toString();
        String address = UpdateProfilPerempuanActivity.this.alamat.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            UpdateProfilPerempuanActivity.this.nama.setError("Harus diisi");
            focusView = UpdateProfilPerempuanActivity.this.nama;
            cancel = true;
        }
        if (TextUtils.isEmpty(tanggal)) {
            UpdateProfilPerempuanActivity.this.tgl_lahir.setError("Harus diisi");
            focusView = UpdateProfilPerempuanActivity.this.tgl_lahir;
            cancel = true;
        }
        if (TextUtils.isEmpty(hp)) {
            UpdateProfilPerempuanActivity.this.no_hp.setError("Harus diisi");
            focusView = UpdateProfilPerempuanActivity.this.no_hp;
            cancel = true;
        }
        if (TextUtils.isEmpty(address)) {
            UpdateProfilPerempuanActivity.this.alamat.setError("Harus diisi");
            focusView = UpdateProfilPerempuanActivity.this.alamat;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new updatePerempuan().execute();
    }
}
