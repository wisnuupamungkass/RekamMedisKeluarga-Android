package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;

@SuppressLint("NewApi")
public class RegisterPetugasActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    String url_register_petugas= "http://rekammedis.gudangtechno.web.id/android/register_petugas.php";

    // JSON Node names, ini harus sesuai yang di API
    protected static final String TAG_NIK = "id_petugas";
    protected static final String TAG_NAMA = "nama";
    protected static final String TAG_TANGGAL = "tgl_lahir";
    protected static final String TAG_JK = "jenis_kelamin";
    protected static final String TAG_ID_PROV = "id_provinsi";
    protected static final String TAG_ID_KAB = "id_kabupaten";
    protected static final String TAG_HP = "no_hp";
    protected static final String TAG_ALAMAT = "alamat";
    protected static final String TAG_EMAIL = "email";
    protected static final String TAG_PASS = "password";

    protected Spinner spinnerProvinsi;
    protected Spinner spinnerKabupaten;
    protected String strid_provinsi;
    protected String strid_kabupaten;
    protected SimpleDateFormat dateFormatter;
    protected String strjk;
    protected EditText nik;
    protected EditText nama;
    protected EditText tgl_lahir;
    protected EditText no_hp;
    protected EditText alamat;
    protected EditText email;
    protected EditText password;
    protected Button register;
    protected Button cancel;
    protected ProgressDialog pDialog;
    protected RadioGroup rGroup;
    protected DatePickerDialog tanggal_dialog;


    class register implements OnClickListener {
        public void onClick(View v) {
            RegisterPetugasActivity.this.validation();
        }
    }

    class cancel implements OnClickListener {
        public void onClick(View v) {
            RegisterPetugasActivity.this.startActivity(new Intent(RegisterPetugasActivity.this, LoginPetugasActivity.class));
        }
    }

    public class registerPetugas extends AsyncTask<String, String, String> {
        String strnik;
        String strnama;
        String strtgl_lahir;
        String strno_hp;
        String stralamat;
        String stremail;
        String strpassword;
        int success;

        public registerPetugas() {
            this.strnik = RegisterPetugasActivity.this.nik.getText().toString();
            this.strnama = RegisterPetugasActivity.this.nama.getText().toString();
            this.strtgl_lahir = RegisterPetugasActivity.this.tgl_lahir.getText().toString();
            this.strno_hp = RegisterPetugasActivity.this.no_hp.getText().toString();
            this.stralamat = RegisterPetugasActivity.this.alamat.getText().toString();
            this.stremail = RegisterPetugasActivity.this.email.getText().toString();
            this.strpassword = RegisterPetugasActivity.this.password.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            RegisterPetugasActivity.this.pDialog = new ProgressDialog(RegisterPetugasActivity.this);
            RegisterPetugasActivity.this.pDialog.setMessage("Loading...");
            RegisterPetugasActivity.this.pDialog.setIndeterminate(false);
            RegisterPetugasActivity.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getPetugasList();
        }

        public String getPetugasList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_NIK, this.strnik));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_NAMA, this.strnama));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_TANGGAL, this.strtgl_lahir));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_JK, strjk));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_HP, this.strno_hp));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_ID_PROV, strid_provinsi));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_ID_KAB, strid_kabupaten));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_ALAMAT, this.stralamat));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_EMAIL, this.stremail));
            params.add(new BasicNameValuePair(RegisterPetugasActivity.TAG_PASS, this.strpassword));

            JSONObject json = jsonParser.makeHttpRequest(url_register_petugas, "POST", params);

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
            RegisterPetugasActivity.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(RegisterPetugasActivity.this, "Periksa Koneksi Internet Anda...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                Builder pDialog = new Builder(RegisterPetugasActivity.this);
                pDialog.setTitle("Account Registration");
                pDialog.setMessage("Your Account Success Created");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else {
                Toast.makeText(RegisterPetugasActivity.this.getApplicationContext(), "NIK Telah Terdaftar... ", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            berhasil() {
            }

            public void onClick(DialogInterface dialog, int d) {
                dialog.dismiss();
                RegisterPetugasActivity.this.finish();
                RegisterPetugasActivity.this.startActivity(new Intent(RegisterPetugasActivity.this, LoginPetugasActivity.class));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_petugas);

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
        this.nik = (EditText) findViewById(R.id.nik);
        this.nama = (EditText) findViewById(R.id.nama);
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.tgl_lahir = (EditText) findViewById(R.id.tgl_lahir);
        this.tgl_lahir.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.tgl_lahir.requestFocus();
        setDateTimeField();
        this.no_hp = (EditText) findViewById(R.id.no_hp);
        this.spinnerProvinsi = (Spinner) findViewById(R.id.provinsi);
        this.spinnerKabupaten = (Spinner) findViewById(R.id.kabupaten);
        this.alamat = (EditText) findViewById(R.id.alamat);
        this.email = (EditText) findViewById(R.id.email);
        this.password = (EditText) findViewById(R.id.password);
        this.register = (Button) findViewById(R.id.button_register);
        this.cancel = (Button) findViewById(R.id.button_cancel);
        this.rGroup = (RadioGroup) findViewById(R.id.radioJk);

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

        try {

            JSONArray data = new JSONArray(getJSONUrl("http://rekammedis.gudangtechno.web.id/android/getProvinsi.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_provinsi", c.getString("id_provinsi"));
                map.put("nama_provinsi", c.getString("nama_provinsi"));
                MyArrList.add(map);

            }
            spinnerProvinsi.setAdapter(new SimpleAdapter(this, MyArrList, R.layout.activity_spinner_provinsi, new String[] {"id_provinsi", "nama_provinsi"}, new int[] {R.id.id_provinsi, R.id.nama_provinsi}));

            spinnerProvinsi.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_provinsi = MyArrList.get(position).get("id_provinsi");
                    Log.e("Error",""+strid_provinsi);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(RegisterPetugasActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.register.setOnClickListener(new register());
        this.cancel.setOnClickListener(new cancel());

        try {
            JSONArray data = new JSONArray(getJSONUrl("http://rekammedis.gudangtechno.web.id/android/getKabupaten.php"));

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

            spinnerKabupaten.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_kabupaten = MyArrList.get(position).get("id_kabupaten");
                    Log.e("Error",""+strid_kabupaten);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(RegisterPetugasActivity.this, "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
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
                RegisterPetugasActivity.this.tanggal_dialog.show();
            }
        });
        Calendar newCalender = Calendar.getInstance();
        this.tanggal_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                RegisterPetugasActivity.this.tgl_lahir.setText(RegisterPetugasActivity.this.dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DATE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            RegisterPetugasActivity.this.startActivity(new Intent(RegisterPetugasActivity.this, LoginPetugasActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        RegisterPetugasActivity.this.startActivity(new Intent(RegisterPetugasActivity.this, LoginPetugasActivity.class));
    }

    public class EmailValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }

    public void validation() {
        String ktp = this.nik.getText().toString();
        String name = this.nama.getText().toString();
        String tanggal = this.tgl_lahir.getText().toString();
        String hp = this.no_hp.getText().toString();
        String address = this.alamat.getText().toString();
        String mail = this.email.getText().toString();
        String pass = this.password.getText().toString();
        final EmailValidator emailValid = new EmailValidator();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(ktp)) {
            this.nik.setError("Harus diisi");
            focusView = this.nik;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            this.nama.setError("Harus diisi");
            focusView = this.nama;
            cancel = true;
        }
        if (TextUtils.isEmpty(tanggal)) {
            this.tgl_lahir.setError("Harus diisi");
            focusView = this.tgl_lahir;
            cancel = true;
        }
        if (TextUtils.isEmpty(hp)) {
            this.no_hp.setError("Harus diisi");
            focusView = this.no_hp;
            cancel = true;
        }
        if (TextUtils.isEmpty(address)) {
            this.alamat.setError("Harus diisi");
            focusView = this.alamat;
            cancel = true;
        }
        if (TextUtils.isEmpty(mail)) {
            this.email.setError("Harus diisi");
            focusView = this.email;
            cancel = true;
        }
        if (!emailValid.validate(mail)) {
            this.email.setError("Email Tidak Valid");
            focusView = this.email;
            this.email.setText("");
            cancel = true;
        }
        if (TextUtils.isEmpty(pass)) {
            this.password.setError("Harus diisi");
            focusView = this.password;
            cancel = true;
        }
        if(strjk == null){
            Toast.makeText(RegisterPetugasActivity.this, "Jenis Kelamin Belum Dipilih...",  Toast.LENGTH_SHORT).show();
            focusView = rGroup;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        new registerPetugas().execute();
    }
}